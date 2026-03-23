package com.mathsmate.utils;

public class MathParser {

    /**
     * Normalises unicode math symbols and shorthand to ASCII equivalents.
     */
    public static String normalize(String input) {
        if (input == null) return "";
        return input.trim()
                .replace("\u00B2", "^2")   // ²
                .replace("\u00B3", "^3")   // ³
                .replace("\u00D7", "*")    // ×
                .replace("\u00F7", "/")    // ÷
                .replace("\u2212", "-")    // −
                .replace("\u00B1", "+-")   // ±
                .replace("\u2260", "!=")   // ≠
                .replace("\u2264", "<=")   // ≤
                .replace("\u2265", ">=");  // ≥
    }

    /** Format a double: show integer when there is no fractional part. */
    public static String formatNumber(double d) {
        if (d == Math.floor(d) && !Double.isInfinite(d)) {
            return String.valueOf((long) d);
        }
        // round to 6 significant figures
        return String.format("%.6g", d).replaceAll("0+$", "").replaceAll("\\.$", "");
    }

    /** Format a double for display in step descriptions. */
    public static String fmt(double d) {
        return formatNumber(d);
    }

    public static boolean isNumeric(String s) {
        if (s == null || s.isBlank()) return false;
        try {
            Double.parseDouble(s.trim().replace(",", ""));
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
