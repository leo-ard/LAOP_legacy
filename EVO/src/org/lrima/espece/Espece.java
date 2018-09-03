package org.lrima.espece;

import java.awt.*;
import java.util.*;

import org.lrima.annotations.DisplayInfo;
import org.lrima.core.UserPrefs;

import org.lrima.espece.capteur.Capteur;
import org.lrima.espece.network.NeuralNetwork;
import org.lrima.simulation.Interface.EspeceInfoPanel;
import org.lrima.simulation.Simulation;

public class Espece {
	
	//Width has to be bigger than the height
	public static final int ESPECES_WIDTH = 74, ESPECES_HEIGHT = 50;

	//Stores the position of the car
	@DisplayInfo
	private double x, y;

	//Is the car still alive?
	@DisplayInfo
	private boolean alive;

	//Orientation of the car in radian
	@DisplayInfo
	private double orientationRad;

	//Speed of the car
	@DisplayInfo
	private double vitesse;

	//Acceleration of the car
	@DisplayInfo
	private double acceleration;

	//The fitness of the car.
	//The fitness is used to classify the car based on how good it did
	@DisplayInfo
	private double fitness;

	//Array to store all the sensors
	private ArrayList<Capteur> capteurs = new ArrayList<Capteur>();
	private int NB_CAPTEUR = 6; //Maxiumum of 180

	//The neural network of the car
	public NeuralNetwork neuralNetwork;

	//Is the car currently selected ?
	public boolean selected;

	//Used to get the average speed
	private double totalSpeed;

	//Used to calculate the fitness
	@DisplayInfo
	private double maxDistanceFromStart;

	//Also used to calculate the fitness
	@DisplayInfo
	private double totalDistanceTraveled;

	//Used to calculate the total distance
	private Point lastPointTraveled;

	//Used to know at what generation the car was created (higher number = the car is good)
	@DisplayInfo
	public int bornOnGeneration = 0;



	//Used to store the color of the car
	private Color voitureColor = Color.RED;

	//Stores the simulation reference
	private Simulation simulation;

	/**
	 * Initialize the car with the map to put him into
	 * The starting position and orientation is determined by the map
	 * @param simulation the simulation the car is into
	 */
	public Espece(Simulation simulation) {

		//Do the base configuration
		this.simulation = simulation;
		this.bornOnGeneration = simulation.getGeneration();
		this.alive = true;
		setupSensors();

		//Get the starting parameters from the map
		this.x = simulation.getMap().getDepart().x;
		this.y = simulation.getMap().getDepart().y;
		this.orientationRad = 0;

		//Used to calculate the total distance traveled
		this.lastPointTraveled = new Point((int)x, (int)y);

		//Setup a base neural network for the car to have
		//If you want to choose which neural network to use, call Espece(Map, NeuralNetwork)
		neuralNetwork = new NeuralNetwork(NB_CAPTEUR, 2, true);
    }

	/**
	 * Initialize a car with a neural network
	 * @param simulation the simulation the car is in
	 * @param neuralNetwork the neural network to copy to the car
	 */
	public Espece(Simulation simulation, NeuralNetwork neuralNetwork){
	    this(simulation);
	    this.neuralNetwork = neuralNetwork;
	    this.neuralNetwork.randomize();
    }

	/**
	 * The function used to calculate the fitness of the car
	 * @return the fitness of the car
	 */
	private double fitnessFunction(){
		double fitness = getMaxDistanceFromStart() + getTotalDistanceTraveled() ;

		//If the car turns in circle at the start
		if(getMaxDistanceFromStart() < 800){
			fitness = 0.0;
		}

		return fitness;
	}

	/**
	 * Creates NB_CAPTEUR number of sensors equally distanced from each other
	 */
	private void setupSensors(){
		//Setup the sensors base on the number of sensors
		//Note that the maximum number of sensors is limited to 180 because of this
		for(int i = 90 ; i > -90 ; i -= 180 / NB_CAPTEUR){
			capteurs.add(new Capteur(this, i, 0, 0));
		}
	}

	//TODO: Doesn't work
	/**
	 * Set the color of the car depending on its fitness
	 * The better the fitness, the greener it gets. The worst the fitness is, the more it is red
	 */
	private void setColor(){
		double bestFitness = this.simulation.getBestFitness();
		double percentageFitness = fitness / bestFitness;

		this.voitureColor = new Color(255 - (int)(255 *  percentageFitness), (int)(255 * percentageFitness), 124, 124);
	}


