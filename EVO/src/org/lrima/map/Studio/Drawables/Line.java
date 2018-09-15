package org.lrima.map.Studio.Drawables;

import org.lrima.espece.Espece;
import org.lrima.espece.capteur.Capteur;

import java.awt.*;
import java.awt.geom.Point2D;
import java.io.Serializable;

/**
 * Base class for obstacles. Obstacles are just made of lines.
 *
 */
public class Line implements Serializable {
    private Point start, end;

    /**
     * Create a line between tow points
     *
     * @param start first point
     * @param end second point
     */
    public Line(Point start, Point end){
        this.start = start;
        this.end = end;
    }

    /**
     * Draw the line
     *
     * @param graphics to draw the line to
     */
    public void draw(Graphics2D graphics){
        graphics.drawLine(start.x, start.y, end.x, end.y);
    }

    /**
     * Check collision with an Espece
     *
     * @param espece Espece to check the collision with
     * @return true if the line touches the espece, false otherwise
     */
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
     * Checks collision with another line
     *
     * @param start2 first point of the other line
     * @param end2 second point
     * @return true if they are colliding, false otherwise
     */
    private boolean isCollideWith(Point.Double start2, Point.Double end2){
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

    /**
     * Checks collision with another line
     *
     * @param start first point of the other line
     * @param end second point
     * @return true if they are colliding, false otherwise
     */
    private boolean isCollideWith(Point start, Point end){
        return this.isCollideWith(new Point.Double(start.x, start.y), new Point.Double(end.x, end.y));
    }


    /**
     * Checks collision with a Capteur
     *
     * @param capteur the capteur to check the collision with
     * @return true if they are colliding, false otherwise
     */
    private boolean isCollideWith(Capteur capteur){
        return isCollideWith(capteur.getPoint1(), capteur.getPoint2());
    }

    /**
     * Get the value of the Capteur.
     *
     * @param c capteur to get the value from
     * @return A number between 0 and 1 depending on the proximity of the point of collision. 1 if there is no collision
     */
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

            //Vertical line
            if(start.x == end.x){
                xCollide = start.x;
                yCollide = tauxVariationCapteur * start.x + ordonneeCapteur;
            }

            //Horizontal line
            if(start.y == end.y){
                xCollide = (start.y - ordonneeCapteur) / tauxVariationCapteur;
                yCollide = start.y;
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
            return 1.0;
        }
    }

    /**
     * Checks if the line is within the range of the mouse point
     *
     * @param mouse the point
     * @param radius the radius of search.
     * @return true if the line is in the range, false otherwise
     */
    public boolean isNear(Point mouse, int radius) {
        double x0 = mouse.x;
        double y0 = mouse.y;

        double x1 = start.x;
        double y1 = start.y;

        double x2 = end.x;
        double y2 = end.y;

        double dist = Math.abs((y2-y1) * x0 - (x2 - x1) * y0 + x2 * y1 - y2 * x1) / Math.sqrt(Math.pow(y2 - y1, 2) + Math.pow(x2 - x1, 2));

        if(radius >= dist){
            if(mouse.x > Math.min(start.x, end.x) - radius && mouse.x < Math.max(start.x, end.x) + radius){
                return true;
            }
        }

        return false;
    }
}
