package org.lrima.map.Studio.Drawables;

import org.lrima.core.UserPrefs;
import org.lrima.map.Studio.tools.CreateObstacleTool;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.Serializable;
import java.util.ArrayList;

public class RectangleObstacle implements Obstacle, Serializable {
    Point start, end;
    public static ImageIcon OBSTACLE_ICON = new ImageIcon(RectangleObstacle.class.getResource(UserPrefs.SRC_TOOLS_REACTANGLE));


    @Override
    public ArrayList<Line> getLines() {
        ArrayList<Line> lines = new ArrayList<Line>();

        if(start != null && end != null){
            lines.add(new Line(start, new Point(end.x, start.y)));
            lines.add(new Line(new Point(end.x, start.y), end));
            lines.add(new Line(end, new Point(start.x, end.y)));
            lines.add(new Line(new Point(start.x, end.y), start));
        }

        return lines;
    }

    @Override
    public void drawWhileEditing(Graphics2D graphics, Point mousePosition) {
        if(start != null){
            graphics.drawRect(Math.min(start.x, mousePosition.x), Math.min(start.y, mousePosition.y), Math.abs(start.x-mousePosition.x), Math.abs(start.y-mousePosition.y) );
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
        return RectangleObstacle.OBSTACLE_ICON;
    }

    @Override
    public ArrayList<Point> getKeyPoints() {
        ArrayList<Point> points = new ArrayList<Point>();
        points.add(start);
        points.add(end);
        return points;
    }
}
