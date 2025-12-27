package com.coordinate.viewer.view.panel.toolbar.button;

import com.coordinate.viewer.view.panel.toolbar.ToolbarActions;

import javax.swing.*;
import java.awt.*;

public class AboutButton extends JButton {

    public AboutButton(final ToolbarActions actions) {
        super("About");
        setFocusPainted(false);
        setPreferredSize(new Dimension(120, 30));
        setToolTipText("Show application information");
        addActionListener(e -> actions.onAbout());
    }

}
