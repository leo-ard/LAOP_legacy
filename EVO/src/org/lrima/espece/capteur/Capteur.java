package org.lrima.espece.capteur;

import java.awt.*;
import java.awt.geom.Point2D;
import org.lrima.espece.Espece;
import org.lrima.espece.network.interfaces.NeuralNetworkTransmitter;
import org.lrima.map.Studio.Drawables.Line;
import org.lrima.map.Studio.Drawables.Obstacle;
import org.lrima.simulation.Simulation;

public class Capteur implements NeuralNetworkTransmitter {
	
	public static final int CAPTEUR_LENGHT = 1500;
	
	public Espece e;
	private double angle;
	private double calAngle;
	private double x, y;
	private double value = 1.0;

	public Obstacle lastObstacleCollided;
	Simulation simulation;

	private final Color SENSOR_COLOR = new Color(117, 104, 255);
	private final Dimension SENSOR_DOT_SIZE = new Dimension(10, 10);
	
	public Capteur(/*Simulation simulation, */Espece e, double angle, int x, int y) {
		this.e = e;
		this.angle = Math.toRadians(angle);
		value = 1;
		this.x = x;
		this.y = y;

		//this.simulation = simulation;
		//simulation.getMapPanel().getPointOnMap(new Point(x, y));
	}
	
	public void reset() {
		value = 1;
	}
	
	/**
	 * Set la valeur la plus basse, car l'algorith va favoriser les jonctions plus proche du v�hicule que celle plus loins. Aussi a noter que la valeur des capteurs est reset � chaque tick.
	 * @param capteurValue the value of the sensor
	 */
	public void setValue(double capteurValue) {
		if(capteurValue <= value) {
			value = capteurValue;
		}
	}
	/**
	 * Dessine une ligne bleu la ou le capteur est
	 * @param g
	 */
	public void draw(Graphics2D g) {
		// = e.getX();// + e.getHeight();
		//this.y = e.getY();// - e.getWidth();

		int x1 = (int)(e.getX());
		int y1 = (int)(e.getY());
		int lx = (int) (CAPTEUR_LENGHT*Math.cos(-this.angle));
		int ly = (int) (CAPTEUR_LENGHT*Math.sin(-this.angle));



		g.setColor(this.SENSOR_COLOR);
		g.fillOval(x1+ (int)(lx*value) - SENSOR_DOT_SIZE.width / 2,y1- (int)(ly*value) - SENSOR_DOT_SIZE.height / 2, SENSOR_DOT_SIZE.width, SENSOR_DOT_SIZE.height);
	}

	public Point.Double getPoint1(){
	    return new Point.Double(e.getX(), e.getY());
    }

    public Point.Double getPoint2(){


		int ly = (int) getLongeurY();
		int lx = (int )getLongeurX();

		return new Point2D.Double(e.getX() + lx, e.getY() + ly);

    }
	
	/**
	 * 
	 * @return la longueur x du capteur
	 */
	public double getLongeurX() {
		/*double a = e.getOrientation()-angle;
		return CAPTEUR_LENGHT * Math.cos(a) ;*/

		double lx = (CAPTEUR_LENGHT*Math.cos(e.getOrientation()+this.angle));

		return lx;

	}
	
	/**
	 * 
	 * @return la longeur y d capteur
	 */
	public double getLongeurY() {
		/*double a = e.getOrientation()-angle;
		return CAPTEUR_LENGHT * Math.sin(a);*/

		double ly = (CAPTEUR_LENGHT*Math.sin(e.getOrientation()+this.angle));
		return ly;
	}
	
	public double getAngle() {
		double a = e.getOrientation()-angle;
		return a;
	}

	public double getValue() {
		return value;
	}


	@Override
	public double getNeuralNetworkInput() {
		return this.value;
	}
}
