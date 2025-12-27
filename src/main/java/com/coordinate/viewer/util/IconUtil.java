package com.coordinate.viewer.util;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public final class IconUtil {
    private static final String ICON_PATH = "/images/app_icon.png";

    private IconUtil() {}

    public static Image getAppImage(int size) {
        return new ImageIcon(
                Objects.requireNonNull(IconUtil.class.getResource(ICON_PATH))
        ).getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH);
    }

    public static ImageIcon getAppIcon(int size) {
        return new ImageIcon(getAppImage(size));
    }

}
