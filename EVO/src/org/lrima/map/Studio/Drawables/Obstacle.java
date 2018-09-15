package org.lrima.map.Studio.Drawables;


import org.lrima.map.Studio.DrawingPanel;
import org.lrima.map.Studio.tools.CreateObstacleTool;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public interface Obstacle{

    /**
     * Convert the obstacle into lines
     *
     * @return the lines of the obstacle
     */
    public ArrayList<Line> getLines();

    /**
     * Function called by DrawingPanel to display the obstacle while editing
     *
     * @param graphics the graphics to draw to
     * @param mousePosition the mouse position
     */
    public void drawWhileEditing(Graphics2D graphics, Point mousePosition);

    /**
     * Function called only when creating the obstacle. Each click while creating is parsed within this function
     *
     * @param event the mouse event
     * @param createObstacleTool the tool that triggered this function
     * @param pointOnMap the point on the map of the mouse
     */
    public void onMouseClickCreate(MouseEvent event, CreateObstacleTool createObstacleTool, Point pointOnMap);

    ImageIcon getIcon();

    /**
     * Gets the points that define an obstacle. Used primarily by the edit mode to move them around.
     *
     * @return the key points
     */
    ArrayList<Point> getKeyPoints();
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
