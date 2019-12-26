package ru.pk.gmi.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class TypeUtils {
    public static boolean isEmpty(String text) {
        return text == null || text.equals("") || text.trim().equals("");
    }
    public static boolean notEmpty(String text) {
        return !isEmpty(text);
    }

    public static String safeString(String text) {
        return isEmpty(text) ? "" : text.trim();
    }

    public static boolean isTrue(String text) {
        return "true".equalsIgnoreCase(text);
    }

    public static int compareStrings(String s1, String s2, boolean ignoreCase) {
        if (s1 == null && s2 == null) return 0;
        if (s1 == null) return -1;
        if (s2 == null) return 1;
        return ignoreCase ? s1.compareToIgnoreCase(s2) : s1.compareTo(s2);
    }

    /**
     * Сравнить даты без учета времени
     */
    public static int compareDay(Date d1, Date d2) {
        Calendar c1 = GregorianCalendar.getInstance();
        c1.setTime(d1);
        c1.set(Calendar.HOUR_OF_DAY, 0);
        c1.set(Calendar.MINUTE, 0);
        c1.set(Calendar.SECOND, 0);
        c1.set(Calendar.MILLISECOND, 0);

        Calendar c2 = GregorianCalendar.getInstance();
        c2.setTime(d2);
        c2.set(Calendar.HOUR_OF_DAY, 0);
        c2.set(Calendar.MINUTE, 0);
        c2.set(Calendar.SECOND, 0);
        c2.set(Calendar.MILLISECOND, 0);

        return c1.compareTo(c2);
    }

}
