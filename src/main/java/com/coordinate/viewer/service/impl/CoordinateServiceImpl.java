  package com.coordinate.viewer.service.impl;

import com.coordinate.viewer.client.CoordinateClient;
import com.coordinate.viewer.config.AppConfig;
import com.coordinate.viewer.model.Coordinate;
import com.coordinate.viewer.model.exception.CoordinateFetchException;
import com.coordinate.viewer.model.exception.ExceededRetryException;
import com.coordinate.viewer.service.CoordinateService;
import com.coordinate.viewer.util.CoordinateValidator;
import com.coordinate.viewer.util.LoggerFactory;
import com.coordinate.viewer.util.RetryExecutor;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CoordinateServiceImpl implements CoordinateService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CoordinateServiceImpl.class);
    private final AppConfig config;
    private final CoordinateClient coordinateHttpClient;
    public CoordinateServiceImpl(final AppConfig config, final CoordinateClient coordinateHttpClient) {
        this.config = config;
        this.coordinateHttpClient = coordinateHttpClient;
    }

    public List<Coordinate> fetchCoordinates() {
        try {
            String response = RetryExecutor.executeWithRetry(
                    coordinateHttpClient::fetchCoordinates,
                    config.getRetryMaxAttempts(),
                    Duration.ofMillis(config.getRetryBaseDelayMillis()),
                    t -> t.getCause() instanceof IOException
            );
            return parseCoordinates(response);

        } catch (ExceededRetryException e) {
            LOGGER.log(Level.WARNING, "Exceeded max retry attempts while fetching coordinates", e);
            return Collections.emptyList();
        } catch (CoordinateFetchException e) {
            LOGGER.log(Level.WARNING, "Failed to fetch coordinates", e);
            return Collections.emptyList();
        }
    }

    private List<Coordinate> parseCoordinates(String body) {

        List<Coordinate> coordinates = new ArrayList<>();
        String[] lines = body.split("\\R");

        for (String line : lines) {
            if (!CoordinateValidator.isValidLine(line)) {
                continue;
            }

            Coordinate coordinate = mapToCoordinate(line);
            if (coordinate != null) {
                coordinates.add(coordinate);
            }
        }
        return coordinates;
    }

    private Coordinate mapToCoordinate(String line) {
        try {
            String[] parts = line.split(",");
            if (parts.length < 2) return null;

            double xPosition = Double.parseDouble(parts[0].trim());
            double yPosition = Double.parseDouble(parts[1].trim());
            String name = (parts.length >= 3 && !parts[2].trim().isEmpty())
                    ? parts[2].trim()
                    : "Not Define";

            return new Coordinate(xPosition, yPosition, name);

        } catch (NumberFormatException e) {
            LOGGER.log(Level.WARNING, "Skipping invalid coordinate: {0}", line);
            return null;
        }
    }
}
