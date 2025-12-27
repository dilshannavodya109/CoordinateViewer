package com.coordinate.viewer.view.worker;

import com.coordinate.viewer.model.Coordinate;
import com.coordinate.viewer.controller.CoordinateController;

import javax.swing.*;
import java.util.List;
import java.util.function.Consumer;

public class CoordinateWorker extends SwingWorker<List<Coordinate>, Void> {

    private final CoordinateController controller;
    private final Consumer<List<Coordinate>> onSuccess;
    private final Consumer<Exception> onError;

    public CoordinateWorker(
            final CoordinateController controller,
            final Consumer<List<Coordinate>> onSuccess,
            final Consumer<Exception> onError) {
        this.controller = controller;
        this.onSuccess = onSuccess;
        this.onError = onError;
    }

    @Override
    protected List<Coordinate> doInBackground() {
        return controller.loadCoordinates();
    }

    @Override
    protected void done() {
        try {
            onSuccess.accept(get());
        } catch (Exception e) {
            onError.accept(e);
        }
    }
}
