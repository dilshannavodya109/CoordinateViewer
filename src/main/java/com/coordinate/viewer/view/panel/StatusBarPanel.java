package com.coordinate.viewer.view.panel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class StatusBarPanel extends JPanel {
    private final JLabel statusLabel;

    public StatusBarPanel(String initialText) {
        this.statusLabel = new JLabel(initialText);
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(0, 12, 0, 0));
        add(statusLabel, BorderLayout.WEST);
    }

    public void updateStatus(String text) {
        statusLabel.setText(text);
    }
}
