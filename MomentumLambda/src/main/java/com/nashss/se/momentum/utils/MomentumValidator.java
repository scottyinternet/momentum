package com.nashss.se.momentum.utils;

import org.apache.commons.lang3.StringUtils;
import java.util.regex.Pattern;

public final class MomentumValidator {
    private static final Pattern INVALID_CHARACTER_PATTERN = Pattern.compile("[\"'\\\\]");
    private MomentumValidator() {
    }

    public static boolean isValidString(String stringToValidate) {
        if (StringUtils.isBlank(stringToValidate)) {
            return false;
        } else {
            return !INVALID_CHARACTER_PATTERN.matcher(stringToValidate).find();
        }
    }
}
