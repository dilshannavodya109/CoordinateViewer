package com.coordinate.viewer.view.panel.map;

import com.coordinate.viewer.model.Coordinate;

import java.awt.*;
import java.util.List;

public interface CoordinateTransform {
    void calculateTransform(List<Coordinate> coordinates, Dimension panelSize);

    Point toScreenPoint(Coordinate coordinate, int panelHeight);
}
