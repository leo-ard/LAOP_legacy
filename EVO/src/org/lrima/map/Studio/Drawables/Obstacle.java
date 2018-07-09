package org.lrima.map.Studio.Drawables;


import org.omg.CORBA.Bounds;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;

public class Obstacle implements Drawable, Serializable {
    Point position;
    ImageIcon icon;
    public String type;

    public boolean placingIcon = true;
    public boolean isSquare = false;

    //TYPES
    public static String TYPE_START = "START";
    public static String TYPE_END = "END";
    public static String TYPE_LINE = "LINE";

    public Obstacle(Obstacle obstacle){
        this.icon = obstacle.getIcon();
        this.position = obstacle.getPosition();
        this.type = obstacle.type;
        this.placingIcon = obstacle.placingIcon;
        this.isSquare = obstacle.isSquare;
    }
    public Obstacle(String type){
        this.type = type;
    }

    public void move(Point newPos){
        this.position = newPos;
    }

    @Override
    public ImageIcon getIcon() {
        return icon;
    }

    @Override
    public Point getPosition() {
        return position;
    }
}
