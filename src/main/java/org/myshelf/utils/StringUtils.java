package org.myshelf.utils;

import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
     * Escapes all characters used in the {@link java.util.regex.Pattern} class for regular expressions.
     * Won't escape characters that are already escaped. but will escape characters with even number
     * of escape characters before that.
     *
     * @param value String literal to escape the not escaped characters in.
     * @return Escaped version of input string.
     */
    public static String escapePatternChars(@NotNull String value) {
        if (value.length() == 0)
            return value;

        String result = value;
        StringBuilder builder = new StringBuilder();
        Pattern[] patterns = new Pattern[]{Pattern.compile("\\\\"), Pattern.compile("\\{")};
        for (Pattern pattern : patterns) {
            Matcher matcher = pattern.matcher(result);
            int index = 0;
            while (matcher.find()) {
                int start = matcher.start() - 1;
                int end = matcher.end();
                builder.append(value, index, start);

                if (!builder.toString().matches(".*(\\\\\\\\)+")) {
                    //not even number of escapes before match => already escaped
                    builder.append(result, start + 1, end);
                } else {
                    //Even number of escapes before match => not escaped
                    builder.append(pattern.pattern());
                }
                index = end;
            }
            result = builder.toString();
            builder = new StringBuilder();
        }

        //Remove { with \\{
        //Has to escape as in java 8 regex will be used
        return result;
    }
}
