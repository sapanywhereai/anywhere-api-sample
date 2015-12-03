package com.sap.integration.utils;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

/**
 * Utility class used for working with dates.
 */
public class DateUtil {

    /**
     * Method which returns time period as string between start date and end date.
     * 
     * @param start - start date
     * @param end - end date
     * @return string representation of duration between two times
     */
    public static final String getDurationTime(DateTime start, DateTime end) {
        Period durationTime = new Period(start, end);
        PeriodFormatter periodFormatter = new PeriodFormatterBuilder()
                .printZeroAlways()
                .minimumPrintedDigits(2)
                .appendHours().appendLiteral(":")
                .appendMinutes().appendLiteral(":")
                .appendSeconds().appendLiteral(".")
                .appendMillis3Digit()
                .toFormatter();

        return periodFormatter.print(durationTime);
    }

    /**
     * Converting Date to String.
     * 
     * @param dateTime Date to convert
     * @return String
     */
    public static final String convertDateTimeToString(DateTime dateTime) {
        return dateTime.toDateTime(DateTimeZone.UTC).toString();
    }
}
