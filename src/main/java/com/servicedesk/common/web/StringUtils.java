package com.servicedesk.common.web;

public class StringUtils {
    public static boolean hasText(String s) {
        return s != null && !s.trim()
                .isEmpty();
    }
}
