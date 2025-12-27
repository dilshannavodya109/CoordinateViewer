package com.coordinate.viewer.util;

import com.coordinate.viewer.model.exception.ExceededRetryException;

import java.time.Duration;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class RetryExecutor {
    private static final Logger LOGGER = LoggerFactory.getLogger(RetryExecutor.class);

    private RetryExecutor() {
    }

    public static <T> T executeWithRetry(
            Supplier<T> operation,
            int maxAttempts,
            Duration baseDelay,
            Predicate<RuntimeException> retryCondition
    ) throws ExceededRetryException {

        RuntimeException currentError = null;

        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                return operation.get();
            } catch (RuntimeException error) {
                currentError = error;
                boolean canRetry = attempt < maxAttempts && retryCondition.test(error);
                if (!canRetry) {
                    throw new ExceededRetryException(String.format("Non-retrievable error occurred on attempt %d : %s", attempt, error.getMessage()), error);
                }
                Duration delay = calculateDelay(baseDelay, attempt);
                logRetryAttempt(attempt, delay, error);
                sleep(delay);
            }
        }
        throw new ExceededRetryException(String.format("Operation failed after %d attempts.", maxAttempts), currentError);
    }

    private static Duration calculateDelay(Duration baseDelay, int attempt) {
        long delayMillis = (long) (baseDelay.toMillis() * Math.pow(2, attempt - 1));
        return Duration.ofMillis(delayMillis);
    }

    private static void sleep(Duration delay) {
        try {
            Thread.sleep(delay.toMillis());
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
    }

    private static void logRetryAttempt(int attempt, Duration delay, Throwable error) {
        LOGGER.log(Level.WARNING,
                "Attempt {0} failed: {1} - retrying in {2} ms",
                new Object[]{attempt, error.getClass().getSimpleName(), delay.toMillis()});
    }
}
