package org.lrima.map.Studio;

import org.lrima.map.Map;
import org.lrima.map.MapPanel;
import org.lrima.map.Studio.Drawables.Line;
import org.lrima.map.Studio.Drawables.LineObstacle;
import org.lrima.map.Studio.Drawables.Obstacle;
import org.lrima.map.Studio.tools.CreateObstacleTool;
import org.lrima.map.Studio.tools.Tool;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;

public class DrawingPanel extends MapPanel implements PopupMenuListener {
    Point mousePosition, mousePositionStatic = new Point(0, 0);

    /**
     * All the current selected obstacles
     */
    public ArrayList<Obstacle> selectedObstacles;
    public boolean multipleSelect = false;
    public Tool currentTool;

    /**
     * The JMenuItem displayed when right clicking on ground or on obstacle
     */
    private ArrayList<JMenuItem> onGroundMenu, onObstacleMenu = new ArrayList<JMenuItem>();


    /**
     * Creates a panel based on MapPanel that can edit the map
     * @param map the map to edit
     */
    public DrawingPanel(Map map) {
        super(map);
        currentTool = new CreateObstacleTool(new LineObstacle(), this.map.getObstacles());

        selectedObstacles = new ArrayList<Obstacle>();

        onGroundMenu = new ArrayList<JMenuItem>();
        onObstacleMenu = new ArrayList<JMenuItem>();

        setupContextPopUp();
    }

    /**
     * Setup the right click menu
     */
    private void setupContextPopUp() {
        ActionListener menuListener = new ActionListener() {
            public void actionPerformed(ActionEvent event) {

            }
        };

        JPopupMenu popupMenu = new JPopupMenu();

        //Obstacle Menu
        JMenuItem dismantle = new JMenuItem("Dismantle");
        dismantle.addActionListener(menuListener);

        onObstacleMenu.add(dismantle);

        //Ground Menu
        JMenu create = new JMenu("Create");

        JMenuItem sqare = new JMenuItem("Sqare");
        sqare.addActionListener(menuListener);

        create.add(sqare);

        onGroundMenu.add(create);

        popupMenu.addPopupMenuListener(this);

        this.setComponentPopupMenu(popupMenu);
    }

    @Override
    public void setupRelativeGraphics(Graphics2D g) {
        this.drawBackground(g);
        this.drawPoints(g);
        this.drawObstacles(g);
        this.drawStart(g);

        g.setColor(Color.blue);
        g.setStroke(new BasicStroke(2));

        for(Obstacle selectedObstacle : selectedObstacles){
            for(Line line : selectedObstacle.getLines()){
                line.draw(g);
            }
            for(Point keyPoints : selectedObstacle.getKeyPoints()){
                g.fillOval(keyPoints.x -10, keyPoints.y-10, 20, 20);
            }
        }

        currentTool.draw(g, mousePosition);
    }

    @Override
    protected void setupStaticGraphics(Graphics2D g) {
        currentTool.drawIcon(g, mousePositionStatic);
    }

    @Override
    protected void doTransforms(Graphics2D graphics) {
        double translateX, translateY;

        translateX = (viewX+offX)*(1.0/(double)zoom);
        translateY = (viewY+offY)*(1.0/(double)zoom);

        graphics.scale(zoom, zoom);
        graphics.translate(translateX, translateY);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        currentTool.onMouseClick(e, this, mapPointFromScreenPoint(e.getPoint()));

        this.repaint();
    }

    public Map getMap(){
        return this.map;
    }

    /**
     * Remove and returns the obstacle nead pointOnMap
     *
     * @param pointOnMap
     * @return
     */
    public Obstacle obstacleNear(Point pointOnMap){
        Obstacle selectedObstacle = null;
        Iterator<Obstacle> iterator = this.map.getObstacles().iterator();
        while(iterator.hasNext()){
            selectedObstacle = iterator.next();
            for(Line line : selectedObstacle.getLines()){
                if(line.isNear(pointOnMap, 20)){
                    return selectedObstacle;
                }
            }
        }
        return null;
    }

    /**
     * Select the obstacle near by adding it to the selectedObstacle array
     *
     * @param pointOnMap the point to add the obstacle from
     */
    public void selectObstacleNear(Point pointOnMap){
        Obstacle selectedObstacle = obstacleNear(pointOnMap);
        if(!multipleSelect)
            this.cancelSelection();
        if(selectedObstacle != null){
            this.getMap().getObstacles().remove(selectedObstacle);
            selectedObstacles.add(selectedObstacle);
        }
    }


    /**
     * Removes all the selection in the array and put them back into the array of the map
     */
    public void cancelSelection(){
        this.getMap().getObstacles().addAll(this.selectedObstacles);
        this.selectedObstacles.removeAll(this.selectedObstacles);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        super.mousePressed(e);
        this.currentTool.onMousePressed(e, mousePosition);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        super.mouseReleased(e);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if(!currentTool.onDrag(e, mapPointFromScreenPoint(e.getPoint()))){
            super.mouseDragged(e);
        }

        this.mousePositionStatic = e.getPoint();


        this.repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        super.mouseMoved(e);
        this.mousePosition = this.mapPointFromScreenPoint(e.getPoint());
        this.mousePositionStatic = e.getPoint();

        this.repaint();
    }

    @Override
    public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
        selectObstacleNear(mousePosition);
        JPopupMenu popupMenu = (JPopupMenu)e.getSource();
        popupMenu.removeAll();
        if(selectedObstacles.size() > 0){
            onObstacleMenu.forEach(popupMenu::add);
        }
        else{
            onGroundMenu.forEach(popupMenu::add);
        }

        repaint();
    }

    @Override
    public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {

    }

    @Override
    public void popupMenuCanceled(PopupMenuEvent e) {

    }

    public void setTool(Tool tool) {
        this.currentTool = tool;
    }

}