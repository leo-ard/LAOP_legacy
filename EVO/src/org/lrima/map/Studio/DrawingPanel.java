package org.lrima.map.Studio;

import org.lrima.map.Studio.Drawables.Drawable;
import org.lrima.map.Studio.Drawables.Line;
import org.lrima.map.Studio.Drawables.Obstacle;
import org.lrima.map.Studio.Drawables.Square;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;

public class DrawingPanel extends JPanel implements MouseMotionListener, MouseListener {

    private Obstacle selectedObstacle = null;
    public ArrayList<Obstacle> placedObstacles;
    private Point mousePosition;

    private Point firstClic = null;
    private Point secondClic = null;

    public DrawingPanel(){

        addMouseMotionListener(this);
        addMouseListener(this);

        placedObstacles = new ArrayList<>();
    }

    public void nextObstacle(){
        //Copie
        if(selectedObstacle.type == Obstacle.TYPE_LINE) {
            selectedObstacle = new Line(selectedObstacle);
        }
        else{
            selectedObstacle = new Obstacle(selectedObstacle);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        //L'icon suit la souris de l'utilisateur
        if(selectedObstacle != null){
            g.drawImage(selectedObstacle.getIcon().getImage(),mousePosition.x, mousePosition.y, null);
            if(selectedObstacle.type == Obstacle.TYPE_LINE){
                Graphics2D g2 = (Graphics2D) g;

                g2.setStroke(new BasicStroke(10));
                if(firstClic != null) {
                    if(secondClic == null) {
                        g2.drawLine(firstClic.x, firstClic.y, mousePosition.x, mousePosition.y);
                    }
                    else{
                        g2.drawLine(firstClic.x, firstClic.y, secondClic.x, secondClic.y);
                    }
                }
            }
        }

        //Draw tous les obstacles placed
        Iterator<Obstacle> iterator = placedObstacles.iterator();
        while(iterator.hasNext()) {

            Obstacle currentObstacle = (Obstacle) iterator.next();

            if (currentObstacle.placingIcon) {
                g.drawImage(currentObstacle.getIcon().getImage(), currentObstacle.getPosition().x, currentObstacle.getPosition().y, null);
            }
            else{
                Graphics2D g2 = (Graphics2D) g;

                if(currentObstacle.type.equals(Obstacle.TYPE_LINE)){
                    Line line = (Line) currentObstacle;
                    g2.setStroke(new BasicStroke(line.strokeWidth));
                    g2.drawLine(line.start.x, line.start.y, line.end.x, line.end.y);
                }
            }
        }
    }

    public void setSelectedObstacle(Obstacle selectedObstacle) { this.selectedObstacle = selectedObstacle; }

    @Override
    public void mouseDragged(MouseEvent e) { }

    @Override
    public void mouseMoved(MouseEvent e) {
        mousePosition = new Point(e.getX(), e.getY());
        this.repaint();
    }

    public void resetClic(){
        firstClic = null;
        secondClic = null;
    }

    @Override
    public void mouseClicked(MouseEvent e) { }

    @Override
    public void mousePressed(MouseEvent e) {
        //Clique gauche
        if(e.getButton() == MouseEvent.BUTTON1) {
            //Si il place une icon
            if (selectedObstacle.placingIcon) {
                selectedObstacle.move(mousePosition);
                placedObstacles.add(selectedObstacle);
                nextObstacle();
            }

            //Si le user dessine une ligne
            if (selectedObstacle.type == Obstacle.TYPE_LINE) {
                Line line = (Line) selectedObstacle;

                if (firstClic == null) {
                    if (secondClic == null) {
                        firstClic = new Point(e.getX(), e.getY());
                    } else {
                        firstClic = secondClic;
                        secondClic = null;
                    }
                    line.start = firstClic;
                } else {
                    if (secondClic == null) {
                        secondClic = new Point(e.getX(), e.getY());
                        line.end = secondClic;
                        placedObstacles.add(line);
                        nextObstacle();
                        firstClic = null;
                    }
                }
            }
        }
        else if(e.getButton() == MouseEvent.BUTTON3){ //Clique droit
            //Check si on clique sur un obstacle
            //Si on clique droit sur un obstacle, ça l'enlève des placedObstacles et le met dans selectedObstacle
            if(selectedObstacle == null){
                Iterator<Obstacle> iterator = placedObstacles.iterator();
                while(iterator.hasNext()){
                    Obstacle current = iterator.next();
                    if(current.isSquare){
                        Square square = (Square) current;
                        if(square.rect.contains(e.getPoint())){
                            iterator.remove();
                            selectedObstacle = current;
                        }
                    }
                }
            }
            else {
                resetClic();
                selectedObstacle = null;
                repaint();
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) { }

    @Override
    public void mouseEntered(MouseEvent e) { }

    @Override
    public void mouseExited(MouseEvent e) { }
}
