package espece.capteur;

import java.awt.Color;
import java.awt.Graphics2D;

import espece.Espece;

public class Capteur {
	
	public static final int CAPTEUR_LENGHT = 2000;
	
	Espece e;
	double angle;
	double calAngle;
	double x, y;
	double value;
	
	public Capteur(Espece e, double angle, int x, int y) {
		this.e = e;
		this.angle = Math.toRadians(angle);
		this.x = x;
		this.y = y;
		value = 1;
	}
	
	public void update() {
		value = 1;
	}
	
	public void setValue(double v) {
		if(v <= value) {
			value= v;
		}
		
	}
	
	public void draw(Graphics2D g, double a) {
		g.setColor(Color.CYAN);
		g.drawLine((int)this.getX1(), (int)this.getY1(),(int)this.getX1()+ (int)this.getXRealtive(),(int)this.getY1()+  (int)this.getYRealtive());
		g.setColor(Color.BLACK);
		g.drawRect((int)this.getX1()+ (int)(this.getXRealtive()*value),(int)this.getY1()+  (int)(this.getYRealtive()*value), 1, 1);
	}
	
	public double getX1() {
		return e.x + x * Math.cos(e.tetha) - y * Math.sin(e.tetha);
	}
	
	public double getY1() {
		return e.y + x * Math.sin(e.tetha) + y * Math.cos(e.tetha);
	}
	
	public double getXRealtive() {
		double a = e.tetha-angle;
		return CAPTEUR_LENGHT * Math.cos(a);
	}
	
	public double getYRealtive() {
		double a = e.tetha-angle;
		return CAPTEUR_LENGHT * Math.sin(a);
	}
	
	public double getAngle() {
		double a = e.tetha-angle;
		return a;
	}

	public double getValue() {
		// TODO Auto-generated method stub
		return value;
	}



}
