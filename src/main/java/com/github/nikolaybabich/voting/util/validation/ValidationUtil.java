package com.github.nikolaybabich.voting.util.validation;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ValidationUtil {

    public static void checkModification(int count, int id) {
        if (count == 0) {
            throw new RuntimeException("Entity with id=" + id + " not found"); // TODO change exception type
        }
    }
}
