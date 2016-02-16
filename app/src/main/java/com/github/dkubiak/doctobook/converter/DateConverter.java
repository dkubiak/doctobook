package com.github.dkubiak.doctobook.converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by dawid.kubiak on 26/01/16.
 */
public class DateConverter {

    private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("dd-MM-yyyy");

    private static final SimpleDateFormat DATE_FORMATTER_DB = new SimpleDateFormat("yyyy-MM-dd");

    private static final SimpleDateFormat DATE_FORMATTER_YEAR_AND_MONTH_DB = new SimpleDateFormat("yyyy-MM");

    public static String toString(java.util.Date date) {
        return DATE_FORMATTER.format(date);
    }

    public static String toStringDB(java.util.Date date) {
        return DATE_FORMATTER_DB.format(date);
    }

    public static String toStringYearAndMonthDB(java.util.Date date) {
        return DATE_FORMATTER_YEAR_AND_MONTH_DB.format(date);
    }

    public static Date toDate(String date) throws ParseException {
        return DATE_FORMATTER.parse(date);
    }

    public static Date toDateFromDB(String date) throws ParseException {
        return DATE_FORMATTER_DB.parse(date);
    }
}
