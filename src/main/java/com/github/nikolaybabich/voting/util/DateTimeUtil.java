package com.github.nikolaybabich.voting.util;

import com.github.nikolaybabich.voting.error.IllegalRequestDataException;
import lombok.experimental.UtilityClass;
import org.springframework.lang.NonNull;

import java.time.LocalDate;
import java.time.LocalTime;

@UtilityClass
public final class DateTimeUtil {

    // DB doesn't support LocalDate.MIN/MAX
    private static final LocalDate MIN_DATE = LocalDate.of(1, 1, 1);
    private static final LocalDate MAX_DATE = LocalDate.of(3000, 1, 1);

    public static LocalDate getOrMin(LocalDate localDate) {
        return localDate != null ? localDate : MIN_DATE;
    }

    public static LocalDate getOrMax(LocalDate localDate) {
        return localDate != null ? localDate : MAX_DATE;
    }

    public static void checkDates(@NonNull LocalDate fromDate, @NonNull LocalDate toDate) {
        if (toDate.isBefore(fromDate)) {
            throw new IllegalRequestDataException("toDate [%s] must not be earlier than fromDate [%s]".formatted(toDate, fromDate));
        }
    }

    public static LocalTime getCurrentTime() {
        return LocalTime.now();
    }
}
