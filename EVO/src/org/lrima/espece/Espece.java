package org.lrima.espece;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

import org.lrima.core.UserPrefs;
import org.lrima.espece.network.NetworkStructure;
import org.lrima.map.Map;

import org.lrima.espece.capteur.Capteur;
import org.lrima.espece.network.NeuralNetwork;
import org.lrima.map.MapPanel;
import org.lrima.map.Studio.Drawables.Line;
import org.lrima.map.Studio.Drawables.Obstacle;

import javax.imageio.ImageIO;

public class Espece {
	
	//Width doit etre plus grand que height
	public static final int ESPECES_WIDTH = 74, ESPECES_HEIGHT = 50;
	
	private double x, y;
	private int w, h;
	
	private boolean alive;
	
	private double orientationRad;
	private double vitesse;
	private double acceleration;
	
	private double fitness;
	
	private ArrayList<Capteur> capteurs = new ArrayList<Capteur>();

	//MAX 180
	private int NB_CAPTEUR = 6;
	
	public NeuralNetwork neuralNetwork;
	
	private Point oldPos;
	
	public static int conter;

	public boolean selected;
	public double totalSpeed;
	public double maxDistanceFromStart;

	public double manual_right = 0.0;
	public double manual_left = 0.0;
	public boolean shouldListenToNN = true;

	private Map map;

	public Point topLeft, topRight, bottomRight, bottomLeft;

	Color voitureColor = Color.RED;

	/**
	 * Créer une espèce avec la position et l'orientation spécifiée
	 *
	 */
	public Espece(Map map) {
		this();

		this.map = map;
		this.x = map.depart.x;
		this.y = map.depart.y;
		this.orientationRad = map.orientation;

        //Load la meilleure org.lrima.espece


		neuralNetwork = new NeuralNetwork(NB_CAPTEUR, 2);


        //Ajoute comme input la position actuel et la destination
		//neuralNetwork.getLayer(0).addNeuron();
		//neuralNetwork.getLayer(0).addNeuron();

    }

    public Espece(Map map, NeuralNetwork nn){
	    this(map);
	    this.neuralNetwork = nn;
    }
	
	public Espece(Point positionDeDepart, double orientationDeDepart, Espece e, Map map) {
		this();
		this.map = map;
		this.x = positionDeDepart.x;
		this.y = positionDeDepart.y;
		this.orientationRad = Math.toRadians(orientationDeDepart);
		this.neuralNetwork = new NeuralNetwork(e.getNeuralNetwork());
		
	}
	
	public Espece() {
		this.w = ESPECES_WIDTH;
		this.h = ESPECES_HEIGHT;

		//Setup les capteurs
        for(int i = 90 ; i > -90 ; i -= 180 / NB_CAPTEUR){
            if(i > 0 && i < 90){
                capteurs.add(new Capteur(this, i, 0, 0));
            }
            else if(i == 0){
                capteurs.add(new Capteur(this, i, 0, 0));
            }
            else{
                capteurs.add(new Capteur(this, i, 0, 0));
            }

        }

		alive = true;
	}

	/**
	 * Met la couleur de la voiture dépendant de son fitness.
	 * Plus son fitness se rapproche du meilleur, plus elle est verte, sinon elle est plus rouge
	 */
	private void setColor(){
		double bestFitness = map.simulation.getBestFitness();
		double percentageFitness = fitness / bestFitness;

		this.voitureColor = new Color(255 - (int)(255 *  percentageFitness), (int)(255 * percentageFitness), 0);
	}

	public Espece(Espece e) {
		neuralNetwork = e.getNeuralNetwork();
	}
	
	public double[] capteursToArray() {
		double[] capteursValue = new double[this.capteurs.size()];
		for (int i = 0; i < capteursValue.length; i++) {
			capteursValue[i] = this.capteurs.get(i).getValue();
		}
		
		return capteursValue;
		
	}
	
	public void update(double dt, double D, double G) {
		if(!alive) {
			return;
		}

		//Set le fitness
		map.setFitnessToEspece(this);
		setColor();

		//Calcul la distance la plus loin que l'auto se rend
		if(this.map != null){
		    double currentDistance = distanceFrom(map.depart);
		    if(maxDistanceFromStart < currentDistance){
		        maxDistanceFromStart = currentDistance;
            }
        }
		
		//Update le résaux de neuronnes avec la valeur des capteurs
		double[] capteurArray = capteursToArray();

		neuralNetwork.update(capteurArray);

		double[] values = neuralNetwork.getOutputValues();
		if(shouldListenToNN) {
            D = values[0];
            G = values[1];
        }
        else{
		    D = manual_right;
		    G = manual_left;
        }

        int savedCarSpeed = UserPrefs.preferences.getInt(UserPrefs.KEY_CAR_SPEED, UserPrefs.DEFAULT_CAR_SPEED);
        double savedTurnRate = UserPrefs.preferences.getDouble(UserPrefs.KEY_TURN_RATE, UserPrefs.DEFAULT_TURN_RATE);


		orientationRad -= Math.toRadians(G*dt - D*dt)*savedTurnRate;
		acceleration = D*dt*savedCarSpeed/100 + G*dt*savedCarSpeed/100;
		vitesse += acceleration - vitesse;
		
		if(vitesse < 0) {
			vitesse = 0;
		}

		//Pour avoir la vitesse moyenne
		totalSpeed += vitesse;
		
		x += vitesse*Math.cos(orientationRad);
		y += vitesse*Math.sin(orientationRad);
		
	}
	
