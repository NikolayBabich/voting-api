package com.github.nikolaybabich.voting.util;

import lombok.experimental.UtilityClass;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@UtilityClass
public final class ErrorUtil {
    private static final Pattern ID_PATTERN_FK_CONSTRAINT = Pattern.compile("\\((\\d+)\\)");
    private static final Pattern ID_PATTERN_UK_CONSTRAINT = Pattern.compile("values (\\d+)");
    private static final Pattern PATTERN_NOT_FOUND = Pattern.compile("\\.(\\w+) with id (\\d+)");

    public static String parseIdFromConstraintMessage(String message) {
        Matcher matcherForeignKeyConstraint = ID_PATTERN_FK_CONSTRAINT.matcher(message);
        Matcher matcherUniqueKeyConstraint = ID_PATTERN_UK_CONSTRAINT.matcher(message);

        if (matcherForeignKeyConstraint.find() && matcherForeignKeyConstraint.groupCount() >= 1) {
            return matcherForeignKeyConstraint.group(1);
        } else if (matcherUniqueKeyConstraint.find() && matcherUniqueKeyConstraint.groupCount() >= 1) {
            return matcherUniqueKeyConstraint.group(1);
        } else {
            throw new IllegalStateException("Couldn't parse error message: " + message);
        }
    }

    public static String parseNotFoundMessage(String message) {
        Matcher matcher = PATTERN_NOT_FOUND.matcher(message);
        if (matcher.find() && matcher.groupCount() >= 2) {
            return "%s with id=%s was not found".formatted(matcher.group(1), matcher.group(2));
        }
        throw new IllegalStateException("Couldn't parse error message: " + message);
    }
}
