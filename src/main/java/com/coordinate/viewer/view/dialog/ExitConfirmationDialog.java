package com.coordinate.viewer.view.dialog;

import com.coordinate.viewer.util.IconUtil;

import javax.swing.*;
import java.awt.*;

public class ExitConfirmationDialog {
    public ExitConfirmationDialog() {
    }

    public boolean confirmExit(Component parent) {
        int choice = JOptionPane.showConfirmDialog(
                parent,
                "Are you sure you want to exit?",
                "Confirm Exit",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                IconUtil.getAppIcon(64)
        );
        return choice == JOptionPane.YES_OPTION;
    }
}
