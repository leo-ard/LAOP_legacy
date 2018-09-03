package org.lrima.map.Studio.Drawables;

import org.lrima.espece.Espece;
import org.lrima.espece.capteur.Capteur;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;

public class Line extends Obstacle implements Serializable {

    public Point start;
    public Point end;
    public int strokeWidth = 10;

    public Line(Obstacle obstacle){
        super(obstacle);
        start = ((Line) obstacle).start;
        end = ((Line) obstacle).end;
        placingIcon = false;
        isSquare = false;
    }

    public Line() {
        super(Obstacle.TYPE_LINE);
        placingIcon = false;
        icon = new ImageIcon(getClass().getResource("/images/icons/Map_Studio/line.gif"));
    }

    @Override
    public void draw(Graphics2D g) {
        super.draw(g);
        g.setStroke(new BasicStroke(10));
        g.drawLine(start.x, start.y, end.x, end.y);
    }

    /*public double distanceFrom(Espece e){
        System.out.println(position);

        if(position != null) {
            return Math.sqrt(Math.pow(e.getX() - position.x, 2) + Math.pow(e.getY() - position.y, 2));
        }
        else{
            return 0;
        }
    }*/

    @Override
    public boolean collisionWithRect(Espece espece) {

        //Get the four corners of the square;
        Point bottomLeft = espece.getBottomLeft();
        Point bottomRight = espece.getBottomRight();
        Point topleft = espece.getTopLeft();
        Point topRight = espece.getTopRight();

        boolean collision = false;

        if(isCollideWith(bottomLeft, bottomRight) || isCollideWith(bottomRight, topRight) || isCollideWith(topRight, topleft) || isCollideWith(topleft, bottomLeft)){
            collision = true;
        }

        return collision;
    }

    /**
     * Collisions entre deux lignes
     * @param start2 debut de la deuxieme ligne
     * @param end2 fin de la deuxieme ligne
     * @return
     */
    public boolean isCollideWith(Point.Double start2, Point.Double end2){
        double denominator = ((end.x - start.x) * (end2.y - start2.y)) - ((end.y - start.y) * (end2.x - start2.x));
        double numerator1 = ((start.y - start2.y) * (end2.x - start2.x)) - ((start.x - start2.x) * (end2.y - start2.y));
        double numerator2 = ((start.y - start2.y) * (end.x - start.x)) - ((start.x - start2.x) * (end.y - start.y));

        if(denominator == 0){
            return (numerator1 == 0 && numerator2 == 0);
        }

        double r = numerator1 / denominator;
        double s = numerator2 / denominator;


        return((r >= 0 && r <= 1) && (s >= 0 && s <= 1));
    }
    public boolean isCollideWith(Point start2, Point end2){
        double denominator = ((end.x - start.x) * (end2.y - start2.y)) - ((end.y - start.y) * (end2.x - start2.x));
        double numerator1 = ((start.y - start2.y) * (end2.x - start2.x)) - ((start.x - start2.x) * (end2.y - start2.y));
        double numerator2 = ((start.y - start2.y) * (end.x - start.x)) - ((start.x - start2.x) * (end.y - start.y));

        if(denominator == 0){
            return (numerator1 == 0 && numerator2 == 0);
        }

        double r = numerator1 / denominator;
        double s = numerator2 / denominator;


        return((r >= 0 && r <= 1) && (s >= 0 && s <= 1));
    }

    public boolean isCollideWith(Capteur capteur){

        return isCollideWith(capteur.getPoint1(), capteur.getPoint2());
    }

    public double getCapteurValue(Capteur c){
        Point.Double capteurStart  = c.getPoint1();
        Point.Double capteurEnd = c.getPoint2();

        //Si collision
        if(isCollideWith(c)){

            float tauxVariationLigne = ((float)end.y - (float)start.y) / ((float)end.x - (float)start.x);
            float ordonneeLigne = (float)end.y - (tauxVariationLigne * (float)end.x);

            if(capteurEnd.x == capteurStart.x){
                //Si le capteur est 90 degrees
                double collisionY = tauxVariationLigne * capteurEnd.x + ordonneeLigne;
                return collisionY / capteurEnd.y;
            }

            double tauxVariationCapteur = ((capteurEnd.y - capteurStart.y) / (capteurEnd.x - capteurStart.x));
            double ordonneeCapteur = capteurStart.y - (tauxVariationCapteur * capteurStart.x);

            double xCollide = (ordonneeLigne - ordonneeCapteur) / (tauxVariationCapteur - tauxVariationLigne);
            double yCollide = tauxVariationLigne * xCollide + ordonneeLigne;

            Point.Double pointDeCollision = new Point.Double(xCollide, yCollide);

            //Calcule de la valeur du capteur
            double longueurMax = Capteur.CAPTEUR_LENGHT;

            double xCollideFromCar = xCollide - c.getPoint1().x;
            double yCollideFromCar = yCollide - c.getPoint1().y;

            double longueurCollision = Math.sqrt(Math.pow(xCollideFromCar, 2) + Math.pow(yCollideFromCar, 2));

            return longueurCollision / longueurMax;
        }
        else{
            //Si aucune collision
            return 1.0;
        }
    }
}
