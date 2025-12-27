package com.coordinate.viewer.util;

import java.util.logging.Level;
import java.util.logging.Logger;

public final class LoggerFactory {
    private LoggerFactory() {
    }

    public static Logger getLogger(Class<?> clazz) {
        Logger logger = Logger.getLogger(clazz.getName());
        logger.setLevel(Level.ALL);
        return logger;
    }
}
