package espece;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import espece.capteur.Capteur;
import simulation.Simulation;

public class Espece {
	
	//Width doit etre plus grand que height
	public final int ESPECES_WIDTH = 40, ESPECES_HEIGHT = 21;
	
	boolean alive;
	public double x, y;
	public int w, h;
	public double tetha;
	public double vit;
	public double acc;
	
	public int fitness;
	
	public ArrayList<Capteur> capteurs = new ArrayList<Capteur>();
	
	
	
	public Espece(Point p) {
		
		this.w = ESPECES_WIDTH;
		this.h = ESPECES_HEIGHT;
		this.x = p.x;
		this.y = p.y;
		this.tetha = 0;
		alive = true;
		
		capteurs.add(new Capteur(this, -30, w/2, h/2));
	}
	
	public void update(long dt, int D, int G) {
		if(!alive) {
			return;
		}
		
		
		//captor
		
		//other things
		
		//movement
		/*double D = Math.random();
		double G = Math.random();*/
		
		
		tetha += Math.toRadians((double)D*dt/10.0 - (double)G*dt/10.0);
		
		acc = D*dt/100.0 + G*dt/100.0 - (double)dt/1000.0;
		
		vit += acc/3 - vit/30;
		
		if(vit < 0) {
			vit = 0;
		}
		
		x += vit*Math.cos(tetha);
		y += vit*Math.sin(tetha);
		
	}
	
	public void updateCapteurs() {
		
	}
	
	public void draw(Graphics2D g) {
		for(Capteur c : capteurs) {
			c.draw(g, tetha);
		}
		fillRect(g, capteurs.get(0).getX1() + capteurs.get(0).getXRealtive(),capteurs.get(0).getY1() + capteurs.get(0).getYRealtive(), 1, 1);
		
		AffineTransform oldForm = g.getTransform();
		
		g.rotate(tetha,x, y);
		g.setColor(new Color(90, 200, 255));
		fillRect(g, x-w/2, y-h/2, w*0.9, h);
		g.setColor(Color.green);
		fillRect(g, x + w*0.9 - w/2, y-h/2, w*0.1, h);
		
		g.setColor(Color.BLACK);
		
		fillRect(g, x, y, 1, 1);
		
		
		
		g.setTransform(oldForm);
	}
	
	public void fillRect(Graphics2D g, double x, double y, double w, double h) {
		g.fillRect((int)x, (int)y, (int)w, (int)h);
	}
	
	public void fitness() {
		
	}
	
	public void kill() {
		alive = false;
	}
	
	public void tp(Point p) {
		this.x = p.x;
		this.y = p.y;
	}

}
