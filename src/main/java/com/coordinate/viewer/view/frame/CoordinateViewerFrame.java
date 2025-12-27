package com.coordinate.viewer.view.frame;

import com.coordinate.viewer.controller.impl.AutoReloadControllerImpl;
import com.coordinate.viewer.view.dialog.AboutDialog;
import com.coordinate.viewer.view.dialog.ExitConfirmationDialog;
import com.coordinate.viewer.config.AppConfig;
import com.coordinate.viewer.controller.AutoReloadController;
import com.coordinate.viewer.controller.CoordinateController;
import com.coordinate.viewer.util.LoggerFactory;
import com.coordinate.viewer.view.panel.MapPanel;
import com.coordinate.viewer.view.panel.StatusBarPanel;
import com.coordinate.viewer.view.panel.ToolbarPanel;
import com.coordinate.viewer.view.panel.toolbar.ToolbarActions;
import com.coordinate.viewer.view.worker.CoordinateWorker;

import javax.swing.*;
import java.awt.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CoordinateViewerFrame extends JFrame implements ToolbarActions {

    private static final Logger LOGGER = LoggerFactory.getLogger(CoordinateViewerFrame.class);
    private final AppConfig appConfig;
    private final CoordinateController coordinateController;
    private final AutoReloadController autoReloadController;
    private final ExitConfirmationDialog exitConfirmationDialog;
    private final MapPanel mapPanel;
    private final ToolbarPanel toolbarPanel;
    private final StatusBarPanel statusBar;

    public CoordinateViewerFrame(final AppConfig appConfig, final CoordinateController coordinateController) {

        this.appConfig = appConfig;
        this.coordinateController = coordinateController;

        this.mapPanel = new MapPanel();

        this.statusBar = new StatusBarPanel("Status: Ready");
        this.toolbarPanel = new ToolbarPanel(this);

        this.autoReloadController = new AutoReloadControllerImpl(
                appConfig.getAutoReloadInterval(),
                this::loadCoordinates
        );
        this.exitConfirmationDialog = new ExitConfirmationDialog();

        initializeFrame();
        loadCoordinates();
        LOGGER.log(Level.INFO, "Completed the initialization of the application.");
    }

    private void initializeFrame() {
        setTitle(appConfig.getAppName());
        setSize(800, 600);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        add(toolbarPanel, BorderLayout.NORTH);
        add(mapPanel, BorderLayout.CENTER);
        add(statusBar, BorderLayout.SOUTH);
        setVisible(true);
    }

    private void toggleAutoReload(boolean enabled) {
        if (enabled) {
            autoReloadController.enable();
            toolbarPanel.setAutoReloadState(true);
            statusBar.updateStatus(String.format(
                    "Auto reload enabled (every %s s)",
                    appConfig.getAutoReloadInterval() / 1000
            ));
        } else {
            autoReloadController.disable();
            toolbarPanel.setAutoReloadState(false);
            statusBar.updateStatus("Auto reload disabled");
        }
    }

    private void loadCoordinates() {
        statusBar.updateStatus("Status: Fetching data...");
        toolbarPanel.setReloadEnabled(false);
        CoordinateWorker worker = new CoordinateWorker(
                coordinateController,
                coordinates -> {
                    mapPanel.setCoordinates(coordinates);
                    statusBar.updateStatus(String.format("Loaded %d points.", coordinates.size()));
                    toolbarPanel.setReloadEnabled(true);
                },
                e -> {
                    statusBar.updateStatus(String.format("Error: %s", e.getMessage()));
                    JOptionPane.showMessageDialog(
                            this,
                            String.format("Failed to fetch coordinates.\n %s", e.getMessage()),
                            "Network Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                    toolbarPanel.setReloadEnabled(true);
                }
        );
        worker.execute();
    }

    @Override
    public void onReload() {
        loadCoordinates();
    }

    @Override
    public void onToggleAutoReload(boolean enabled) {
        toggleAutoReload(enabled);
    }

    @Override
    public void onAbout() {
        toolbarPanel.setAboutButtonDisable();
        new AboutDialog(this, appConfig).setVisible(true);
        toolbarPanel.setAboutButtonEnable();

    }

    @Override
    public void onExit() {
        toolbarPanel.setExitButtonDisable();
        if (exitConfirmationDialog.confirmExit(this)) {
            LOGGER.log(Level.INFO, "Closing the application.");
            autoReloadController.disable();
            dispose();
            System.exit(0);
        }
        toolbarPanel.setExitButtonEnable();
    }
}
