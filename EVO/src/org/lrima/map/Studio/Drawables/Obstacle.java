package org.lrima.map.Studio.Drawables;


import org.lrima.espece.Espece;
import org.lrima.map.Studio.DrawingPanel;
import org.omg.CORBA.Bounds;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;

public interface Obstacle{

    public ArrayList<Line> getLines();

    /**
     * Function called by DrawingPanel to display the obstacle while editing
     *
     * @param graphics the graphics to draw to
     * @param mousePosition the mouse position
     */
    public void drawWhileEditing(Graphics2D graphics, Point mousePosition);

    public void onMouseClick(MouseEvent event, DrawingPanel drawingPanel, Point pointOnMap);
    public void onMouseMove(MouseEvent event);
}

//public abstract class Obstacle implements Drawable, Serializable {
//    Point position;
//    ImageIcon icon;
//    public String type;
//
//    public boolean placingIcon = true;
//    public boolean isSquare = false;
//
//    //TYPES
//    public static String TYPE_START = "START";
//    public static String TYPE_END = "END";
//    public static String TYPE_LINE = "LINE";
//
//    public Obstacle(Obstacle obstacle){
//        this.icon = obstacle.getIcon();
//        this.position = obstacle.getPosition();
//        this.type = obstacle.type;
//        this.placingIcon = obstacle.placingIcon;
//        this.isSquare = obstacle.isSquare;
//    }
//    public Obstacle(String type){
//        this.type = type;
//    }
//
//    public void move(Point newPos){
//        this.position = newPos;
//    }
//
//    @Override
//    public ImageIcon getIcon() {
//        return icon;
//    }
//
//    @Override
//    public Point getPosition() {
//        return position;
//    }
//    @Override
//    public void draw(Graphics2D g) {
//    }
//
//    public abstract boolean collisionWithRect(Espece espece);
//}
