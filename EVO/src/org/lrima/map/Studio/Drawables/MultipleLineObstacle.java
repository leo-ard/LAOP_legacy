package org.lrima.map.Studio.Drawables;

import org.lrima.map.Studio.DrawingPanel;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

public class MultipleLineObstacle implements Obstacle, Serializable{
    private ArrayList<Point> points = new ArrayList<Point>();


    /**
     * Convert all the points into lines
     *
     * @return line the lines converted
     */
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


    }


    @Override
    public void onMouseClick(MouseEvent event, DrawingPanel drawingPanel, Point pointOnMap) {
        if(event.getButton() == MouseEvent.BUTTON1){
            this.points.add(pointOnMap);
        }
        else if(event.getButton() == MouseEvent.BUTTON3){
            drawingPanel.placeObstacle(this);
        }
    }

    @Override
    public void onMouseMove(MouseEvent event) {

    }
}
