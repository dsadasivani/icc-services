package com.nme.core.util;

import java.util.Optional;

public class Utility {
    public static String handleNull(String input, String defaultValue) {
        return Optional.ofNullable(input).orElse(defaultValue);
    }

    public static String handleNull(String input) {
        return Optional.ofNullable(input).orElse("");
    }
}
