package org.lrima.map.Studio.tools;

import org.lrima.map.Studio.Drawables.Obstacle;
import org.lrima.map.Studio.DrawingPanel;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class CreateObstacleTool  implements Tool {
    Obstacle obstacleInCreation = null;
    ArrayList<Obstacle> addObstacleHere;

    /**
     * Tool that can create obstacles
     *
     * @param obstacle the obstacle type to create
     * @param arrayToAddTheObstacles array to add the obstacles to when the creation is finished
     */
    public CreateObstacleTool(Obstacle obstacle, ArrayList<Obstacle> arrayToAddTheObstacles){
        obstacleInCreation = obstacle;
        addObstacleHere = arrayToAddTheObstacles;
    }

    @Override
    public void onMouseClick(MouseEvent event, DrawingPanel drawingPanel, Point pointOnMap) {
        drawingPanel.cancelSelection();
        obstacleInCreation.onMouseClickCreate(event, this, pointOnMap);
    }

    @Override
    public void draw(Graphics2D g, Point mousePosition) {
        obstacleInCreation.drawWhileEditing(g, mousePosition);
    }

    @Override
    public void drawIcon(Graphics2D g, Point mousePosition){
        g.drawImage(obstacleInCreation.getIcon().getImage(), mousePosition.x, mousePosition.y, null);
    }

    @Override
    public boolean onDrag(MouseEvent e, Point mousePosition) {
        return false;
    }

    @Override
    public void onMousePressed(MouseEvent e, Point mousePosition) {

    }

    /**
     * Function called by the obstacle when it wants to be created
     */
    public void createObstacle(){
        addObstacleHere.add(obstacleInCreation);
        try {
            //creates a new instance of the current obstacle
            obstacleInCreation = (Obstacle) obstacleInCreation.getClass().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
