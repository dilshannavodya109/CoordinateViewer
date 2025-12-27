package com.coordinate.viewer;

import com.coordinate.viewer.client.CoordinateClient;
import com.coordinate.viewer.client.impl.CoordinateHttpClient;
import com.coordinate.viewer.config.AppConfig;
import com.coordinate.viewer.config.HttpClientProvider;
import com.coordinate.viewer.controller.CoordinateController;
import com.coordinate.viewer.controller.impl.CoordinateControllerImpl;
import com.coordinate.viewer.service.CoordinateService;
import com.coordinate.viewer.service.impl.CoordinateServiceImpl;
import com.coordinate.viewer.view.frame.CoordinateViewerFrame;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.net.http.HttpClient;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;

public class CoordinateViewerAppTest {

    @Test
    @DisplayName("main() should start the application without throwing exceptions")
    void main_startsApplicationSuccessfully() {

        try (var swingMock = mockStatic(SwingUtilities.class)) {
            CoordinateViewerApp.main(new String[]{});

            swingMock.verify(() -> SwingUtilities.invokeLater(any(Runnable.class)));

        }
    }

    @Test
    @DisplayName("Runnable passed to invokeLater() should create CoordinateViewerFrame successfully")
    void invokeLater_createsMainFrame() {
        AppConfig config = new AppConfig();
        HttpClient client = HttpClientProvider.getClient();
        CoordinateClient coordinateClient = new CoordinateHttpClient(config, client);
        CoordinateService service = new CoordinateServiceImpl(config, coordinateClient);
        CoordinateController coordinateController = new CoordinateControllerImpl(service);

        SwingUtilities.invokeLater(() -> {
            CoordinateViewerFrame frame = new CoordinateViewerFrame(config, coordinateController);
            assertNotNull(frame, "Frame should be created successfully");
            assertTrue(frame.isVisible(), "Frame should be visible after initialization");
            frame.dispose();
        });
    }
}
