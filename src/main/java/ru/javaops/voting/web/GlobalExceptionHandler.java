package ru.javaops.voting.web;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.javaops.voting.error.AppException;
import ru.javaops.voting.util.Util;
import ru.javaops.voting.util.ValidationUtil;

import javax.persistence.EntityNotFoundException;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
@AllArgsConstructor
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    public static final String EXCEPTION_DUPLICATE_MENU = "Menu for the restaurant already exists on the specified date";
    public static final String EXCEPTION_DUPLICATE_RESTAURANT = "Restaurant with the same name and address already exists";
    public static final String EXCEPTION_DUPLICATE_DISH = "Dish with the same name and price already exists";
    public static final String EXCEPTION_DUPLICATE_VOTE = "Your vote for today already exists, you can try to change it";

    private static final Map<String, String> CONSTRAINTS_MAP = Map.of(
            "ux_menu_lunch_date_restaurant", EXCEPTION_DUPLICATE_MENU,
            "ux_restaurant_name_address", EXCEPTION_DUPLICATE_RESTAURANT,
            "ux_dish_name_price", EXCEPTION_DUPLICATE_DISH,
            "ux_vote_user_id_lunch_date", EXCEPTION_DUPLICATE_VOTE);

    private final ErrorAttributes errorAttributes;

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> handleNotFound(WebRequest request, EntityNotFoundException ex) {
        String msg = Util.getFormattedMessage(HttpStatus.NOT_FOUND, ex.getMessage());
        log.warn("EntityNotFoundException: {}", msg);
        return createResponseEntity(getDefaultBody(request, msg), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> handleDataIntegrityViolation(WebRequest request, DataIntegrityViolationException ex) {
        String rootMsg = ValidationUtil.getRootCause(ex).getMessage();
        if (rootMsg != null) {
            String lowerCaseMsg = rootMsg.toLowerCase();
            for (Map.Entry<String, String> entry : CONSTRAINTS_MAP.entrySet()) {
                if (lowerCaseMsg.contains(entry.getKey())) {
                    int existingId = Util.getIdFromConstraintMessage(lowerCaseMsg);
                    String msg = Util.getFormattedMessage(HttpStatus.CONFLICT, entry.getValue() + " (existing id=" + existingId + ")");
                    log.warn("DataIntegrityViolationException: {}", msg);
                    return createResponseEntity(getDefaultBody(request, msg), HttpStatus.CONFLICT);
                }
            }
        }
        log.error("DataIntegrityViolationException ", ex);
        return createResponseEntity(getDefaultBody(request, ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AppException.class)
    public ResponseEntity<?> handleAppException(WebRequest request, AppException ex) {
        String message = ex.getMessage();
        log.warn("AppException: {}", message);
        return createResponseEntity(getDefaultBody(request, message), ex.getStatus());
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return handleBindingErrors(ex.getBindingResult(), request);
    }

    @Override
    protected ResponseEntity<Object> handleBindException(
            BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return handleBindingErrors(ex.getBindingResult(), request);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
            Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.error("Exception", ex);
        super.handleExceptionInternal(ex, body, headers, status, request);
        String msg = ValidationUtil.getRootCause(ex).getMessage();
        return createResponseEntity(getDefaultBody(request, Util.getFormattedMessage(status, msg)), status);
    }

    private ResponseEntity<Object> handleBindingErrors(BindingResult result, WebRequest request) {
        String msg = result.getFieldErrors().stream()
                .map(fe -> String.format("[%s] %s", fe.getField(), fe.getDefaultMessage()))
                .collect(Collectors.joining("\n"));
        HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
        return createResponseEntity(getDefaultBody(request, Util.getFormattedMessage(status, msg)), status);
    }

    private Map<String, Object> getDefaultBody(WebRequest request, String msg) {
        Map<String, Object> body = errorAttributes.getErrorAttributes(request, ErrorAttributeOptions.defaults());
        if (msg != null) {
            body.put("message", msg);
        }
        return body;
    }

    @SuppressWarnings("unchecked")
    private <T> ResponseEntity<T> createResponseEntity(Map<String, Object> body, HttpStatus status) {
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        return (ResponseEntity<T>) ResponseEntity.status(status).body(body);
    }
}