	/**
	 * Transfer all the value of the sensors into a single array
	 * @return an array containing all the values of the sensors
	 */
	private double[] capteursToArray() {
		double[] capteursValue = new double[this.capteurs.size()];
		for (int i = 0; i < capteursValue.length; i++) {
			capteursValue[i] = this.capteurs.get(i).getValue();
		}
		
		return capteursValue;
		
	}
	
	public void update(double dt) {
		//No need to update if car is not alive
		if(!alive) {
			return;
		}

		//Set the fitness to the car
		this.calculateFitnessScore();

		//Set the color of the car based on its fitness
		setColor();
		
		//Set the speed of the right and left side of the car from the neural network's output
		double[] speeds = getSpeedFromNeuralNetwork();
		double rightSpeed = speeds[0];
		double leftSpeed = speeds[1];

		//Get the car speed and turn rate from the settings
        int savedCarSpeed = UserPrefs.preferences.getInt(UserPrefs.KEY_CAR_SPEED, UserPrefs.DEFAULT_CAR_SPEED);
        double savedTurnRate = UserPrefs.preferences.getDouble(UserPrefs.KEY_TURN_RATE, UserPrefs.DEFAULT_TURN_RATE);

		//Applies the speed of each side of the car to move it to the next position
		orientationRad -= Math.toRadians(leftSpeed*dt - rightSpeed*dt)*savedTurnRate;
		acceleration = rightSpeed*dt*savedCarSpeed/100 + leftSpeed*dt*savedCarSpeed/100;
		vitesse += acceleration - vitesse;
		this.x += vitesse*Math.cos(orientationRad);
		this.y += vitesse*Math.sin(orientationRad);

		//The car can't go backward
		if(vitesse < 0) {
			vitesse = 0;
		}

		//To get the average speed
		totalSpeed += vitesse;

		//TODO: Better way to do this
		//Get the information of the car in the EspeceInfoPanel if it is selected
		if(selected){
			EspeceInfoPanel.update(this);
		}

	}

	public void draw(Graphics2D g) {
		g.setColor(Color.CYAN);

		//TODO: Make the color based on the fitness of the car
		if(bornOnGeneration != simulation.getGeneration()){
			g.setColor(new Color(0, 255, 0, 127));
		}
		else{
			g.setColor(new Color(255, 0, 0, 127));
		}

		//Get all the points of the car and draws it
		int[] pointX = {getTopLeft().x, getTopRight().x, getBottomRight().x, getBottomLeft().x, getTopLeft().x};
		int[] pointY = {getTopLeft().y, getTopRight().y, getBottomRight().y, getBottomLeft().y, getTopLeft().y};
		g.fillPolygon(pointX, pointY, 5);

		//Draw the contour of the car.
		//If the car is selected. Set the color of the contour to blue
		g.setStroke(new BasicStroke(5));
		g.setColor(Color.BLACK);
		if(selected){
			g.setColor(Color.BLUE);
		}
		g.drawPolyline(pointX, pointY, 5);

		//TODO: Make the sensors start in the middle of the car and not on the bottom left
		g.setStroke(new BasicStroke(3));
		g.rotate(orientationRad,x, y);

		//Draw the sensors
		if(selected) {
			for (Capteur c : capteurs) {
				g.setColor(Color.CYAN);
				c.draw(g);
			}
		}
		//Resets the orientation
		g.rotate(-orientationRad, x, y);
	}

	/**
	 * Get the output of the neural network based on the values that the sensors has
	 * @return the speed that the left wheel of the car should go [1] and the speed that the
	 * 			right wheel of the car should go [0]
	 */
	private double[] getSpeedFromNeuralNetwork(){
		double[] capteurArray = capteursToArray();
		neuralNetwork.update(capteurArray);
		//The speed that the car has to go left and right
		double[] speeds = neuralNetwork.getOutputValues();

		return speeds;
	}

	/**
	 * Calculate all the variables required to get the fitness of the car
	 * It calculates the biggest distance it gets from the start and
	 * the total distance traveled.
	 * The fitness is then assigned to this car
	 */
	public void calculateFitnessScore(){
		//Calculate the biggest distance the car gets to from the start
		if(this.simulation.getMap() != null){
			double currentDistance = distanceFrom(this.simulation.getMap().getDepart());
			if(maxDistanceFromStart < currentDistance){
				maxDistanceFromStart = currentDistance;
			}
		}

		//Calculate the total distance of the car
		totalDistanceTraveled += distanceFrom(lastPointTraveled);
		lastPointTraveled = new Point((int)x, (int)y);

		this.fitness = fitnessFunction();
	}

