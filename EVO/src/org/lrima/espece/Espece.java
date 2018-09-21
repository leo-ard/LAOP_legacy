package org.lrima.espece;

import java.awt.*;
import java.util.*;

import org.lrima.Interface.FrameManager;
import org.lrima.annotations.DisplayInfo;
import org.lrima.core.EVO;
import org.lrima.core.UserPrefs;

import org.lrima.espece.capteur.Capteur;
import org.lrima.espece.network.interfaces.NeuralNetwork;
import org.lrima.espece.network.interfaces.NeuralNetworkReceiver;
import org.lrima.map.Studio.Drawables.Line;
import org.lrima.map.Studio.Drawables.Obstacle;
import org.lrima.Interface.EspeceInfoPanel;
import org.lrima.simulation.Simulation;
import org.lrima.map.Map;

public class Espece implements Comparable<Espece>, NeuralNetworkReceiver {
	
	//Width has to be bigger than the height
	private static final int ESPECES_WIDTH = 74, ESPECES_HEIGHT = 50;

	//Stores the position of the car
	@DisplayInfo
	private double x, y;

	private double rightSpeed, leftSpeed;

	//Is the car still alive?
	@DisplayInfo
	private boolean alive;


	private double diedAtTime = 0;

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
	private double fitness = 0.0;

	//Array to store all the sensors
	private ArrayList<Capteur> capteurs = new ArrayList<>();
	private int NB_CAPTEUR = 6; //Maxiumum of 180

	//The neural network of the car
	private NeuralNetwork neuralNetwork;

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

	//Also used to calculate the fitness
	@DisplayInfo(textRepresentation = "Right")
	private float numberOfTimeWentRight = 0;
	@DisplayInfo(textRepresentation = "Left")
	private float numberOfTimeWentLeft = 0;

	//Used to calculate the total distance
	private Point lastPointTraveled;

	//Used to know at what generation the car was created (higher number = the car is good)
	@DisplayInfo
	private int bornOnGeneration = 0;



	//Used to store the color of the car
	private Color voitureColor = Color.RED;

	//Stores the simulation reference
	private Simulation simulation;
	private Class<?extends NeuralNetwork> algorithm;

	/**
	 * Initialize the car with the map to put him into
	 * The starting position and orientation is determined by the map
	 * @param simulation the simulation the car is into
	 */
	public Espece(Simulation simulation) {

		this.algorithm = simulation.getAlgorithm();

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
		//neuralNetwork = new NeuralNetwork(NB_CAPTEUR, 2, true);
		try {
			neuralNetwork = this.algorithm.getConstructor().newInstance();
			neuralNetwork.init(this.capteurs, this);
		}catch (Exception e){
			e.printStackTrace();
		}
    }

	/**
	 * Used to clone a car
	 * @param e the car to clone
	 */
	public Espece(Espece e){
		this.fitness = e.getFitness();
		this.neuralNetwork = e.getNeuralNetwork();
	}