	public void draw(Graphics2D g) {
		AffineTransform oldForm = g.getTransform();

		g.setColor(Color.CYAN);

		g.rotate(orientationRad,x, y);
		g.setColor(voitureColor);

		//Dessine l'image avec sa couleur
		g.fillRect((int)x, (int)y, Espece.ESPECES_WIDTH, Espece.ESPECES_HEIGHT);
		g.drawImage(MapPanel.IMG_VOITURE, (int)x, (int)y, null);

		g.setStroke(new BasicStroke(3));
		//Draw les capteurs
        if(selected) {
            for (Capteur c : capteurs) {
                g.setColor(Color.CYAN);
            	for(Obstacle o : map.obstacles){
            	    if(o.type.equals(Obstacle.TYPE_LINE)){
            	        Line line = (Line) o;

            	        if(line.isCollideWith(c)){
            	            g.setColor(Color.GREEN);
                        }
                    }
                }
                c.draw(g);
            }

            //Draw hitboxe
			//g.translate(-w / 2, -h / 2);
            g.setStroke(new BasicStroke(5));
            g.rotate(-orientationRad, x, y);
			setCorners();
            int[] pointX = {topLeft.x, topRight.x, bottomRight.x, bottomLeft.x, topLeft.x};
            int[] pointY = {topLeft.y, topRight.y, bottomRight.y, bottomLeft.y, topLeft.y};

			g.setColor(Color.BLACK);
            g.drawPolyline(pointX, pointY, 5);
        }

		g.setTransform(oldForm);
	}

	private void setCorners(){
		getTopLeft();
		getTopRight();
		getBottomRight();
		getBottomLeft();
	}

	private Point rotatePoint(Point point){
		int centerX = (int)x;
		int centerY = (int)y;

		int newX = (int)((point.x-centerX)*Math.cos(orientationRad) - (point.y-centerY) * Math.sin(orientationRad)) + centerX;
		int newY = (int)((point.x-centerX)*Math.sin(orientationRad) + (point.y-centerY) * Math.cos(orientationRad)) + centerY;

		return new Point(newX, newY);
	}

	public Point getTopLeft() {
		double x = this.x;
		double y = this.y;

		topLeft = rotatePoint(new Point((int)x, (int)y));
		return topLeft;
	}

	public Point getTopRight() {
		double x = this.x;
		double y = this.y;

		topRight = rotatePoint(new Point((int)x + w, (int)y));
		return topRight;
	}

	public Point getBottomRight() {
		double x = this.x;
		double y = this.y;

		bottomRight = rotatePoint(new Point((int)x + w, (int)y + h));
		return bottomRight;
	}

	public Point getBottomLeft() {
		double x = this.x;
		double y = this.y;

		bottomLeft = rotatePoint(new Point((int)x, (int)y + h));
		return bottomLeft;
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

	public double getFitness() {
		return this.fitness;
	}
	
	public NeuralNetwork getNeuralNetwork() {
		return this.neuralNetwork;
	}

	public ArrayList<Capteur> getCapteursList() {
		return capteurs;
	}

	public int distanceFrom(Point point) {
		return (int) ((point.x - this.x)*(point.x - this.x) + (point.y - this.y)*(point.y - this.y));
	}

	/**
	 * Tp la voiure au point départ et lui donne une orientation orientation.
	 * 
	 * De plus, l'acceleration et la vitesse sont mise a zéro
	 * 
	 * @param depart
	 * @param orientation
	 */
	public void tpLikeNew(Point depart, int orientation) {
		this.acceleration = 0;
		this.vitesse = 0;
		this.x = depart.getX();
		this.y = depart.getY();
		this.orientationRad = Math.toRadians(orientation);
		this.alive = true;
		this.resetCapteur();
		
	}

	public boolean isDead() {
		// TODO Auto-generated method stub
		return !alive;
	}
	
	public void mutate() {
		this.neuralNetwork.mutate();
	}


    public void setFitness(double fitness) {
		this.fitness = fitness;
		this.neuralNetwork.setFitness(fitness);
    }

}
