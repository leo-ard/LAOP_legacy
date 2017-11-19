package espece;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

import espece.capteur.Capteur;
import map.MapPanel;
public class Espece {
	
	//Width doit etre plus grand que height
	public static final int ESPECES_WIDTH = 200, ESPECES_HEIGHT = 101;
	
	boolean alive;
	public double x, y;
	public int w, h;
	public double tetha;
	public double vit;
	public double acc;
	public double spe;
	
	public int fitness;
	
	public ArrayList<Capteur> capteurs = new ArrayList<Capteur>();
	
	
	
	public Espece(Point p, double ori) {
		
		this.w = ESPECES_WIDTH;
		this.h = ESPECES_HEIGHT;
		this.x = p.x;
		this.y = p.y;
		this.tetha = Math.toRadians(ori);
		alive = true;
		
		capteurs.add(new Capteur(this, -25, w/2, h/2));
		capteurs.add(new Capteur(this, 25, w/2, -h/2));
		//capteurs.add(new Capteur(this, 0, w/2, 0));
		
		spe = 12;
	}
	
	public void update(double dt, double D, double G) {
		if(!alive) {
			return;
		}
		
		//capteur
		
		
		//other things
		
		//movement
		/*D = Math.random();
		G = Math.random();*/
		D = Math.pow(capteurs.get(0).getValue(), 2);// * ((capteurs.get(2).getValue())*0.8 + 0.2);
		G = Math.pow(capteurs.get(1).getValue(), 2);// *((capteurs.get(2).getValue())*0.8 + 0.2);
		
		
		tetha += Math.toRadians((double)D*dt/10.0 - (double)G*dt/10.0);
		
		acc = D*dt*spe/100 + G*dt*spe/100 ;
		
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
		//fillRect(g, capteurs.get(0).getX1() + capteurs.get(0).getXRealtive(),capteurs.get(0).getY1() + capteurs.get(0).getYRealtive(), 1, 1);
		
		AffineTransform oldForm = g.getTransform();
		
		g.rotate(tetha,x, y);
		g.setColor(new Color(90, 200, 255));
		
		//fillRect(g, x-w/2, y-h/2, w, h);
		g.drawImage(MapPanel.IMG_VOITURE, (int)x-w/2, (int)y-h/2, null);
		
		
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

	public void resetCapteur() {
		for(Capteur c : capteurs) {
			c.update();
		}
	}

	public void setAngle(int orientation) {
		this.tetha = Math.toRadians(orientation);
	}

}
