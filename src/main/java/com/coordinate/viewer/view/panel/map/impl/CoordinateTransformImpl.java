package com.coordinate.viewer.view.panel.map.impl;

import com.coordinate.viewer.model.Coordinate;
import com.coordinate.viewer.view.dto.BoundaryCoordination;
import com.coordinate.viewer.view.dto.TransformContext;
import com.coordinate.viewer.view.panel.map.CoordinateTransform;

import java.awt.*;
import java.util.List;

public class CoordinateTransformImpl implements CoordinateTransform {
    private static final double MARGIN_RATIO = 0.10;
    private TransformContext context;

    @Override
    public void calculateTransform(List<Coordinate> coordinates, Dimension panelSize) {
        if (coordinates.isEmpty()) {
            throw new IllegalArgumentException("Coordinate list cannot be empty");
        }

        BoundaryCoordination boundaryCoordination = findBoundaries(coordinates);

        BoundaryCoordination adjusted = applyMargin(boundaryCoordination);

        this.context = computeTransform(adjusted, panelSize);
    }

    @Override
    public Point toScreenPoint(Coordinate coordinate, int panelHeight) {
        if (context == null) {
            throw new IllegalStateException("Transformation has not been calculated yet");
        }

        double scale = context.scale();
        double offsetX = context.offsetX();
        double maxY = context.maxY();

        int x = (int) (coordinate.x() * scale + offsetX);
        int y = (int) ((maxY - coordinate.y()) * scale +
                (panelHeight - maxY * scale) / 2);

        return new Point(x, y);
    }

    private BoundaryCoordination findBoundaries(List<Coordinate> coordinates) {
        double minX = coordinates.stream().mapToDouble(Coordinate::x).min().orElse(0);
        double maxX = coordinates.stream().mapToDouble(Coordinate::x).max().orElse(1);
        double minY = coordinates.stream().mapToDouble(Coordinate::y).min().orElse(0);
        double maxY = coordinates.stream().mapToDouble(Coordinate::y).max().orElse(1);

        if (maxX == minX || maxY == minY) {
            if (minX == 0 && minY == 0) {
                double buffer = 1.0;
                return new BoundaryCoordination(minX - buffer, maxX + buffer, minY - buffer, maxY + buffer);
            }

            minX = (maxX > 0) ? 0 : minX;
            maxX = (maxX > 0) ? maxX : 0;
            minY = (maxY > 0) ? 0 : minY;
            maxY = (maxY > 0) ? maxY : 0;

        }

        return new BoundaryCoordination(minX, maxX, minY, maxY);
    }

    private BoundaryCoordination applyMargin(BoundaryCoordination boundaryCoordination) {
        double width = boundaryCoordination.maxX() - boundaryCoordination.minX();
        double height = boundaryCoordination.maxY() - boundaryCoordination.minY();

        double marginX = width * MARGIN_RATIO;
        double marginY = height * MARGIN_RATIO;

        return new BoundaryCoordination(
                boundaryCoordination.minX() - marginX,
                boundaryCoordination.maxX() + marginX,
                boundaryCoordination.minY() - marginY,
                boundaryCoordination.maxY() + marginY
        );
    }

    private TransformContext computeTransform(BoundaryCoordination boundaryCoordination, Dimension panelSize) {
        double dataWidth = boundaryCoordination.maxX() - boundaryCoordination.minX();
        double dataHeight = boundaryCoordination.maxY() - boundaryCoordination.minY();

        double scaleX = panelSize.getWidth() / dataWidth;
        double scaleY = panelSize.getHeight() / dataHeight;
        double scale = Math.min(scaleX, scaleY);

        double offsetX = -boundaryCoordination.minX() * scale + (panelSize.getWidth() - dataWidth * scale) / 2;
        double offsetY = boundaryCoordination.maxY() * scale - (panelSize.getHeight() - dataHeight * scale) / 2;

        return new TransformContext(scale, offsetX, offsetY, boundaryCoordination.maxY());
    }

}