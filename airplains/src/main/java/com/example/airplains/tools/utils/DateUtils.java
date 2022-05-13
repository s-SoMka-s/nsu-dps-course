package com.example.airplains.tools.utils;

import java.sql.Date;
import java.time.OffsetDateTime;

public class DateUtils {

    public static OffsetDateTime addDays(OffsetDateTime date, int days) {
        return date.plusDays(days);
    }
}