	/**
	 * Get the position of a point based on the orientation of the car
	 * @param point the point to rotate
	 * @return the true position based on the rotation of the car
	 */
	private Point rotatePoint(Point point){
		int centerX = (int)x;
		int centerY = (int)y;

		int newX = (int)((point.x-centerX)*Math.cos(orientationRad) - (point.y-centerY) * Math.sin(orientationRad)) + centerX;
		int newY = (int)((point.x-centerX)*Math.sin(orientationRad) + (point.y-centerY) * Math.cos(orientationRad)) + centerY;

		return new Point(newX, newY);
	}

	/**
	 * Set the alive status of the car to false
	 */
	public void kill() {
		alive = false;
	}

	/**
	 * Reset all the sensors to a value of 1
	 */
	public void resetCapteur() {
		for(Capteur c : capteurs) {
			c.reset();
		}
	}

	/**
	 * Get the distance from a certain point to the car
	 * @param point the point
	 * @return the distance from the point to the car
	 */
	public int distanceFrom(Point point) {
		return (int) ((point.x - this.x)*(point.x - this.x) + (point.y - this.y)*(point.y - this.y));
	}

	/**
	 * Mutate the neural network of the car
	 */
	public void mutate() {
		this.neuralNetwork.mutate();
	}

	//TODO: Is it really necessary?
	/**
	 * Tp la voiure au point d?part et lui donne une orientation orientation.
	 *
	 * De plus, l'acceleration et la vitesse sont mise a z?ro
	 *
	 */
	public void tpLikeNew() {
		this.acceleration = 0;
		this.vitesse = 0;
		this.x = this.simulation.getMap().getDepart().x;
		this.y = this.simulation.getMap().getDepart().y;
		this.orientationRad = 0;
		this.alive = true;
		this.resetCapteur();

	}

	//*******===========================================================================
	// * ACCESSORS AND MUTATORS
	// * ACCESSORS AND MUTATORS
	// ********============================================================================/

	/**
	 * Get the position of the top left of the car based on its position and orientation
	 * @return the position of the top left point of the car
	 */
	public Point getTopLeft() {
		double x = this.x;
		double y = this.y;

		Point topLeft = rotatePoint(new Point((int)x, (int)y));
		return topLeft;
	}

	/**
	 * Get the position of the top right of the car based on its position and orientation
	 * @return the position of the top right point of the car
	 */
	public Point getTopRight() {
		double x = this.x;
		double y = this.y;

		Point topRight = rotatePoint(new Point((int)x + Espece.ESPECES_WIDTH, (int)y));
		return topRight;
	}

	/**
	 * Get the position of the bottom right of the car based on its position and orientation
	 * @return the position of the bottom right point of the car
	 */
	public Point getBottomRight() {
		double x = this.x;
		double y = this.y;

		Point bottomRight = rotatePoint(new Point((int)x + Espece.ESPECES_WIDTH, (int)y + Espece.ESPECES_HEIGHT));
		return bottomRight;
	}

	/**
	 * Get the position of the bottom left of the car based on its position and orientation
	 * @return the position of the bottom left point of the car
	 */
	public Point getBottomLeft() {
		double x = this.x;
		double y = this.y;

		Point bottomLeft = rotatePoint(new Point((int)x, (int)y + Espece.ESPECES_HEIGHT));
		return bottomLeft;
	}
	
	public double getOrientation() {
		return orientationRad;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
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


    /*public void setFitness(double fitness) {
		this.fitness = fitness;
		this.neuralNetwork.setFitness(fitness);
    }*/


	/**
	 * Set the car to selected and make sure it is the only car selected by deselecting all the others
	 * @param selected Should this car be selected?
	 */
	public void setSelected(boolean selected) {
		//Reset la selection des especes a false
		if(selected == true){
			this.simulation.resetSelected();
		}
		this.selected = selected;
	}

	public double getTotalSpeed() {
		return totalSpeed;
	}

	public double getMaxDistanceFromStart() {
		return maxDistanceFromStart;
	}

	public double getTotalDistanceTraveled() {
		return totalDistanceTraveled;
	}
}
