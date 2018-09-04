package org.lrima.map.Studio.Drawables;

import org.lrima.espece.Espece;
import org.lrima.espece.capteur.Capteur;
import org.lrima.map.Studio.DrawingPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class LineObstacle implements Obstacle, Serializable{

    Point start = null, end = null;
    public static ImageIcon OBSTACLE_ICON = new ImageIcon(LineObstacle.class.getResource("/images/icons/Map_Studio/line.gif"));


    @Override
    public ArrayList<Line> getLines() {
        ArrayList<Line> lignes = new ArrayList<>();
        if(start != null && end != null) {
            lignes.add(new Line(start, end));
        }

        return lignes;
    }

    @Override
    public void onMouseClick(MouseEvent event, DrawingPanel panel, Point pointOnMap) {
        if(start == null){
            start = pointOnMap;
        }
        else{
            end = pointOnMap;
            panel.placeObstacle(this);

        }
    }

    @Override
    public void onMouseMove(MouseEvent event) {

    }

    public Point getStart() {
        return start;
    }
}

//public class Line extends Obstacle implements Serializable {
//
//    public Point start;
//    public Point end;
//    public int strokeWidth = 10;
//
//    public Line(Obstacle obstacle){
//        super(obstacle);
//        start = ((Line) obstacle).start;
//        end = ((Line) obstacle).end;
//        placingIcon = false;
//        isSquare = false;
//    }
//
//    public Line() {
//        super(Obstacle.TYPE_LINE);
//        placingIcon = false;
//        icon = new ImageIcon(getClass().getResource("/images/icons/Map_Studio/line.gif"));
//    }
//
//    @Override
//    public void draw(Graphics2D g) {
//        super.draw(g);
//        g.setStroke(new BasicStroke(10));
//        g.drawLine(start.x, start.y, end.x, end.y);
//    }
//
//
//}
