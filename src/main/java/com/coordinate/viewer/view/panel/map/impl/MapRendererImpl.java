package com.coordinate.viewer.view.panel.map.impl;

import com.coordinate.viewer.model.Coordinate;
import com.coordinate.viewer.view.panel.map.CoordinateTransform;
import com.coordinate.viewer.view.panel.map.MapRenderer;

import java.awt.*;
import java.util.List;

public class MapRendererImpl implements MapRenderer {
    private final CoordinateTransform coordinateTransform;

    public MapRendererImpl(final CoordinateTransform coordinateTransform) {
        this.coordinateTransform = coordinateTransform;
    }

    @Override
    public void drawCoordinates(Graphics2D graphics2D, List<Coordinate> coordinates, int panelHeight) {
        graphics2D.setColor(Color.BLUE);
        for (Coordinate coordinate : coordinates) {
            Point p = coordinateTransform.toScreenPoint(coordinate, panelHeight);
            graphics2D.fillOval(p.x - 3, p.y - 3, 6, 6);
        }
    }

    public void drawHoverLabel(Graphics2D graphics2D, Coordinate coordinate, Point mouse) {
        if (coordinate == null) return;

        String text = coordinate.name();
        FontMetrics fontMetrics = graphics2D.getFontMetrics();
        int textWidth = fontMetrics.stringWidth(text);
        int textHeight = fontMetrics.getHeight();
        int xAxis = mouse.x + 10;
        int yAxis = mouse.y - 10;

        graphics2D.setColor(new Color(255, 255, 255, 220));
        graphics2D.fillRoundRect(xAxis - 4, yAxis - textHeight + 4, textWidth + 8, textHeight, 8, 8);

        graphics2D.setColor(new Color(50, 50, 50, 200));
        graphics2D.drawRoundRect(xAxis - 4, yAxis - textHeight + 4, textWidth + 8, textHeight, 8, 8);

        graphics2D.setColor(Color.BLACK);
        graphics2D.drawString(text, xAxis, yAxis);
    }
}