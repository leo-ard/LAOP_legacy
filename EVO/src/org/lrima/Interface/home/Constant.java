package org.lrima.Interface.home;

import java.awt.*;

public class Constant {
    private static final Dimension screenDimension = Toolkit.getDefaultToolkit().getScreenSize();
    private static final int FRAME_WIDTH = screenDimension.width / 3;
    private static final int FRAME_HEIGHT = (int)(screenDimension.height / 1.5);

    public static Rectangle bounds = new Rectangle(screenDimension.width / 2 - FRAME_WIDTH / 2, screenDimension.height / 2 - FRAME_HEIGHT / 2, FRAME_WIDTH, FRAME_HEIGHT);
}
