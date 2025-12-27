package com.coordinate.viewer.view;

import com.coordinate.viewer.model.Coordinate;
import com.coordinate.viewer.view.panel.map.impl.CoordinateTransformImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CoordinateTransformImplTest {

    private CoordinateTransformImpl transform;

    @BeforeEach
    void setUp() {
        transform = new CoordinateTransformImpl();
    }

    private Coordinate createCoordinate(double x, double y) {
        return Coordinate.builder()
                .name("TestPoint")
                .x(x).y(y)
                .build();
    }

    @Test
    @DisplayName("calculateTransform() should correctly compute transform context for valid coordinates")
    void calculateTransform_validCoordinates_setsContextSuccessfully() {
        List<Coordinate> coordinates = List.of(createCoordinate(0, 0), createCoordinate(100, 100));
        Dimension panelSize = new Dimension(200, 200);

        transform.calculateTransform(coordinates, panelSize);
        Point screenPoint = transform.toScreenPoint(createCoordinate(50, 50), panelSize.height);

        assertNotNull(screenPoint, "Transformed point should not be null");
        assertTrue(screenPoint.x > 0 && screenPoint.x < 200, "X should map within panel width");
        assertTrue(screenPoint.y > 0 && screenPoint.y < 200, "Y should map within panel height");
    }

    @Test
    @DisplayName("calculateTransform() should throw IllegalArgumentException when coordinate list is empty")
    void calculateTransform_emptyList_throwsIllegalArgumentException() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> transform.calculateTransform(List.of(), new Dimension(100, 100)));

        assertEquals("Coordinate list cannot be empty", ex.getMessage());
    }

    @Test
    @DisplayName("calculateTransform() should handle equal X values gracefully")
    void calculateTransform_sameXValues_handlesGracefully() {
        List<Coordinate> coordinates = List.of(createCoordinate(10, 10), createCoordinate(10, 20));
        assertDoesNotThrow(() ->
                transform.calculateTransform(coordinates, new Dimension(100, 100))
        );
    }

    @Test
    @DisplayName("calculateTransform() should handle equal Y values gracefully")
    void calculateTransform_sameYValues_handlesGracefully() {
        List<Coordinate> coordinates = List.of(createCoordinate(10, 10), createCoordinate(20, 10));
        assertDoesNotThrow(() ->
                transform.calculateTransform(coordinates, new Dimension(100, 100))
        );
    }

    @Test
    @DisplayName("toScreenPoint() should throw IllegalStateException when transform not calculated")
    void toScreenPoint_withoutTransform_throwsIllegalStateException() {
        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> transform.toScreenPoint(createCoordinate(5, 5), 200));

        assertTrue(ex.getMessage().contains("Transformation has not been calculated yet"));
    }

    @Test
    @DisplayName("toScreenPoint() should correctly map center coordinate near screen center")
    void toScreenPoint_mapsCenterCoordinateToApproximateCenter() {
        List<Coordinate> coordinates = List.of(createCoordinate(0, 0), createCoordinate(100, 100));
        Dimension panel = new Dimension(200, 200);
        transform.calculateTransform(coordinates, panel);

        Point mappedCenter = transform.toScreenPoint(createCoordinate(50, 50), panel.height);

        assertAll("Center point mapping",
                () -> assertTrue(Math.abs(mappedCenter.x - panel.width / 2) < 20,
                        "X coordinate should be roughly centered"),
                () -> assertTrue(Math.abs(mappedCenter.y - panel.height / 2) < 20,
                        "Y coordinate should be roughly centered"));
    }

    @Test
    @DisplayName("calculateTransform() should apply margin and adjust scaling")
    void calculateTransform_appliesMargin_adjustsOffsets() {
        List<Coordinate> coordinates = List.of(createCoordinate(0, 0), createCoordinate(100, 100));
        Dimension panel = new Dimension(200, 200);
        transform.calculateTransform(coordinates, panel);

        Point originMapped = transform.toScreenPoint(createCoordinate(0, 0), panel.height);

        assertAll("Margin effects",
                () -> assertTrue(originMapped.x > 0, "Expected horizontal offset due to margin"),
                () -> assertTrue(originMapped.y > 0, "Expected vertical offset due to margin"));
    }
}
