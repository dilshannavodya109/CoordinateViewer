package com.coordinate.viewer.service;

import com.coordinate.viewer.model.Coordinate;

import java.util.List;

public interface CoordinateService {
    List<Coordinate> fetchCoordinates();
}
