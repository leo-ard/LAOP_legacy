package org.lrima.map.Studio.tools;

import org.lrima.map.Studio.DrawingPanel;

import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * Add the start to the map
 */
public class CreateStartTool implements Tool {


    @Override
    public void onMouseClick(MouseEvent event, DrawingPanel drawingPanel, Point pointOnMap) {
        drawingPanel.getMap().setDepart(pointOnMap);
        drawingPanel.repaint();
    }

    @Override
    public void draw(Graphics2D g, Point mousePosition) {

    }

    @Override
    public void drawIcon(Graphics2D g, Point mousePosition) {

    }

    @Override
    public boolean onDrag(MouseEvent e, Point mousePosition) {
        return false;
    }

    @Override
    public void onMousePressed(MouseEvent e, Point mousePosition) {

    }
}
