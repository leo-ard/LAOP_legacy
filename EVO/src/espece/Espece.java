package espece;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

import core.CONSTANTS;
import espece.capteur.Capteur;
import espece.network.NeuralNetwork;
import map.MapPanel;
import simulation.Simulation;
public class Espece {
	
	//Width doit etre plus grand que height
	public static final int ESPECES_WIDTH = 200, ESPECES_HEIGHT = 101;
	
	private double x, y;
	private int w, h;
	
	private boolean alive;
	
	private double orientationRad;
	private double vitesse;
	private double acceleration;
	
	private int fitness;
	
	private ArrayList<Capteur> capteurs = new ArrayList<Capteur>();
	
	private NeuralNetwork neuralNetwork;
	
	/**
	 * Créer une espèce avec la position et l'orientation spécifiée
	 * 
	 * @param positionDeDepart Position de départ
	 * @param orientationDeDepart Orientation de départ en degrés
	 */
	public Espece(Point positionDeDepart, double orientationDeDepart) {
		this();
		
		this.x = positionDeDepart.x;
		this.y = positionDeDepart.y;
		this.orientationRad = Math.toRadians(orientationDeDepart);
		alive = true;
		neuralNetwork = new NeuralNetwork(3, 2);
	}
	
	public Espece() {
		this.w = ESPECES_WIDTH;
		this.h = ESPECES_HEIGHT;
		capteurs.add(new Capteur(this, -25, w/2, h/2));
		capteurs.add(new Capteur(this, 25, w/2, -h/2));
		capteurs.add(new Capteur(this, 0, w/2, 0));
	}

	public Espece(Espece e) {
		NeuralNetwork n = e.getNeuralNetwork();
	}

	

	public void mutate() {
		//this.neuralNetwork.mutate();
	}
	
	public void update(double dt, double D, double G) {
		if(!alive) {
			return;
		}
		
		//Update le résaux de neuronnes avec la valeur des capteurs
		neuralNetwork.update(capteurs.get(0).getValue(), capteurs.get(1).getValue(),capteurs.get(2).getValue());
		double[] values = neuralNetwork.getOutputValues();
		D = values[0];
		G = values[1];
		
		
		orientationRad += Math.toRadians((double)D*dt/10.0 - (double)G*dt/10.0);
		acceleration = D*dt*CONSTANTS.VITESSE_VOITURE/100 + G*dt*CONSTANTS.VITESSE_VOITURE/100 ;
		vitesse += acceleration/3 - vitesse/50;
		
		if(vitesse < 0) {
			vitesse = 0;
		}
		
		x += vitesse*Math.cos(orientationRad);
		y += vitesse*Math.sin(orientationRad);
		
	}
	
	public void draw(Graphics2D g) {
		AffineTransform oldForm = g.getTransform();
		
		g.rotate(orientationRad,x, y);
		g.setColor(new Color(90, 200, 255));
		
		g.drawImage(MapPanel.IMG_VOITURE, (int)x-w/2, (int)y-h/2, null);
		
		g.setTransform(oldForm);
	}
	
	public void calculateFitness() {
		fitness = (int) (this.x*this.x/10000);
	}
	
	public void kill() {
		alive = false;
		this.calculateFitness();
	}
	
	public void tp(Point p) {
		this.x = p.x;
		this.y = p.y;
	}

	public void resetCapteur() {
		for(Capteur c : capteurs) {
			c.reset();
		}
	}
	
	public double getOrientation() {
		return orientationRad;
	}
	
	public void setOrientation(double oriRad) {
		orientationRad = oriRad;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public int getWidth() {
		return w;
	}

	public int getHeight() {
		return h;
	}

	public int getFitness() {
		// TODO Auto-generated method stub
		return this.fitness;
	}
	
	public NeuralNetwork getNeuralNetwork() {
		return this.neuralNetwork;
	}

	
	//TODO just delete this
	public Espece getMutated() {
		Espece e = this;
		e.mutate();
		return e;
	}

	public ArrayList<Capteur> getCapteursList() {
		// TODO Auto-generated method stub
		return capteurs;
	}
	
	

}
