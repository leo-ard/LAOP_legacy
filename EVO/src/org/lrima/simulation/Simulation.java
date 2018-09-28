package org.lrima.simulation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import org.lrima.core.UserPrefs;
import org.lrima.espece.Espece;
import org.lrima.espece.capteur.Capteur;
import org.lrima.network.interfaces.NeuralNetworkModel;
import org.lrima.network.interfaces.NeuralNetworkSuperviser;
import org.lrima.map.Map;
import org.lrima.map.Studio.Drawables.Line;
import org.lrima.map.Studio.Drawables.Obstacle;

public class Simulation extends Thread{

	public static double simulationTime = 0.0;

	//Stores the map information
	private Map map;

	//Keep track of the current generation
	private int generation = 1;
	private int maxGenerations = 20;

	//Stores the cars in the simulation
	private ArrayList<Espece> especesOpen;
	private ArrayList<Espece> especesClosed;

	//Used for the main loop
	private boolean running = true;
	private boolean pausing = false;
	private double msBetweenFrames = 10;

	//Used to trigger an action in the main loop
    private boolean shouldResetAndAddEspece = false;
    private boolean shouldGoToNextGeneration = false;
    private boolean shouldRestart = false;

    //The neural network that the cars in the next generation will have
    private NeuralNetworkModel algorithmModel;

    //NeuralNetworkSupervisor
	private NeuralNetworkSuperviser neuralNetworkSuperviser;

    //Stores information about all the generations
    private ArrayList<Generation> generations;

    private ArrayList<SimulationListener> simulationListeners = new ArrayList<>();

	/**
	 * Initialize variables
	 */
	public Simulation(NeuralNetworkModel algorithmModel) {
		super();
		this.algorithmModel = algorithmModel;
		this.neuralNetworkSuperviser = algorithmModel.getSuperviser();

		Simulation.simulationTime = 0;
		this.running = true;
		this.generations = new ArrayList<>();
		this.map = Map.loadMapFromPreferences();

		this.initializeCars();
	}

