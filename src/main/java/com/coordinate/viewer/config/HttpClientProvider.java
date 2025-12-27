package com.coordinate.viewer.config;

import java.net.http.HttpClient;
import java.time.Duration;

public final class HttpClientProvider {
    private static final HttpClient CLIENT = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5))
            .followRedirects(HttpClient.Redirect.NORMAL)
            .version(HttpClient.Version.HTTP_2)
            .build();

    private HttpClientProvider() {
    }

    public static HttpClient getClient() {
        return CLIENT;
    }
}
