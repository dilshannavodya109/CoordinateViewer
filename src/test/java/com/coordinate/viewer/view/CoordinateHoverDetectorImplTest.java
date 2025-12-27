package com.coordinate.viewer.view;

import com.coordinate.viewer.model.Coordinate;
import com.coordinate.viewer.view.panel.map.CoordinateTransform;
import com.coordinate.viewer.view.panel.map.impl.CoordinateHoverDetectorImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class CoordinateHoverDetectorImplTest {

    private CoordinateTransform mockTransform;
    private CoordinateHoverDetectorImpl hoverDetector;

    @BeforeEach
    void setUp() {
        mockTransform = mock(CoordinateTransform.class);
        hoverDetector = new CoordinateHoverDetectorImpl(mockTransform);
    }

    private Coordinate coordinate(double x, double y, String name) {
        return Coordinate.builder().x(x).y(y).name(name).build();
    }

    @Test
    @DisplayName("findHoveredCoordinate() returns empty when coordinates list is null")
    void findHoveredCoordinate_nullList_returnsEmpty() {
        Optional<Coordinate> result = hoverDetector.findHoveredCoordinate(
                new Point(100, 100), null, 200);

        assertTrue(result.isEmpty(), "Expected empty result for null list");
    }

    @Test
    @DisplayName("findHoveredCoordinate() returns empty when coordinates list is empty")
    void findHoveredCoordinate_emptyList_returnsEmpty() {
        Optional<Coordinate> result = hoverDetector.findHoveredCoordinate(
                new Point(100, 100), List.of(), 200);

        assertTrue(result.isEmpty(), "Expected empty result for empty list");
    }

    @Test
    @DisplayName("findHoveredCoordinate() returns coordinate when mouse is near its screen point")
    void findHoveredCoordinate_mouseNearCoordinate_returnsCoordinate() {
        Coordinate coordinate = coordinate(10, 10, "A");
        List<Coordinate> coordinates = List.of(coordinate);
        Point mouse = new Point(103, 103);

        when(mockTransform.toScreenPoint(coordinate, 200)).thenReturn(new Point(100, 100));

        Optional<Coordinate> result = hoverDetector.findHoveredCoordinate(mouse, coordinates, 200);

        assertTrue(result.isPresent(), "Expected to detect coordinate near mouse");
        assertEquals(coordinate, result.get());
        verify(mockTransform).toScreenPoint(coordinate, 200);
    }

    @Test
    @DisplayName("findHoveredCoordinate() returns empty when mouse is far from all coordinates")
    void findHoveredCoordinate_mouseFarFromCoordinates_returnsEmpty() {
        Coordinate coordinate1 = coordinate(10, 10, "A");
        Coordinate coordinate2 = coordinate(20, 20, "B");
        List<Coordinate> coordinates = List.of(coordinate1, coordinate2);

        when(mockTransform.toScreenPoint(coordinate1, 200)).thenReturn(new Point(10, 10));
        when(mockTransform.toScreenPoint(coordinate2, 200)).thenReturn(new Point(50, 50));

        Point mouse = new Point(300, 300);

        Optional<Coordinate> result = hoverDetector.findHoveredCoordinate(mouse, coordinates, 200);

        assertTrue(result.isEmpty(), "Expected empty when mouse far from all coordinates");
        verify(mockTransform).toScreenPoint(coordinate1, 200);
        verify(mockTransform).toScreenPoint(coordinate2, 200);
    }

    @Test
    @DisplayName("findHoveredCoordinate() returns the first matching coordinate when multiple are close")
    void findHoveredCoordinate_multipleCloseCoordinates_returnsFirstMatch() {
        Coordinate coordinate1 = coordinate(1, 1, "A");
        Coordinate coordinate2 = coordinate(2, 2, "B");
        List<Coordinate> coordinates = List.of(coordinate1, coordinate2);

        when(mockTransform.toScreenPoint(coordinate1, 200)).thenReturn(new Point(100, 100));
        when(mockTransform.toScreenPoint(coordinate2, 200)).thenReturn(new Point(105, 105));

        Point mouse = new Point(103, 103);

        Optional<Coordinate> result = hoverDetector.findHoveredCoordinate(mouse, coordinates, 200);

        assertTrue(result.isPresent(), "Expected one match");
        assertEquals(coordinate1, result.get(), "Should return first matching coordinate");
    }

    @Test
    @DisplayName("findHoveredCoordinate() should check all coordinates until a match is found")
    void findHoveredCoordinate_invokesTransformForEachUntilMatch() {
        Coordinate coordinate1 = coordinate(1, 1, "A");
        Coordinate coordinate2 = coordinate(2, 2, "B");
        Coordinate coordinate3 = coordinate(3, 3, "C");
        List<Coordinate> coordinates = List.of(coordinate1, coordinate2, coordinate3);

        when(mockTransform.toScreenPoint(coordinate1, 200)).thenReturn(new Point(10, 10));
        when(mockTransform.toScreenPoint(coordinate2, 200)).thenReturn(new Point(300, 300));
        when(mockTransform.toScreenPoint(coordinate3, 200)).thenReturn(new Point(500, 500));

        Point mouse = new Point(303, 303);

        Optional<Coordinate> result = hoverDetector.findHoveredCoordinate(mouse, coordinates, 200);

        assertTrue(result.isPresent());
        assertEquals(coordinate2, result.get(), "Expected to stop at first near coordinate");
        verify(mockTransform).toScreenPoint(coordinate1, 200);
        verify(mockTransform).toScreenPoint(coordinate2, 200);
        verify(mockTransform, never()).toScreenPoint(coordinate3, 200);
    }
}