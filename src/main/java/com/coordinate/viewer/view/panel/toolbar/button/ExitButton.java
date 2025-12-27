package com.coordinate.viewer.view.panel.toolbar.button;

import com.coordinate.viewer.view.panel.toolbar.ToolbarActions;

import javax.swing.*;
import java.awt.*;

public class ExitButton extends JButton {

    public ExitButton(final ToolbarActions actions) {
        super("Exit");
        setFocusPainted(false);
        setPreferredSize(new Dimension(120, 30));
        setToolTipText("Exit the application");
        addActionListener(e -> actions.onExit());
    }
}
