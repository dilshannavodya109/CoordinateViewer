package com.coordinate.viewer.util;

import java.io.IOException;
import java.net.http.HttpResponse;

public final class HttpResponseHandler {
    private HttpResponseHandler() {
    }

    public static void validate(HttpResponse<?> response) throws IOException {
        int status = response.statusCode();

        if (status >= 200 && status < 300) {
            return;
        }
        if (status >= 500) {
            throw new IOException(String.format("Server error (status %s )", status));
        }
        if (status >= 400) {
            throw new IllegalStateException(String.format("Client error (status %s )", status));
        }
        throw new IOException(String.format("Unexpected HTTP status: %s", status));
    }
}
