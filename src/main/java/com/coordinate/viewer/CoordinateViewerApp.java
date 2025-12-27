package com.coordinate.viewer;

import com.coordinate.viewer.client.CoordinateClient;
import com.coordinate.viewer.client.impl.CoordinateHttpClient;
import com.coordinate.viewer.config.AppConfig;
import com.coordinate.viewer.config.HttpClientProvider;
import com.coordinate.viewer.controller.CoordinateController;
import com.coordinate.viewer.controller.impl.CoordinateControllerImpl;
import com.coordinate.viewer.service.CoordinateService;
import com.coordinate.viewer.service.impl.CoordinateServiceImpl;
import com.coordinate.viewer.util.LoggerFactory;
import com.coordinate.viewer.view.frame.CoordinateViewerFrame;

import javax.swing.*;
import java.net.http.HttpClient;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CoordinateViewerApp {
    private static final Logger LOGGER = LoggerFactory.getLogger(CoordinateViewerApp.class);

    public static void main(String[] args) {
        AppConfig config = new AppConfig();
        HttpClient client = HttpClientProvider.getClient();
        CoordinateClient coordinateClient = new CoordinateHttpClient(config, client);
        CoordinateService service = new CoordinateServiceImpl(config, coordinateClient);
        CoordinateController coordinateController = new CoordinateControllerImpl(service);

        LOGGER.log(Level.INFO, "Coordinate Viewer Application started.");

        SwingUtilities.invokeLater(() ->
                new CoordinateViewerFrame(config, coordinateController)
        );
    }
}
