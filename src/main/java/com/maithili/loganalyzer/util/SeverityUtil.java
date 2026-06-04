package com.maithili.loganalyzer.util;

/**
 * Utility class to map return codes to severity levels
 */
public class SeverityUtil {

    public static String getSeverity(String code) {

        switch (code) {
            case "45":
                return "LOW";
            case "99":
                return "HIGH";
            case "111":
                return "CRITICAL";
            default:
                return "UNKNOWN";
        }
    }
}