package ru.javaops.voting.util;

import lombok.experimental.UtilityClass;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.lang.NonNull;
import ru.javaops.voting.error.IllegalRequestDataException;
import ru.javaops.voting.error.UpdateRestrictionException;
import ru.javaops.voting.error.UpdateRestrictionException.RestrictionType;
import ru.javaops.voting.model.BaseEntity;
import ru.javaops.voting.model.Menu;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

@UtilityClass
public final class ValidationUtil {

    public static void checkNew(BaseEntity bean) {
        if (!bean.isNew()) {
            throw new IllegalRequestDataException(bean.getClass().getSimpleName() + " must be new (id=null)");
        }
    }

    @SuppressWarnings("ConstantConditions")
    public static void assureIdConsistent(BaseEntity bean, int id) {
        if (bean.isNew()) {
            bean.setId(id);
        } else if (bean.getId() != id) {
            throw new IllegalRequestDataException(bean.getClass().getSimpleName() + " must be with id=" + id);
        }
    }

    // https://stackoverflow.com/a/65442410
    @NonNull
    public static Throwable getRootCause(@NonNull Throwable t) {
        Throwable rootCause = NestedExceptionUtils.getRootCause(t);
        return rootCause != null ? rootCause : t;
    }

    public static void checkMenuUpdateRestriction(Menu menu) {
        if (menu.getLunchDate().isBefore(LocalDate.now())) {
            throw new UpdateRestrictionException(RestrictionType.MENU_DATE_RESTRICTION);
        }
    }

    public static <T> T checkNotFound(Optional<T> optionalEntity, Class<?> clazz, Integer... ids) {
        if (optionalEntity.isEmpty()) {
            if (ids.length == 0) {
                throw new IllegalArgumentException("Must be at least one id argument");
            }
            String message = "Not found " + clazz.getSimpleName() + " id=" + ids[0];
            message += (ids.length > 1) ? " for related entity id=" + ids[1] : "";
            throw new EntityNotFoundException(message);
        }
        return optionalEntity.get();
    }

    public static boolean isValidId(Integer... ids) {
        return Arrays.stream(ids).allMatch(number -> number != null && number > 0);
    }
}
