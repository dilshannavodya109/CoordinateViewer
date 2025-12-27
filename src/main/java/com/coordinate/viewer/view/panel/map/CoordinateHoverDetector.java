package com.coordinate.viewer.view.panel.map;

import com.coordinate.viewer.model.Coordinate;

import java.awt.*;
import java.util.List;
import java.util.Optional;

public interface CoordinateHoverDetector {
    Optional<Coordinate> findHoveredCoordinate(Point mouse, List<Coordinate> coordinates, int panelHeight);
}
