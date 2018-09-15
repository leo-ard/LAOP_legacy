package org.lrima.map.Studio.tools;

import org.lrima.map.Studio.Drawables.Obstacle;
import org.lrima.map.Studio.DrawingPanel;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class EditObstacleTool implements Tool {
    private Point selectedPoint;
    private ArrayList<Obstacle> selectedObstacles;

    /**
     * Tool to edit and move around the obstacles
     *
     * @param selectedObstacles the array of selected obstacle
     */
    public EditObstacleTool(ArrayList<Obstacle> selectedObstacles){
        this.selectedObstacles = selectedObstacles;
    }

    @Override
    public void onMouseClick(MouseEvent event, DrawingPanel drawingPanel, Point pointOnMap) {
        if(event.getButton() == MouseEvent.BUTTON1) {
            drawingPanel.selectObstacleNear(pointOnMap);
        }
    }

    public void init(DrawingPanel drawingPanel){

    }

    @Override
    public void draw(Graphics2D g, Point mousePosition) {


    }

    @Override
    public void drawIcon(Graphics2D g, Point mousePosition) {

    }



    @Override
    public boolean onDrag(MouseEvent e, Point mousePosition) {
        if(selectedPoint != null){
            selectedPoint.setLocation(mousePosition);
            return true;
        }

        return false;
    }

    @Override
    public void onMousePressed(MouseEvent e, Point mousePosition) {
        selectedPoint = null;
        for(Obstacle selectedObstacle : selectedObstacles){
            for(Point p : selectedObstacle.getKeyPoints()){
                if(Math.pow(mousePosition.x - p.x, 2) + Math.pow(mousePosition.y - p.y, 2) < 100){
                    selectedPoint = p;
                }
            }
        }
    }
}
