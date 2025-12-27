package com.coordinate.viewer.view.panel.toolbar.button;

import com.coordinate.viewer.view.panel.toolbar.ToolbarActions;

import javax.swing.*;
import java.awt.*;

public class ReloadButton extends JButton {
    public ReloadButton(final ToolbarActions actions) {
        super("Reload");
        setFocusPainted(false);
        setPreferredSize(new Dimension(120, 30));
        setToolTipText("Manually reload coordinates");
        addActionListener(e -> actions.onReload());
    }
}
