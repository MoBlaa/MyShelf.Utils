package org.myshelf.utils;

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
}
