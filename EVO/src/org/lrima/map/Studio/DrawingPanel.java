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
        System.out.println("test1212");
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
            System.out.println(selectedObstacle);
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


//public class DrawingPanel extends JPanel implements MouseMotionListener, MouseListener, MouseWheelListener {
//
//    private Obstacle selectedObstacle = null;
//    public ArrayList<Obstacle> placedObstacles;
//    private Point mousePositionOnMap;
//
//    private Point firstClic = null;
//    private Point secondClic = null;
//
//    //View range
//    private int viewX, viewY;
//    private int offX, offY;
//    private float zoom;
//    int lastMousePositionX, lastMousePositionY;
//
//    int mapWidth = 10000;
//    int mapHeight = 10000;
//
//    private Point start = null;
//    boolean placedStart = false;
//    boolean placedEnd = false;
//
//    Studio studio;
//
//    public DrawingPanel(Studio studio){
//        this.studio = studio;
//
//        addMouseMotionListener(this);
//        addMouseListener(this);
//        addMouseWheelListener(this);
//
//        this.zoom = 0.40f;
//        this.viewX =0;
//        this.viewY =0;
//
//        placedObstacles = new ArrayList<>();
//    }
//
//    public void nextObstacle(){
//        //Copie
//        if(selectedObstacle.type == Obstacle.TYPE_LINE) {
//            selectedObstacle = new Line(selectedObstacle);
//        }
//        else{
//            //selectedObstacle = new Obstacle(selectedObstacle);
//        }
//    }
//
//    @Override
//    protected void paintComponent(Graphics gld) {
//        Graphics2D g = (Graphics2D) gld;
//        super.paintComponent(g);
//
//        g.scale(zoom, zoom);
//        g.translate((viewX+offX)*(1.0/(double)zoom), (viewY+offY)*(1.0/(double)zoom));
//
//        //Draw la map
//        g.setColor(new Color(255, 178, 102));
//        g.fillRect(0, 0, mapWidth, mapHeight);
//
//        g.setColor(new Color(255, 51, 51));
//        //L'icon suit la souris de l'utilisateur
//        if(selectedObstacle != null){
//            g.drawImage(selectedObstacle.getIcon().getImage(),mousePositionOnMap.x, mousePositionOnMap.y, null);
//            if(selectedObstacle.type.equals(Obstacle.TYPE_LINE)){
//
//                g.setStroke(new BasicStroke(10));
//                if(firstClic != null) {
//                    if(secondClic == null) {
//                        g.drawLine(firstClic.x, firstClic.y, mousePositionOnMap.x, mousePositionOnMap.y);
//                    }
//                    else{
//                        g.drawLine(firstClic.x, firstClic.y, secondClic.x, secondClic.y);
//                    }
//                }
//            }
//        }
//
//        //Draw tous les obstacles placed
//        Iterator<Obstacle> iterator = placedObstacles.iterator();
//        while(iterator.hasNext()) {
//            Obstacle currentObstacle = iterator.next();
//
//            currentObstacle.draw(g);
//        }
//
//        //Draw le start
//        if(start != null) {
//            g.setColor(Color.GREEN);
//            g.fillRect(start.x, start.y, 20, 20);
//        }
//    }
//
//    public void setSelectedObstacle(Obstacle selectedObstacle) { this.selectedObstacle = selectedObstacle; }
//
//    @Override
//    public void mouseDragged(MouseEvent e) {
//        viewX = (int) (e.getX()-lastMousePositionX);
//        viewY = (int) (e.getY()-lastMousePositionY);
//
//        repaint();
//    }
//
//    @Override
//    public void mouseMoved(MouseEvent e) {
//        getMousePositionOnMap(e);
//        this.repaint();
//    }
//
//    private void getMousePositionOnMap(MouseEvent e){
//        int x = -((int)((viewX+offX)*(1.0/(double)zoom) - e.getX()*(1.0/(double)zoom)));
//        int y = -((int)((viewY+offY)*(1.0/(double)zoom) - e.getY()*(1.0/(double)zoom)));
//
//        mousePositionOnMap = new Point(x, y);
//    }
//
//    public void resetClic(){
//        firstClic = null;
//        secondClic = null;
//    }
//
//    @Override
//    public void mouseClicked(MouseEvent e) { }
//
//    @Override
//    public void mousePressed(MouseEvent e) {
//        getMousePositionOnMap(e);
//        lastMousePositionX = e.getX()-viewX;
//        lastMousePositionY = e.getY()-viewY;
//
//        //Clique gauche
//        if(e.getButton() == MouseEvent.BUTTON1) {
//            //Si il place une icon
//            if (selectedObstacle != null && selectedObstacle.placingIcon) {
//                selectedObstacle.move(mousePositionOnMap);
//
//                //Au lieu de store start comme un obstacle, store le comme un point
//                if(!selectedObstacle.type.equals(Obstacle.TYPE_START)) {
//                    placedObstacles.add(selectedObstacle);
//                }
//                else{
//                    this.start = mousePositionOnMap;
//                }
//
//                //On place seulement start et end une fois
//                if(selectedObstacle.type.equals(Obstacle.TYPE_START)){
//                    placedStart = true;
//                    selectedObstacle = null;
//                    studio.startButton.setEnabled(false);
//                }
//                if(selectedObstacle.type.equals(Obstacle.TYPE_END)){
//                    placedEnd = true;
//                    selectedObstacle = null;
//                    studio.endButton.setEnabled(false);
//                }
//
//                nextObstacle();
//            }
//
//            //Si le user dessine une ligne
//            if (selectedObstacle.type.equals(Obstacle.TYPE_LINE)) {
//                Line line = (Line) selectedObstacle;
//
//                if (firstClic == null) {
//                    if (secondClic == null) {
//                        firstClic = mousePositionOnMap;
//                    } else {
//                        firstClic = secondClic;
//                        secondClic = null;
//                    }
//                    line.start = firstClic;
//                } else {
//                    if (secondClic == null) {
//                        secondClic = mousePositionOnMap;
//                        line.end = secondClic;
//                        placedObstacles.add(line);
//                        nextObstacle();
//                        firstClic = null;
//                    }
//                }
//            }
//        }
//        else if(e.getButton() == MouseEvent.BUTTON3){ //Clique droit
//            //Check si on clique sur un obstacle
//            //Si on clique droit sur un obstacle, ça l'enlève des placedObstacles et le met dans selectedObstacle
//            if(selectedObstacle == null){
//                Iterator<Obstacle> iterator = placedObstacles.iterator();
//                while(iterator.hasNext()){
//                    Obstacle current = iterator.next();
//                    if(current.isSquare){
//                        Square square = (Square) current;
//                        if(square.rect.contains(e.getPoint())){
//                            iterator.remove();
//                            selectedObstacle = current;
//                        }
//                    }
//                }
//            }
//            else {
//                resetClic();
//                selectedObstacle = null;
//                repaint();
//            }
//
//            //Check si il n'y a plus de start ou de end
//            placedStart = false;
//            studio.startButton.setEnabled(true);
//
//            Iterator<Obstacle> iterator = placedObstacles.iterator();
//            while(iterator.hasNext()){
//                Obstacle obstacle = iterator.next();
//                if(obstacle.type.equals(Obstacle.TYPE_START)){
//                    placedStart = true;
//                    studio.startButton.setEnabled(false);
//                }
//                if(obstacle.type.equals(Obstacle.TYPE_END)){
//                    placedEnd = true;
//                    studio.endButton.setEnabled(false);
//                }
//            }
//        }
//    }
//
//    @Override
//    public void mouseReleased(MouseEvent e) {
//        viewX = (int) (e.getX()-lastMousePositionX);
//        viewY = (int) (e.getY()-lastMousePositionY);
//    }
//
//    @Override
//    public void mouseEntered(MouseEvent e) { }
//
//    @Override
//    public void mouseExited(MouseEvent e) { }
//
//    @Override
//    public void mouseWheelMoved(MouseWheelEvent e) {
//        double delta = e.getWheelRotation()/10.0;
//        double zoomOld = zoom;
//        zoom /= Math.exp(delta);
//
//        double mouseXOnMap = (viewX+offX)*(1.0/(double)zoomOld) - e.getX()*(1.0/(double)zoomOld);
//        double mouseYOnMap = (viewY+offY)*(1.0/(double)zoomOld) - e.getY()*(1.0/(double)zoomOld);
//
//        offX += (int) (-mouseXOnMap  * -(zoom-zoomOld));
//        offY += (int) (-mouseYOnMap * -(zoom-zoomOld));
//    }
//
//    /**
//     * Reset la map a zero
//     */
//    public void newMap(){
//        this.selectedObstacle = null;
//        this.placedEnd = false;
//        this.placedStart = false;
//        this.placedObstacles = new ArrayList<Obstacle>();
//    }
//
//    public Point getStart() {
//        return start;
//    }
//
//    public void placeObstacle(Obstacle obstacle){
//        this.placedObstacles.add(obstacle);
//        this.selectedObstacle = null;
//    }
//}
