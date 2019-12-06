package com.xebia.fs101.writerpad.utils;

        import java.util.UUID;

public abstract class StringUtils {
    private StringUtils() {

    }

    public static String slugify(String input) {

        if (input == null) {
            throw new IllegalArgumentException("input can't be null");
        }
        input = input.trim().toLowerCase()
                .replaceAll("\\s\\s+", " ");
        return input.replaceAll("\\s", "-");
    }

    public static UUID extractUuid(String input) {

        if (input.length() > 36) {
            return UUID.fromString(input.substring(input.length() - 36));
        } else {
            throw new IllegalArgumentException("uuid is not valid");
        }
    }
}
