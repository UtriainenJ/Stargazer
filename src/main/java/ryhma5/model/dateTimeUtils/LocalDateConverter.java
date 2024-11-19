package ryhma5.model.dateTimeUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateConverter {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;

    public static String toString(LocalDate date) {
        if(date == null) return null;
        return date.format(formatter);
    }

    public static LocalDate fromString(String dateString) {
        if(dateString == null) return null;
        return LocalDate.parse(dateString, formatter);
    }
}
