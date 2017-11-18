package espece.capteur;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

import espece.Espece;
import simulation.Simulation;

public class Capteur {
	
	public static final int CAPTEUR_LENGHT = 100;
	
	Espece e;
	double angle;
	double calAngle;
	double x, y;
	
	public Capteur(Espece e, double angle, int x, int y) {
		this.e = e;
		this.angle = Math.toRadians(angle);
		this.x = x;
		this.y = y;
	}
	
	public void update() {
		calAngle = this.angle + this.e.tetha;
	}
	
	public double getValue() {
		double a = e.tetha-angle;
		
		double u = CAPTEUR_LENGHT * Math.cos(a);
		double v = CAPTEUR_LENGHT * Math.sin(a);
		//System.out.println(Math.toDegrees(e.tetha));
		
		
		
		return 0.0;
	}
	
	public void draw(Graphics2D g, double a) {
		
		
		
		g.drawLine((int)this.getX1(), (int)this.getY1(),(int)this.getX1()+ (int)this.getXRealtive(),(int)this.getY1()+  (int)this.getYRealtive());
		
		//this.getValue();
		/*AffineTransform af = g.getTransform();
		g.translate(e.x + x, e.y + y);
		g.rotate(a);
		g.drawLine(0, 0,(int)( CAPTEUR_LENGHT*Math.sin(a+angle)),(int)(CAPTEUR_LENGHT* Math.cos(a+angle)));
		g.setTransform(af);*/
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



}
