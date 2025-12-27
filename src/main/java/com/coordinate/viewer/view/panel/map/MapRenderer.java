package com.coordinate.viewer.view.panel.map;

import com.coordinate.viewer.model.Coordinate;

import java.awt.*;
import java.util.List;

public interface MapRenderer {
    void drawCoordinates(Graphics2D graphics2D, List<Coordinate> coordinates, int panelHeight);

    void drawHoverLabel(Graphics2D graphics2D, Coordinate coordinate, Point mousePointer);
}
