package com.coordinate.viewer.view;

import com.coordinate.viewer.view.dialog.ExitConfirmationDialog;
import com.coordinate.viewer.config.AppConfig;
import com.coordinate.viewer.controller.AutoReloadController;
import com.coordinate.viewer.controller.CoordinateController;
import com.coordinate.viewer.view.frame.CoordinateViewerFrame;
import com.coordinate.viewer.view.panel.MapPanel;
import com.coordinate.viewer.view.panel.StatusBarPanel;
import com.coordinate.viewer.view.panel.ToolbarPanel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class CoordinateViewerFrameTest {

    private AutoReloadController mockAutoReloadController;
    private ExitConfirmationDialog mockExitDialog;
    private ToolbarPanel mockToolbar;
    private StatusBarPanel mockStatusBar;
    private CoordinateViewerFrame frame;

    @BeforeEach
    void setUp() {
        AppConfig mockConfig = mock(AppConfig.class);
        CoordinateController mockCoordinateController = mock(CoordinateController.class);
        mockAutoReloadController = mock(AutoReloadController.class);
        mockExitDialog = mock(ExitConfirmationDialog.class);
        mockToolbar = mock(ToolbarPanel.class);
        mockStatusBar = mock(StatusBarPanel.class);
        MapPanel mockMapPanel = mock(MapPanel.class);

        when(mockConfig.getAppName()).thenReturn("Coordinate Viewer");
        when(mockConfig.getAutoReloadInterval()).thenReturn(5000);

        frame = new CoordinateViewerFrame(mockConfig, mockCoordinateController);

        injectPrivateField(frame, "coordinateController", mockCoordinateController);
        injectPrivateField(frame, "autoReloadController", mockAutoReloadController);
        injectPrivateField(frame, "exitConfirmationDialog", mockExitDialog);
        injectPrivateField(frame, "toolbarPanel", mockToolbar);
        injectPrivateField(frame, "statusBar", mockStatusBar);
        injectPrivateField(frame, "mapPanel", mockMapPanel);
    }

    @AfterEach
    void tearDown() {
        frame.dispose();
    }

    private void injectPrivateField(Object target, String fieldName, Object value) {
        try {
            var field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException(String.format("Failed to inject field: %s", fieldName), e);
        }
    }

    @Test
    @DisplayName("Enabling auto reload updates controllers and status bar")
    void onToggleAutoReload_enabled_updatesControllersAndStatus() {
        frame.onToggleAutoReload(true);

        verify(mockAutoReloadController).enable();
        verify(mockToolbar).setAutoReloadState(true);
        verify(mockStatusBar).updateStatus(contains("Auto reload enabled"));
    }

    @Test
    @DisplayName("Disabling auto reload updates controllers and status bar")
    void onToggleAutoReload_disabled_updatesControllersAndStatus() {
        frame.onToggleAutoReload(false);

        verify(mockAutoReloadController).disable();
        verify(mockToolbar).setAutoReloadState(false);
        verify(mockStatusBar).updateStatus("Auto reload disabled");
    }

    @Test
    @DisplayName("Reload action triggers coordinate loading sequence")
    void onReload_triggersCoordinateLoadingBehavior() {
        CoordinateViewerFrame spyFrame = spy(frame);
        injectPrivateField(spyFrame, "toolbarPanel", mockToolbar);
        injectPrivateField(spyFrame, "statusBar", mockStatusBar);

        spyFrame.onReload();

        verify(mockStatusBar).updateStatus("Status: Fetching data...");
        verify(mockToolbar).setReloadEnabled(false);
    }

    @Test
    @DisplayName("Exit canceled by user does not disable controllers or close frame")
    void onExit_whenUserCancels_doesNothing() {
        when(mockExitDialog.confirmExit(any())).thenReturn(false);
        frame.onExit();

        verify(mockExitDialog).confirmExit(frame);
        verify(mockAutoReloadController, never()).disable();
        verify(mockToolbar, never()).setAutoReloadState(anyBoolean());
    }

    @Test
    @DisplayName("Frame is initialized with correct layout and visibility")
    void frameInitialization_addsExpectedPanels() {
        assertEquals("Coordinate Viewer", frame.getTitle(), "Title should match app name");
        assertTrue(frame.getLayout() instanceof BorderLayout, "Expected BorderLayout");
        assertTrue(frame.isVisible(), "Frame should be visible after initialization");
    }
}