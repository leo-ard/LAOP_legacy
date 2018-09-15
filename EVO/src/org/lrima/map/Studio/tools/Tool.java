package org.lrima.map.Studio.tools;

import org.lrima.map.Studio.DrawingPanel;

import java.awt.*;
import java.awt.event.MouseEvent;

public interface Tool {

    /**
     * Function called when the user is clicking on the screen (and have this obstacle selected)
     *
     * @param event the mouseEvent
     * @param drawingPanel the drawingPanel that called this function
     * @param pointOnMap the mouse point on the map
     */
    void onMouseClick(MouseEvent event, DrawingPanel drawingPanel, Point pointOnMap);

    /**
     * Draws the tool or tool's obstacle currently in edition
     *
     * @param g graphics to draw this icon to
     * @param mousePosition the mouse position
     */
    void draw(Graphics2D g, Point mousePosition);

    /**
     * Draws the icon of the tool near the mouse on each frame
     *
     * @param g graphics to draw this icon to
     * @param mousePosition the mouse position
     */
    void drawIcon(Graphics2D g, Point mousePosition);

    /**
     * Function called when the user is draging on the screen (and have this obstacle selected)
     *
     * @param e the mouseEvent
     * @param mousePosition the mouse point on the map
     * @return true if the tool should retain the screen to move when dragging, false otherwise
     */
    boolean onDrag(MouseEvent e, Point mousePosition);

    /**
     * Function called when the user is pressing the mouse on the screen (and have this obstacle selected)
     *
     * @param e the mouseEvent
     * @param mousePosition the mouse point
     */
    void onMousePressed(MouseEvent e, Point mousePosition);
}
