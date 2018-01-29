package espece.capteur;

import java.awt.Color;
import java.awt.Graphics2D;

import espece.Espece;

public class Capteur {
	
	public static final int CAPTEUR_LENGHT = 2000;
	
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
	 * Set la valeur la plus basse, car l'algorith va favoriser les jonctions plus proche du véhicule que celle plus loins. Aussi a noter que la valeur des capteurs est reset à chaque tick.
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
	 * @param a
	 */
	public void draw(Graphics2D g, double a) {
		g.setColor(Color.CYAN);
		g.drawLine((int)this.getX1(), (int)this.getY1(),(int)this.getX1()+ (int)this.getLongeurX(),(int)this.getY1()+  (int)this.getLongeurY());
		g.setColor(Color.BLACK);
		g.drawRect((int)this.getX1()+ (int)(this.getLongeurX()*value),(int)this.getY1()+  (int)(this.getLongeurY()*value), 1, 1);
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
