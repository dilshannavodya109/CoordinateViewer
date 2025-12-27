package com.coordinate.viewer.view.panel;

import com.coordinate.viewer.view.panel.toolbar.button.AboutButton;
import com.coordinate.viewer.view.panel.toolbar.button.AutoReloadButton;
import com.coordinate.viewer.view.panel.toolbar.button.ExitButton;
import com.coordinate.viewer.view.panel.toolbar.button.ReloadButton;
import com.coordinate.viewer.view.panel.toolbar.ToolbarActions;

import javax.swing.*;
import java.awt.*;

public class ToolbarPanel extends JPanel {
    private final ReloadButton reloadButton;
    private final AutoReloadButton autoReloadButton;
    private final AboutButton aboutButton;
    private final ExitButton exitButton;

    public ToolbarPanel(final ToolbarActions actions) {
        setLayout(new FlowLayout(FlowLayout.CENTER));
        setBackground(UIManager.getColor("Panel.background"));
        this.reloadButton = new ReloadButton(actions);
        this.autoReloadButton = new AutoReloadButton(actions);
        this.aboutButton = new AboutButton(actions);
        this.exitButton=new ExitButton(actions);

        add(reloadButton);
        add(autoReloadButton);
        add(aboutButton);
        add(exitButton);
    }

    public void setReloadEnabled(boolean enabled) {
        reloadButton.setEnabled(enabled);
    }
    public void setAboutButtonDisable() {
        aboutButton.setEnabled(false);
    }
    public void setAboutButtonEnable() {
        aboutButton.setEnabled(true);
    }
    public void setExitButtonDisable() {
        exitButton.setEnabled(false);
    }
    public void setExitButtonEnable() {
        exitButton.setEnabled(true);
    }
    public void setAutoReloadState(boolean enabled) {
        autoReloadButton.setAutoReloadState(enabled);
    }

}
