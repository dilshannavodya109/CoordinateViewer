package com.coordinate.viewer.view.panel.toolbar.button;

import com.coordinate.viewer.view.panel.toolbar.ToolbarActions;

import javax.swing.*;
import java.awt.*;

public class AutoReloadButton extends JToggleButton {
    public AutoReloadButton(final ToolbarActions actions) {
        super("Auto Reload");
        setFocusPainted(false);
        setToolTipText("Enable or disable automatic reloading");
        setPreferredSize(new Dimension(120, 30));

        addActionListener(e -> {
            boolean selected = isSelected();
            updateLabel(selected);
            actions.onToggleAutoReload(selected);
        });
    }

    public void updateLabel(boolean enabled) {
        setText(enabled ? "Stop" : "Auto Reload");
    }

    public void setAutoReloadState(boolean enabled) {
        setSelected(enabled);
        updateLabel(enabled);
    }

}
