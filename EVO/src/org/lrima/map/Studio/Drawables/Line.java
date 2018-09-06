package org.lrima.map.Studio.Drawables;

import org.lrima.espece.Espece;
import org.lrima.espece.capteur.Capteur;

import java.awt.*;
import java.awt.geom.Line2D;
import java.io.Serializable;

public class Line implements Serializable {
    Point start = null, end = null;

    public Line(Point start, Point end){
        this.start = start;
        this.end = end;
    }

    public void draw(Graphics2D graphics){
        graphics.setStroke(new BasicStroke(10));
        graphics.drawLine(start.x, start.y, end.x, end.y);
    }

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
    public boolean isCollideWith(Point start2, Point end2){

        return Line2D.linesIntersect(start.x, start.y, end.x, end.y, start2.x, start2.y, end2.x, end2.y);
    }

    public boolean isCollideWith(Capteur capteur){
        Point.Double capteurStart = capteur.getPoint1();
        Point.Double capteurEnd = capteur.getPoint2();

        return isCollideWith(new Point((int)capteurStart.x, (int)capteurStart.y), new Point((int)capteurEnd.x, (int)capteurEnd.y));
    }

    public double getCapteurValue(Capteur c){
        Point.Double capteurStart  = c.getPoint1();
        Point.Double capteurEnd = c.getPoint2();

        //Si collision
        if(isCollideWith(c)){

            float tauxVariationLigne = ((float)end.y - (float)start.y) / ((float)end.x - (float)start.x);
            float ordonneeLigne = (float)end.y - (tauxVariationLigne * (float)end.x);

            if(end.y == start.y){//Ligne horizontale
                tauxVariationLigne = 0;
                ordonneeLigne = start.y;
            }
            else if(end.x == start.x){  //Ligne verticale
                tauxVariationLigne = 0;
                ordonneeLigne = Float.NaN;
            }

            double tauxVariationCapteur = ((capteurEnd.y - capteurStart.y) / (capteurEnd.x - capteurStart.x));
            double ordonneeCapteur = capteurStart.y - (tauxVariationCapteur * capteurStart.x);
            double xCollide = 0;
            double yCollide = 0;

            if (capteurEnd.x == capteurStart.x) {
                //Si le capteur est 90 degrees
                double collisionY = tauxVariationLigne * capteurEnd.x + ordonneeLigne;
                return collisionY / capteurEnd.y; //TODO: Pas la bonne chose return
            }

            //Si la ligne n'est pas verticale

            if(!Float.isNaN(ordonneeLigne)) {
                xCollide = (ordonneeLigne - ordonneeCapteur) / (tauxVariationCapteur - tauxVariationLigne);
                yCollide = tauxVariationLigne * xCollide + ordonneeLigne;
            }
            else{
                xCollide = start.x;
                yCollide = tauxVariationCapteur * start.x + ordonneeCapteur;
            }

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
            return 1;
        }
    }
}
