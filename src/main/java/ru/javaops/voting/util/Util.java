package ru.javaops.voting.util;

import lombok.experimental.UtilityClass;
import org.springframework.http.HttpStatus;

@UtilityClass
public final class Util {
    private static final String SEARCH_STRING = " values ";

    public static int getIdFromConstraintMessage(String message) {
        int beginIndex = message.indexOf(SEARCH_STRING) + SEARCH_STRING.length();
        int endIndex = message.indexOf("\"", beginIndex);
        return Integer.parseInt(message.substring(beginIndex, endIndex));
    }

    public static String getFormattedMessage(HttpStatus status, String message) {
        return String.format("%s \"%s\"", status, message);
    }
}
