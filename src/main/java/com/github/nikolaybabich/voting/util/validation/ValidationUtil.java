package com.github.nikolaybabich.voting.util.validation;

import com.github.nikolaybabich.voting.HasId;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ValidationUtil {

    public static void checkNew(HasId bean) {
        if (!bean.isNew()) {
            throw new RuntimeException(bean.getClass().getSimpleName() + " must be new (id=null)"); // TODO change exception type
        }
    }

    // Conservative when you reply, but accept liberally (http://stackoverflow.com/a/32728226)
    public static void assureIdConsistent(HasId bean, int id) {
        if (bean.isNew()) {
            bean.setId(id);
        } else if (bean.id() != id) {
            throw new RuntimeException(bean.getClass().getSimpleName() + " must has id=" + id); // TODO change exception type
        }
    }

    public static void checkModification(int count, int id) {
        if (count == 0) {
            throw new RuntimeException("Entity with id=" + id + " not found"); // TODO change exception type
        }
    }
}
