package org.lrima.map.Studio.Drawables;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;

public class Line extends Obstacle implements Serializable {

    public Point start;
    public Point end;
    public int strokeWidth = 10;

    public Line(Obstacle obstacle){
        super(obstacle);
        start = ((Line) obstacle).start;
        end = ((Line) obstacle).end;
        placingIcon = false;
        isSquare = false;
    }

    public Line() {
        super(Obstacle.TYPE_LINE);
        placingIcon = false;
        icon = new ImageIcon(getClass().getResource("/images/icons/Map_Studio/line.gif"));
    }
}
