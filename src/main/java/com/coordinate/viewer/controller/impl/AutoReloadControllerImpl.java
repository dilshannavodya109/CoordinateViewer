package com.coordinate.viewer.controller.impl;


import com.coordinate.viewer.controller.AutoReloadController;

import javax.swing.*;

public class AutoReloadControllerImpl implements AutoReloadController {
    private final Timer autoReloadTimer;

    public AutoReloadControllerImpl(int intervalMillis, Runnable reloadAction) {
        this.autoReloadTimer = new Timer(intervalMillis, e -> reloadAction.run());
        this.autoReloadTimer.setRepeats(true);
    }

    public void enable() {
        autoReloadTimer.start();
    }

    public void disable() {
        autoReloadTimer.stop();
    }
}
