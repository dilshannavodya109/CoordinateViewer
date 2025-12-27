package com.coordinate.viewer.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class ConfigLoader {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigLoader.class);
    private static final Properties PROPERTIES = new Properties();
    private static final List<String> PROPERTY_FILES = List.of(
            "application.properties",
            "app-info.properties"
    );

    static {
        for (String fileName : PROPERTY_FILES) {
            try (InputStream input = ConfigLoader.class.getClassLoader().getResourceAsStream(fileName)) {
                if (input == null) {
                    LOGGER.log(Level.WARNING, "Warning: {0} not found in classpath", fileName);
                    continue;
                }
                PROPERTIES.load(input);
                LOGGER.log(Level.CONFIG, "Loaded all the application configurations successfully");

            } catch (IOException e) {
                LOGGER.log(Level.WARNING, "Failed to load {0} : {1}", new Object[]{fileName, e.getMessage()});
            }
        }
    }

    private ConfigLoader() {
        throw new UnsupportedOperationException("ConfigLoader utility class cannot be instantiated");
    }

    public static String get(String key) {
        return PROPERTIES.getProperty(key);
    }

    public static int getInt(String key, int defaultValue) {
        try {
            return Integer.parseInt(PROPERTIES.getProperty(key));
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static long getLong(String key, long defaultValue) {
        try {
            return Long.parseLong(PROPERTIES.getProperty(key));
        } catch (Exception e) {
            return defaultValue;
        }
    }
}
