package org.lrima.map.Studio.Drawables;

import org.lrima.core.UserPrefs;
import org.lrima.map.Studio.tools.CreateObstacleTool;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * The obstacle of type line
 */
public class LineObstacle implements Obstacle, Serializable{
    Point start = null, end = null;
    public static ImageIcon OBSTACLE_ICON = new ImageIcon(LineObstacle.class.getResource(UserPrefs.SRC_TOOLS_LINE));

    private boolean placed;


    @Override
    public ArrayList<Line> getLines() {
        ArrayList<Line> lignes = new ArrayList<>();
        if(start != null && end != null) {
            lignes.add(new Line(start, end));
        }

        return lignes;
    }

    @Override
    public void drawWhileEditing(Graphics2D graphics2D, Point mousePosition) {
       if(start != null){
           graphics2D.drawLine(start.x, start.y, mousePosition.x, mousePosition.y);
       }
    }

    @Override
    public void onMouseClickCreate(MouseEvent event, CreateObstacleTool createObstacleTool, Point pointOnMap) {
        if(start == null){
            start = pointOnMap;
        }
        else{
            end = pointOnMap;
            createObstacleTool.createObstacle();
        }
    }

    @Override
    public ImageIcon getIcon() {
        return LineObstacle.OBSTACLE_ICON;
    }

    @Override
    public ArrayList<Point> getKeyPoints() {
        ArrayList<Point> points = new ArrayList<Point>();

        points.add(start);
        points.add(end);

        return points;
    }

    public Point getStart() {
        return start;
    }

    public Point getEnd() {
        return end;
    }
}