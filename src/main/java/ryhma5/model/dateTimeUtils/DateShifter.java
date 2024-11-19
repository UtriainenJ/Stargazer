package ryhma5.model.dateTimeUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class DateShifter {
    public static String shiftDateByMonths(String date, int monthsToShift) {

        LocalDate originalDate = LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE);
        LocalDate newDate = originalDate.plusMonths(monthsToShift);

        return newDate.format(DateTimeFormatter.ISO_LOCAL_DATE);
    }
}
