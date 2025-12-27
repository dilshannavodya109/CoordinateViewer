package com.coordinate.viewer.util;

public final class CoordinateValidator {
    private CoordinateValidator() {
    }

    public static boolean isValidLine(String line) {
        if (line == null) return false;
        line = line.trim();
        return !line.isEmpty() && !line.startsWith("#");
    }
}
