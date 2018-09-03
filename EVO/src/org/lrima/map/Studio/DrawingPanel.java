package org.lrima.map.Studio;

import org.lrima.map.Map;
import org.lrima.map.MapPanel;
import org.lrima.map.Studio.Drawables.LineObstacle;
import org.lrima.map.Studio.Drawables.Obstacle;
import java.awt.*;
import java.awt.event.MouseEvent;

public class DrawingPanel extends MapPanel {

    private Obstacle selectedObstacle = null;
    Point mousePosition = new Point(0, 0);

    public DrawingPanel(Map map) {
        super(map);
    }

    public void placeObstacle(Obstacle obstacle){
        this.map.getObstacles().add(obstacle);
        selectedObstacle = null;
    }

    public Obstacle getSelectedObstacle() {
        return selectedObstacle;
    }

    public void setSelectedObstacle(Obstacle selectedObstacle) {
        this.selectedObstacle = selectedObstacle;
    }

    public Map getMap(){
        return this.map;
    }

    @Override
    public void setupRelativeGraphics(Graphics2D g) {
        this.drawBackground(g);
        this.drawObstacles(g);

        //Draw the icon following the mouse
        if(selectedObstacle != null){
            g.drawImage(LineObstacle.OBSTACLE_ICON.getImage(), mousePosition.x, mousePosition.y, null);
        }
    }

    @Override
    protected void setupStaticGraphics(Graphics2D graphics) {

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
        if(selectedObstacle != null){
            selectedObstacle.onMouseClick(e, this, mapPointFromScreenPoint(e.getPoint()));
        }

        this.repaint();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        super.mouseDragged(e);
        if(selectedObstacle != null){
            selectedObstacle.onMouseMove(e);
        }

        this.repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        super.mouseMoved(e);
        this.mousePosition = this.mapPointFromScreenPoint(e.getPoint());

        this.repaint();
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
