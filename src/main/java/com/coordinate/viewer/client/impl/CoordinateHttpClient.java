package com.coordinate.viewer.client.impl;

import com.coordinate.viewer.client.CoordinateClient;
import com.coordinate.viewer.config.AppConfig;
import com.coordinate.viewer.model.exception.CoordinateFetchException;
import com.coordinate.viewer.util.HttpResponseHandler;
import com.coordinate.viewer.util.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CoordinateHttpClient implements CoordinateClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(CoordinateHttpClient.class);
    private final HttpClient httpClient;
    private final AppConfig config;

    public CoordinateHttpClient(AppConfig config, HttpClient httpClient) {
        this.config = config;
        this.httpClient = httpClient;
    }

    public String fetchCoordinates() {
        LOGGER.log(Level.INFO, "Fetching coordinate data from remote server");

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(config.getDataSourceUrl()))
                    .timeout(Duration.ofSeconds(config.getTimeoutIntervalSeconds()))
                    .header("Accept", "text/plain")
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(
                    request,
                    HttpResponse.BodyHandlers.ofString(StandardCharsets.ISO_8859_1)
            );

            HttpResponseHandler.validate(response);
            return response.body();

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new CoordinateFetchException("Thread interrupted during HTTP call", e);

        } catch (IOException e) {
            throw new CoordinateFetchException("I/O error during HTTP call", e);
        }
    }
}
