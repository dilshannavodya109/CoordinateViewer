package com.coordinate.viewer.view.panel.map.impl;

import com.coordinate.viewer.model.Coordinate;
import com.coordinate.viewer.view.panel.map.CoordinateHoverDetector;
import com.coordinate.viewer.view.panel.map.CoordinateTransform;

import java.awt.*;
import java.util.List;
import java.util.Optional;


public class CoordinateHoverDetectorImpl implements CoordinateHoverDetector {
    private static final int DEFAULT_RADIUS = 6;
    private final CoordinateTransform transform;

    public CoordinateHoverDetectorImpl(final CoordinateTransform transform) {
        this.transform = transform;
    }

    @Override
    public Optional<Coordinate> findHoveredCoordinate(
            Point mouse,
            List<Coordinate> coordinates,
            int panelHeight
    ) {
        if (coordinates == null || coordinates.isEmpty()) {
            return Optional.empty();
        }

        for (Coordinate coordinate : coordinates) {
            Point screenPoint = transform.toScreenPoint(coordinate, panelHeight);
            if (screenPoint.distance(mouse) <= DEFAULT_RADIUS) {
                return Optional.of(coordinate);
            }
        }
        return Optional.empty();
    }
}
