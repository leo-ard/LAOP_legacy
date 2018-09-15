package org.lrima.map.Studio.Drawables;

import org.lrima.core.UserPrefs;
import org.lrima.map.Studio.tools.CreateObstacleTool;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;

public class MultipleLineObstacle implements Obstacle, Serializable{
    private ArrayList<Point> points = new ArrayList<Point>();
    public static ImageIcon OBSTACLE_ICON = new ImageIcon(MultipleLineObstacle.class.getResource(UserPrefs.SRC_TOOLS_MULTIPLE_LINE));

    @Override
    public ArrayList<Line> getLines() {
        ArrayList<Line> lines = new ArrayList<Line>();
        Point lastPoint = null;
        for(Point point : points){
            if(lastPoint != null){
                lines.add(new Line(point, lastPoint));
            }
            lastPoint = point;
        }

        return lines;
    }

    @Override
    public void drawWhileEditing(Graphics2D graphics, Point mousePosition) {
        Point lastPoint = null;
        Point point = null;
        Iterator<Point> iterator = points.iterator();
        while(iterator.hasNext()){
            point = iterator.next();
            if(lastPoint != null){
                graphics.drawLine(point.x, point.y, lastPoint.x, lastPoint.y);
            }
            lastPoint = point;
        }
        if(point != null){
            graphics.drawLine(point.x, point.y, mousePosition.x, mousePosition.y );
        }

        if(points.size() > 0){
            graphics.fillOval(points.get(0).x-10, points.get(0).y-10, 20,20);
            graphics.fillOval(points.get(points.size()-1).x-10, points.get(points.size()-1).y-10, 20,20);

        }


    }

    @Override
    public void onMouseClickCreate(MouseEvent event, CreateObstacleTool createObstacleTool, Point pointOnMap) {
        //If the mouse is near the first point
        if(this.points.size() > 0 &&  this.pointIsNear(this.points.get(0), pointOnMap, 20)){
            this.points.add(this.points.get(0));
            createObstacleTool.createObstacle();
            return;
        }

        if(this.points.size() > 1 &&  this.pointIsNear(this.points.get(this.points.size()-1), pointOnMap, 20)){
            createObstacleTool.createObstacle();
            return;
        }

        if(event.getButton() == MouseEvent.BUTTON1){
            this.points.add(pointOnMap);
        }
    }

    public boolean pointIsNear(Point p1, Point p2, double threshold){
        return  Math.pow(p2.x - p1.x, 2) + Math.pow(p2.y - p1.y, 2) < threshold*threshold;
    }

    @Override
    public ImageIcon getIcon() {
        return MultipleLineObstacle.OBSTACLE_ICON;
    }

    @Override
    public ArrayList<Point> getKeyPoints() {
        return points;
    }
}
