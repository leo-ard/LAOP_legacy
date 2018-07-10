package org.lrima.map.Studio.Drawables;

import javax.swing.*;
import java.awt.*;

public interface Drawable {

    public ImageIcon getIcon();
    public Point getPosition();

    public void draw(Graphics2D g);
}
