package com.coordinate.viewer.view.dialog;

import com.coordinate.viewer.config.AppConfig;

import javax.swing.*;

public class AboutDialog extends JDialog {
    public AboutDialog(final JFrame parent, final AppConfig config) {
        super(parent, "About", true);
        setResizable(false);

        String aboutMessage = String.format("""
                        %s
                        Version: %s
                        Developed by: %s
                        Email: %s
                        %s
                        """,
                config.getAppName(),
                config.getVersion(),
                config.getAuthor(),
                config.getAuthorEmail(),
                config.getMetaData()
        );

        JTextArea textArea = new JTextArea(aboutMessage);
        textArea.setEditable(false);
        textArea.setFocusable(false);
        textArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(new JScrollPane(textArea));
        setSize(350, 200);
        setLocationRelativeTo(parent);
    }
}