package org.lrima.espece.capteur;

import java.awt.Color;
import java.awt.Graphics2D;

import org.lrima.espece.Espece;

public class Capteur {
	
	public static final int CAPTEUR_LENGHT = 1500;
	
	private Espece e;
	private double angle;
	private double calAngle;
	private double x, y;
	private double value;
	
	public Capteur(Espece e, double angle, int x, int y) {
		this.e = e;
		this.angle = Math.toRadians(angle);
		this.x = x;
		this.y = y;
		value = 1;
	}
	
	public void reset() {
		value = 1;
	}
	
	/**
	 * Set la valeur la plus basse, car l'algorith va favoriser les jonctions plus proche du v�hicule que celle plus loins. Aussi a noter que la valeur des capteurs est reset � chaque tick.
	 * @param v
	 */
	public void setValue(double v) {
		if(v <= value) {
			value= v;
		}
	}
	/**
	 * Dessine une ligne bleu la ou le capteur est
	 * @param g
	 */
	public void draw(Graphics2D g) {
		g.setColor(Color.CYAN);

		int x1 = (int)(e.getX());
		int y1 = (int)(e.getY());
		int lx = (int) (CAPTEUR_LENGHT*Math.cos(-this.angle));
		int ly = (int) (CAPTEUR_LENGHT*Math.sin(-this.angle));
		
		g.drawLine(x1,y1,x1+lx,y1-ly);
		g.setColor(Color.BLACK);
		g.drawRect(x1+ (int)(lx*value),y1- (int)(ly*value), 1, 1);
	}
	
	public double getX1() {
		return e.getX() + x * Math.cos(e.getOrientation()) - y * Math.sin(e.getOrientation());
	}
	
	public double getY1() {
		return e.getY() + x * Math.sin(e.getOrientation()) + y * Math.cos(e.getOrientation());
	}
	
	/**
	 * 
	 * @return la longueur x du capteur
	 */
	public double getLongeurX() {
		double a = e.getOrientation()-angle;
		return CAPTEUR_LENGHT * Math.cos(a);
	}
	
	/**
	 * 
	 * @return la longeur y d capteur
	 */
	public double getLongeurY() {
		double a = e.getOrientation()-angle;
		return CAPTEUR_LENGHT * Math.sin(a);
	}
	
	public double getAngle() {
		double a = e.getOrientation()-angle;
		return a;
	}

	public double getValue() {
		// TODO Auto-generated method stub
		return value;
	}



}
