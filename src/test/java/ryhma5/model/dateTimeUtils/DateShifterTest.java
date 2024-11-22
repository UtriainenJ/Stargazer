package ryhma5.model.dateTimeUtils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DateShifterTest {

    @Test
    void shiftDateByMonths() {
        assertEquals("2021-06-01", DateShifter.shiftDateByMonths("2021-05-01", 1));
        assertEquals("2021-07-01", DateShifter.shiftDateByMonths("2021-05-01", 2));
        assertEquals("2021-05-01", DateShifter.shiftDateByMonths("2021-05-01", 0));
        assertEquals("2021-04-01", DateShifter.shiftDateByMonths("2021-05-01", -1));
        assertEquals("2020-05-01", DateShifter.shiftDateByMonths("2021-05-01", -12));
    }
}