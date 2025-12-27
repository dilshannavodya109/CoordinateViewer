package com.coordinate.viewer.view.panel;

import com.coordinate.viewer.model.Coordinate;
import com.coordinate.viewer.view.panel.map.CoordinateHoverDetector;
import com.coordinate.viewer.view.panel.map.CoordinateTransform;
import com.coordinate.viewer.view.panel.map.MapRenderer;
import com.coordinate.viewer.view.panel.map.impl.MapRendererImpl;
import com.coordinate.viewer.view.panel.map.impl.CoordinateHoverDetectorImpl;
import com.coordinate.viewer.view.panel.map.impl.CoordinateTransformImpl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class MapPanel extends JPanel {
    private static final String NO_COORDINATES_MSG = "No coordinates loaded";
    private List<Coordinate> coordinates = new ArrayList<>();
    private final CoordinateTransform coordinateTransform;
    private final CoordinateHoverDetector coordinateHoverManager;
    private final MapRenderer mapRenderer;
    private Coordinate hoveredCoordinate;
    private Point mousePoint;

    public MapPanel() {
        this.coordinateTransform = new CoordinateTransformImpl();
        this.coordinateHoverManager = new CoordinateHoverDetectorImpl(coordinateTransform);
        this.mapRenderer = new MapRendererImpl(coordinateTransform);

        configurePanel();
        initializeMouseListener();
    }

    private void configurePanel() {
        setBackground(Color.WHITE);
        setDoubleBuffered(true);
    }

    private void initializeMouseListener() {
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                handleMouseMoved(e);
            }
        });
    }

    private void handleMouseMoved(MouseEvent event) {
        mousePoint = event.getPoint();
        Optional<Coordinate> hovered = coordinateHoverManager.findHoveredCoordinate(mousePoint, coordinates, getHeight());
        if (!Objects.equals(hoveredCoordinate, hovered.orElse(null))) {
            hoveredCoordinate = hovered.orElse(null);
            repaint();
        }
    }

    public void setCoordinates(List<Coordinate> coordinates) {
        this.coordinates = (coordinates != null) ? coordinates : new ArrayList<>();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        if (coordinates == null || coordinates.isEmpty()) {
            setEmptyMessage(graphics);
            return;
        }

        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.setFont(new Font("Dialog", Font.PLAIN, 12));

        coordinateTransform.calculateTransform(coordinates, getSize());
        mapRenderer.drawCoordinates(graphics2D, coordinates, getHeight());

        if (hoveredCoordinate != null && mousePoint != null) {
            mapRenderer.drawHoverLabel(graphics2D, hoveredCoordinate, mousePoint);
        }
    }

    private void setEmptyMessage(Graphics graphics) {
        graphics.setColor(Color.GRAY);
        graphics.setFont(graphics.getFont().deriveFont(Font.ITALIC, 14f));
        FontMetrics fontMetrics = graphics.getFontMetrics();
        graphics.drawString(NO_COORDINATES_MSG, (getWidth() - fontMetrics.stringWidth(NO_COORDINATES_MSG)) / 2, getHeight() / 2);
    }
}