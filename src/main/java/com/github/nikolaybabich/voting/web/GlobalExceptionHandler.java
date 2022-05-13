package com.github.nikolaybabich.voting.web;

import com.github.nikolaybabich.voting.error.AppException;
import com.github.nikolaybabich.voting.error.DataConflictException;
import com.github.nikolaybabich.voting.util.validation.ValidationUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.EntityNotFoundException;
import java.util.Map;
import java.util.stream.Collectors;

import static com.github.nikolaybabich.voting.util.ErrorUtil.parseIdFromConstraintMessage;
import static com.github.nikolaybabich.voting.util.ErrorUtil.parseNotFoundMessage;
import static org.springframework.boot.web.error.ErrorAttributeOptions.Include.MESSAGE;

@RestControllerAdvice
@AllArgsConstructor
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    public static final String EXCEPTION_DUPLICATE_RESTAURANT = "Restaurant with the same name and address already exists (existing id=%s)";
    public static final String EXCEPTION_DUPLICATE_MENU = "Menu for this date already exists for the restaurant (existing id=%s)";
    public static final String EXCEPTION_DUPLICATE_DISH = "Dish with the same name and price already exists (existing id=%s)";
    public static final String EXCEPTION_DUPLICATE_VOTE = "Your today's vote already exists (existing id=%s), you can try to change it";
    public static final String EXCEPTION_NO_RESTAURANT = "Restaurant with id=%s was not found";
    public static final String EXCEPTION_DELETE_DISH = "Dish id=%s is contained in existing menu and can't be deleted";

    private static final Map<String, String> CONSTRAINTS_MAP = Map.of(
            "uk__restaurants__address__name", EXCEPTION_DUPLICATE_RESTAURANT,
            "uk__menus__actual_date__restaurant_id", EXCEPTION_DUPLICATE_MENU,
            "uk__dishes__name__price", EXCEPTION_DUPLICATE_DISH,
            "uk__votes__user_id__actual_date", EXCEPTION_DUPLICATE_VOTE,
            "fk__menus__restaurants", EXCEPTION_NO_RESTAURANT,
            "fk__votes__restaurants", EXCEPTION_NO_RESTAURANT,
            "fk__menu_dishes__dishes", EXCEPTION_DELETE_DISH
    );
    private final ErrorAttributes errorAttributes;

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> handleDataIntegrityViolation(WebRequest request, DataIntegrityViolationException ex) {
        String rootMsg = ValidationUtil.getRootCause(ex).getMessage();
        if (rootMsg != null) {
            String lowerCaseMsg = rootMsg.toLowerCase();
            for (Map.Entry<String, String> entry : CONSTRAINTS_MAP.entrySet()) {
                if (lowerCaseMsg.contains(entry.getKey())) {
                    String msg = entry.getValue().formatted(parseIdFromConstraintMessage(lowerCaseMsg));
                    log.error("DataIntegrityViolationException: {}", msg);
                    return createResponseEntity(request, ErrorAttributeOptions.defaults(), msg, HttpStatus.UNPROCESSABLE_ENTITY);
                }
            }
        }
        log.error("DataIntegrityViolationException ", ex);
        return createResponseEntity(request, ErrorAttributeOptions.defaults(), ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AppException.class)
    public ResponseEntity<?> appException(WebRequest request, AppException ex) {
        log.error("ApplicationException: {}", ex.getMessage());
        return createResponseEntity(request, ex.getOptions(), null, ex.getStatus());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> entityNotFoundException(WebRequest request, EntityNotFoundException ex) {
        String msg = parseNotFoundMessage(ex.getMessage());
        log.error("EntityNotFoundException: {}", msg);
        return createResponseEntity(request, ErrorAttributeOptions.of(MESSAGE), msg, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(DataConflictException.class)
    public ResponseEntity<?> dataConflictException(WebRequest request, DataConflictException ex) {
        log.error("DataConflictException: {}", ex.getMessage());
        return createResponseEntity(request, ErrorAttributeOptions.of(MESSAGE), null, HttpStatus.CONFLICT);
    }

    @NonNull
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
            @NonNull Exception ex, Object body, @NonNull HttpHeaders headers, @NonNull HttpStatus status, @NonNull WebRequest request) {
        log.error("Exception", ex);
        super.handleExceptionInternal(ex, body, headers, status, request);
        return createResponseEntity(request, ErrorAttributeOptions.of(), ValidationUtil.getRootCause(ex).getMessage(), status);
    }

    @NonNull
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, @NonNull HttpHeaders headers, @NonNull HttpStatus status, @NonNull WebRequest request) {
        return handleBindingErrors(ex.getBindingResult(), request);
    }

    @NonNull
    @Override
    protected ResponseEntity<Object> handleBindException(
            BindException ex, @NonNull HttpHeaders headers, @NonNull HttpStatus status, @NonNull WebRequest request) {
        return handleBindingErrors(ex.getBindingResult(), request);
    }

    private ResponseEntity<Object> handleBindingErrors(BindingResult result, WebRequest request) {
        String msg = result.getFieldErrors().stream()
                .map(fe -> String.format("[%s] %s", fe.getField(), fe.getDefaultMessage()))
                .collect(Collectors.joining("\n"));
        return createResponseEntity(request, ErrorAttributeOptions.defaults(), msg, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @SuppressWarnings("unchecked")
    private <T> ResponseEntity<T> createResponseEntity(WebRequest request, ErrorAttributeOptions options, String msg, HttpStatus status) {
        Map<String, Object> body = errorAttributes.getErrorAttributes(request, options);
        if (msg != null) {
            body.put("message", msg);
        }
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        return (ResponseEntity<T>) ResponseEntity.status(status).body(body);
    }
}
