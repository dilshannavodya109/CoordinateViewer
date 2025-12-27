package com.coordinate.viewer.service;

import com.coordinate.viewer.client.CoordinateClient;
import com.coordinate.viewer.config.AppConfig;
import com.coordinate.viewer.model.Coordinate;
import com.coordinate.viewer.model.exception.CoordinateFetchException;
import com.coordinate.viewer.service.impl.CoordinateServiceImpl;
import com.coordinate.viewer.util.CoordinateValidator;
import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CoordinateServiceImplTest {

    private static final String VALID_RESPONSE =
            "10,20,Colombo\n30,40,Kandy";

    private static final String MIXED_RESPONSE =
            "10,20,Valid\ninvalid,line\n30,40,Another";

    private AppConfig config;
    private CoordinateClient coordinateClient;
    private CoordinateServiceImpl service;

    @BeforeEach
    void setUp() {
        config = mock(AppConfig.class);
        coordinateClient = mock(CoordinateClient.class);

        when(config.getRetryMaxAttempts()).thenReturn(3);
        when(config.getRetryBaseDelayMillis()).thenReturn(10L);

        service = new CoordinateServiceImpl(config, coordinateClient);
    }

    @Test
    @DisplayName("fetchCoordinates -> returns parsed coordinates on success")
    void fetchCoordinates_success() {
        when(coordinateClient.fetchCoordinates()).thenReturn(VALID_RESPONSE);

        try (MockedStatic<CoordinateValidator> validatorMock =
                     mockStatic(CoordinateValidator.class)) {

            validatorMock.when(() -> CoordinateValidator.isValidLine(anyString()))
                    .thenReturn(true);

            List<Coordinate> result = service.fetchCoordinates();

            assertEquals(2, result.size());
            assertEquals("Colombo", result.get(0).getName());
            assertEquals("Kandy", result.get(1).getName());
        }
    }

    @Test
    @DisplayName("fetchCoordinates -> skips invalid lines")
    void fetchCoordinates_skipsInvalidLines() {
        when(coordinateClient.fetchCoordinates()).thenReturn(MIXED_RESPONSE);

        try (MockedStatic<CoordinateValidator> validatorMock =
                     mockStatic(CoordinateValidator.class)) {

            validatorMock.when(() -> CoordinateValidator.isValidLine("10,20,Valid"))
                    .thenReturn(true);
            validatorMock.when(() -> CoordinateValidator.isValidLine("invalid,line"))
                    .thenReturn(false);
            validatorMock.when(() -> CoordinateValidator.isValidLine("30,40,Another"))
                    .thenReturn(true);

            List<Coordinate> result = service.fetchCoordinates();

            assertEquals(2, result.size());
            assertEquals("Valid", result.get(0).getName());
            assertEquals("Another", result.get(1).getName());
        }
    }

    @Test
    @DisplayName("fetchCoordinates -> retries on IOException and succeeds")
    void fetchCoordinates_retryThenSuccess() {
        when(coordinateClient.fetchCoordinates())
                .thenThrow(new CoordinateFetchException("IO", new IOException()))
                .thenReturn("10,20,RetrySuccess");

        try (MockedStatic<CoordinateValidator> validatorMock =
                     mockStatic(CoordinateValidator.class)) {

            validatorMock.when(() -> CoordinateValidator.isValidLine(anyString()))
                    .thenReturn(true);

            List<Coordinate> result = service.fetchCoordinates();

            assertEquals(1, result.size());
            assertEquals("RetrySuccess", result.get(0).getName());
            verify(coordinateClient, times(2)).fetchCoordinates();
        }
    }

    @Test
    @DisplayName("fetchCoordinates -> all retries fail returns empty list")
    void fetchCoordinates_allRetriesFail() {
        when(coordinateClient.fetchCoordinates())
                .thenThrow(new CoordinateFetchException("IO", new IOException()));

        List<Coordinate> result = service.fetchCoordinates();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(coordinateClient, times(3)).fetchCoordinates();
    }

    @Nested
    class MapToCoordinateTests {

        @Test
        void invalidNumber_returnsNull() {
            Coordinate c = invokeMap("abc,20");
            assertNull(c);
        }

        @Test
        void missingName_usesDefault() {
            Coordinate c = invokeMap("10,20");
            assertNotNull(c);
            assertEquals("Not Define", c.getName());
        }

        private Coordinate invokeMap(String line) {
            try {
                var m = CoordinateServiceImpl.class
                        .getDeclaredMethod("mapToCoordinate", String.class);
                m.setAccessible(true);
                return (Coordinate) m.invoke(service, line);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
