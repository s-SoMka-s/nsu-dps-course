package com.example.airplains.tools.utils;

import java.sql.Date;
import java.util.Calendar;

public class DateUtils {

    public static Date addDays(Date date, int days) {
        var calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, days);

        return new Date(calendar.getTimeInMillis());
    }
}