	/**
	 * The function used to calculate the fitness of the car
	 * @return the fitness of the car
	 */
	private double fitnessFunction(){
		double fitness;
		//if(alive) {
		//fitness = getMaxDistanceFromStart() + getTotalDistanceTraveled();

		fitness = (getMaxDistanceFromStart()) / 10000;// + (diedAtTime / UserPrefs.TIME_LIMIT) * 100;

		/*if(diedAtTime != 0){
			System.out.println("--------");
			System.out.println("Max distance from start: " + getMaxDistanceFromStart());
			System.out.println("Died at time / time limit: " + diedAtTime / UserPrefs.TIME_LIMIT);
			System.out.println("Fitness: " + fitness);
		}*/

		//}
		//else{
		//	fitness = this.diedAtTime;
		//}

		/*if(numberOfTimeWentLeft != 0 && numberOfTimeWentRight != 0) {
			if (numberOfTimeWentRight / numberOfTimeWentLeft < 0.01 || numberOfTimeWentLeft / numberOfTimeWentRight < 0.01) {
				fitness -= (diedAtTime / UserPrefs.TIME_LIMIT) * 100;
			}
		}
		else if(numberOfTimeWentLeft == 0 || numberOfTimeWentRight == 0){
			fitness -= (diedAtTime / UserPrefs.TIME_LIMIT) * 100;
		}*/

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

		if(fitness <= bestFitness) {
			this.voitureColor = new Color(255 - (int) (255 * percentageFitness), (int) (255 * percentageFitness), 124, 124);
		}
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

	/**
	 * Get the fitness and update the speed of the wheels from the neural network
	 * @param timePassed the time that passed since the last call of update
	 */
	public void update(double timePassed) {
		//No need to update if car is not alive
		if(!alive) {
			return;
		}

		//Set the fitness to the car
		this.calculateFitnessScore();

		neuralNetwork.feedForward();

		//Get the car speed and turn rate from the settings
        int savedCarSpeed = UserPrefs.preferences.getInt(UserPrefs.KEY_CAR_SPEED, UserPrefs.DEFAULT_CAR_SPEED);
        double savedTurnRate = UserPrefs.preferences.getDouble(UserPrefs.KEY_TURN_RATE, UserPrefs.DEFAULT_TURN_RATE);

		//Applies the speed of each side of the car to move it to the next position
		orientationRad -= Math.toRadians(leftSpeed*timePassed - rightSpeed*timePassed)*savedTurnRate;
		acceleration = rightSpeed*timePassed*savedCarSpeed/100 + leftSpeed*timePassed*savedCarSpeed/100;
		vitesse += acceleration - vitesse;
		this.x += vitesse*Math.cos(orientationRad);
		this.y += vitesse*Math.sin(orientationRad);

		//The car can't go backward
		if(vitesse < 0) {
			vitesse = 0;
		}

		//To get the average speed
		totalSpeed += vitesse;


		//Reset the sensors
		this.resetCapteur();

	}

	/**
	 * Resets the car in case it gets to the next generation
	 */
	public void resetEspece(){
		this.numberOfTimeWentRight = 0;
		this.numberOfTimeWentLeft = 0;
		this.maxDistanceFromStart = 0;
	}

	public void draw(Graphics2D g) {
		//Set the color of the car based on its fitness
		setColor();
		g.setColor(voitureColor);

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
		/*if(simulation.getBest() == this){
			g.setColor(Color.YELLOW);
		}*/
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
	 * Calculate all the variables required to get the fitness of the car
	 * It calculates the biggest distance it gets from the start and
	 * the total distance traveled.
	 * The fitness is then assigned to this car
	 */
	private void calculateFitnessScore(){
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
		this.diedAtTime = Simulation.simulationTime;
	}

	/**
	 * Reset all the sensors to a value of 1
	 */
	private void resetCapteur() {
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

	/**
	 * If the car is alive, it checks if it is coliding with an obstale.
	 * If it is, it adds it to the closed set and kills it
	 * @param map the map to retreive the walls from the map
	 * @return true if it should die, false otherwise
	 */
	public boolean shouldDie(Map map){
		//Regarde si l'espèce meurt
		if(this.alive) {
			for (Obstacle o : map.getObstacles()) {
				//Si l'espece touche un mur
				for(Line line : o.getLines()) {
					if (line.collisionWithRect(this)) {
						try {
							this.simulation.addEspeceToClosed(this);
							this.kill();

							return true;
						} catch (Exception e1) {
							e1.printStackTrace();
							simulation.setRunning(false);
						}
					}
				}
			}
			return false;
		}
		return true; // If the car is dead
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

		return rotatePoint(new Point((int)x, (int)y));
	}

	/**
	 * Get the position of the top right of the car based on its position and orientation
	 * @return the position of the top right point of the car
	 */
	public Point getTopRight() {
		double x = this.x;
		double y = this.y;

		return rotatePoint(new Point((int)x + Espece.ESPECES_WIDTH, (int)y));
	}

	/**
	 * Get the position of the bottom right of the car based on its position and orientation
	 * @return the position of the bottom right point of the car
	 */
	public Point getBottomRight() {
		double x = this.x;
		double y = this.y;

		return rotatePoint(new Point((int)x + Espece.ESPECES_WIDTH, (int)y + Espece.ESPECES_HEIGHT));
	}

	/**
	 * Get the position of the bottom left of the car based on its position and orientation
	 * @return the position of the bottom left point of the car
	 */
	public Point getBottomLeft() {
		double x = this.x;
		double y = this.y;

		return rotatePoint(new Point((int)x, (int)y + Espece.ESPECES_HEIGHT));
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
		if(selected){
			this.simulation.resetSelected();
			FrameManager.instance.changeCarFocus(this);
		}
		this.selected = selected;
	}

	public double getTotalSpeed() {
		return totalSpeed;
	}

	private double getMaxDistanceFromStart() {
		return maxDistanceFromStart;
	}

	public double getTotalDistanceTraveled() {
		return totalDistanceTraveled;
	}

	@Override
	public int compareTo(Espece o) {
		return Double.compare(o.getFitness(), this.getFitness());
	}

	@Override
	public void setNeuralNetworkOutput(double... outputs) {
		this.rightSpeed = outputs[0];
		this.leftSpeed = outputs[1];

	}

	public void setNeuralNetwork(NeuralNetwork network) {
		this.neuralNetwork = network;
		this.neuralNetwork.setTransmitters(this.capteurs);
		this.neuralNetwork.setReceiver(this);
	}

	public void setFitness(double fitness) {
		this.fitness = fitness;
	}
}
