package org.myshelf.utils;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Utility class for manipulation of strings.
 *
 * @author moblaa
 * @since 2017/02/22
 */
public final class StringUtils {

    private StringUtils() {
    }

    /**
     * Concatenates `toRepeat` by `numberOfRepeats` times with itself.
     * In other words: Raises `toRepeat` to the power of `numberOfRepeats`.
     *
     * @param toRepeat String to be repeated.
     * @param numberOfRepeats Number of concatenations.
     * @return (toRepeat)^numberOfRepeats
     */
    public static String repeat(String toRepeat, int numberOfRepeats) {
        if (toRepeat == null || numberOfRepeats < 0)
            throw new IllegalArgumentException("Argument null or negative");
        return new String(new char[numberOfRepeats]).replace("\0",toRepeat);
    }

    /**
     * Converts a map to a Property-File like String.
     */
    public static String toPropertiesString(Map<?, ?> map) {
        //Add all to Properties
        //toString
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            builder.append(entry.getKey()).append("=").append(entry.getValue()).append(System.lineSeparator());
        }
        return builder.toString();
    }

    /**
     * Serializes a class to a properties file like String representation.
     * @param item
     * @return
     */
    public static String toPropertiesString(Object item) {
        Map<String, Object> map = new LinkedHashMap<>();

        for (Field field : item.getClass().getFields()) {
            field.setAccessible(true);
            try {
                map.put(field.getName(), field.get(item));
            } catch (IllegalAccessException ignored) {
                ignored.printStackTrace();
            }
        }

        return toPropertiesString(map);
    }
}
