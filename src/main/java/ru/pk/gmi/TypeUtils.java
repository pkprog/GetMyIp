package ru.pk.gmi;

public class TypeUtils {
    public static boolean isEmpty(String text) {
        return text == null || text.equals("") || text.trim().equals("");
    }
    public static boolean notEmpty(String text) {
        return !isEmpty(text);
    }


    public static boolean isTrue(String text) {
        return "true".equalsIgnoreCase(text);
    }

}