	/**
	 * Main loop of the simulation
	 */
	public void run() {

		long currentTime = System.currentTimeMillis();
		long timePassed = (long)msBetweenFrames;
		while(running) {
			if(generation > maxGenerations){

				this.simulationEnd();
				this.terminate();
			}

			if(!pausing) {
				if(especesOpen.size() != 0 && Simulation.simulationTime < UserPrefs.getInt(UserPrefs.KEY_TIME_LIMIT)) {
					//Add the current time to the Simulation.simulationTime
					currentTime = System.currentTimeMillis();
					Simulation.simulationTime += msBetweenFrames;

					//Iterate through all the cars to get the value of the sensors
					Iterator<Espece> iterator = especesOpen.iterator();
					while (((Iterator) iterator).hasNext()){
						Espece espece = iterator.next();

						espece.update(msBetweenFrames);
						this.loopSetCapteur(espece);


						//If the car should die, remove it from the espece open
						if(espece.shouldDie(this.map)){
							iterator.remove();
						}
					}

					this.loopEnd();
				}
				else {
					//Goes to the next generation
					nextGeneration();
				}
			}
			if(UserPrefs.getBoolean(UserPrefs.KEY_REAL_TIME)) {
				try {
					timePassed = System.currentTimeMillis() - currentTime;
					Simulation.sleep((long) ((msBetweenFrames - timePassed) > 0 ?msBetweenFrames - timePassed: 0));
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
			else {
			    try{
			        Simulation.sleep(0);

                }catch(InterruptedException e){

                }
            }

		}
	}


	//Todo: Avoir un arraylist avec les actions a perform at the end of the loop
	/**
	 * Thing to do at the end of the main loop
	 */
	private void loopEnd(){
		if(shouldResetAndAddEspece){
			initializeCars();
			shouldResetAndAddEspece = false;
		}
		if(shouldGoToNextGeneration){
			nextGeneration();
			shouldGoToNextGeneration = false;
		}
		if(shouldRestart){
			this.restart();
			shouldRestart = false;
		}
	}

	/**
	 * Used in the main loop to set the value of the sensors
	 */
	private void loopSetCapteur(Espece espece){
		for(Capteur capteur : espece.getCapteursList()) {
			capteur.reset();
			for (Obstacle obstacle : map.getObstacles()) {
				for (Line line : obstacle.getLines()) {
					double capteurValue = line.getCapteurValue(capteur);
					capteur.setValue(capteurValue);
				}
			}
		}
	}

	/**
	 * Goes to the next generation.
	 * It reloads the user preferences in case it changed.
	 * Kills all the cars before mutating them
	 */
	public void nextGeneration(){
		//Add the generation to the array of generationInformation
		this.generations.add(new Generation(this.generation, this.getClonedEspeceSet()));

		for(SimulationListener simulationListener : simulationListeners){
			simulationListener.onNextGeneration();
		}

		this.generation++;

	    //Kill all cars and add them to especesClosed
        for(int i = 0; i < especesOpen.size(); i++) {
            Espece espece = especesOpen.get(i);

            espece.kill();
            especesClosed.add(espece);
            especesOpen.remove(i);
        }

        //Reset the arrays
		especesOpen = new ArrayList<Espece>(this.neuralNetworkSuperviser.alterEspeceListAtGenerationFinish(especesClosed, this));
        especesClosed = new ArrayList<Espece>();


		for(Espece e : especesOpen){
			e.resetEspece();
			e.tpLikeNew();
		}

        //Reset the time of the simulation
        Simulation.simulationTime = 0;
    }

    private void restart(){
		this.especesOpen = new ArrayList<>();
		this.especesClosed = new ArrayList<>();
		this.generation = 1;
		this.generations = new ArrayList<>();
		Simulation.simulationTime = 0;

		for(SimulationListener listener : this.simulationListeners){
			listener.simulationRestarted();
		}

		this.initializeCars();

	}

	/**
	 * Destroy all cars and create an array containing new cars the size of the
	 * max number of car (set by the user in settings)
	 */
	private void initializeCars() {
		//Reset both car arrays
        especesOpen = new ArrayList<>();
		especesClosed = new ArrayList<>();

		//Retreive the number of cars the user want
		int numberOfCar = UserPrefs.getInt(UserPrefs.KEY_NUMBER_OF_CAR);

		//Add a new car until you reach the max number of car
		while(especesOpen.size() < numberOfCar) {
			especesOpen.add(new Espece(this));
		}
	}

	/**
	 * Find the best fitness in all the cars
	 * @return the fitness of the best car
	 */
	public double getBestFitness(){
		return getBest().getFitness();
	}

	/**
	 * Finds the car with the highest fitness in all cars (dead or alive)
	 * @return the car with the highest fitness
	 */
	public Espece getBest(){
		//Combine cars from especesOpen and especesClosed into same array
		ArrayList<Espece> allCars = this.getAllEspeces();

		//Sorts the new array by the fitness of the cars
		Collections.sort(allCars);


		//Returns the car with the highest fitness (first)
		return allCars.get(0);
	}

	public void addNeuralNetworkSuperviser(){

	}

	/**
	 * Loop through all the cars to find the one which is selected
	 * @return the car that is selected
	 */
	public Espece getSelectedEspece(){
        for(Espece espece : this.especesOpen){
            if(espece.selected){
                return espece;
            }
        }

        return null;
    }

	/**
	 * Sets the selected state of all the cars to not selected
	 */
	public void resetSelected(){
		for(int i = 0 ; i < especesOpen.size() ; i++){
			especesOpen.get(i).setSelected(false);
		}
		for(int i = 0 ; i < especesClosed.size() ; i++){
			especesClosed.get(i).setSelected(false);
		}
	}

	/**
	 * Wait for the main loop to finish its iteration then goes to the next generation
	 */
	public void goToNextGeneration(){
		this.shouldGoToNextGeneration = true;
	}

	/**
	 * Add a car to the closed set
	 * @param e the car to put into the array
	 */
	public void addEspeceToClosed(Espece e){
		especesClosed.add(e);
	}

	/**
	 * Clone all the species inside the closed and open set
	 * @return The list containing the cloned cars
	 */
	private ArrayList<Espece> getClonedEspeceSet(){
		ArrayList<Espece> allSpecies = new ArrayList<>(this.getAllEspeces());
		ArrayList<Espece> clonedList = new ArrayList<>();

		for(Espece e : allSpecies){
			clonedList.add(new Espece(e));
		}

		return clonedList;
	}

	public void terminate(){
		this.running = false;
	}

	public void simulationEnd(){
		for(SimulationListener listener : this.simulationListeners){
			listener.simulationEnded();
		}
	}

	//*******===========================================================================
	//* ACCESSORS AND MUTATORS
	//* ACCESSORS AND MUTATORS
	//********============================================================================/

	/**
	 * Create an array containing all cars (dead or alive)
	 * @return the array with all the cars
	 */
	public ArrayList<Espece> getAllEspeces() {
		ArrayList<Espece> temp = new ArrayList<>();
		temp.addAll(especesClosed);
		temp.addAll(especesOpen);
		
		return temp;
	}

	/**
	 * Map accessor
	 * @return the current map
	 */
    public Map getMap() {
        return map;
    }

	public int getGeneration() {
		return generation;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	public void setNeuralNetworkSuperviser(Class<? extends NeuralNetworkSuperviser> nns){
        try {
            this.neuralNetworkSuperviser = nns.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

	public ArrayList<Espece> getEspecesClosed() {
		return especesClosed;
	}

	public ArrayList<Espece> getEspecesOpen() {
		return especesOpen;
	}

	public boolean isPausing() {
		return pausing;
	}

	public void setPausing(boolean pausing) {
		this.pausing = pausing;
	}

	public ArrayList<Generation> getGenerationList(){
    	return this.generations;
	}

	public void addSimulationListener(SimulationListener listener){
    	this.simulationListeners.add(listener);
	}
	public void setShouldRestart(Boolean shouldRestart){
    	this.shouldRestart = shouldRestart;
	}

	public void setMap(Map map) {
		this.map = map;
	}

	public int getMaxGenerations() {
		return maxGenerations;
	}

	public NeuralNetworkModel getAlgorithm(){
    	return this.algorithmModel;
	}

	public ArrayList<Generation> getGenerations() {
		return generations;
	}
}
