package com.coordinate.viewer.controller.impl;

import com.coordinate.viewer.model.Coordinate;
import com.coordinate.viewer.service.CoordinateService;
import com.coordinate.viewer.controller.CoordinateController;

import java.util.List;

public class CoordinateControllerImpl implements CoordinateController {
    private final CoordinateService coordinateService;

    public CoordinateControllerImpl(final CoordinateService coordinateService) {
        this.coordinateService = coordinateService;
    }

    public List<Coordinate> loadCoordinates() {
        return coordinateService.fetchCoordinates();
    }

}
