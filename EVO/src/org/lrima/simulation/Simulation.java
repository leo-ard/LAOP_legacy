package org.lrima.simulation;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

import org.lrima.core.UserPrefs;
import org.lrima.espece.Espece;
import org.lrima.espece.capteur.Capteur;
import org.lrima.espece.network.interfaces.NeuralNetwork;
import org.lrima.map.Map;
import org.lrima.map.Studio.Drawables.Line;
import org.lrima.map.Studio.Drawables.Obstacle;
import org.lrima.simulation.Interface.GraphicPanel;
import org.lrima.simulation.selection.NaturalSelection;

public class Simulation extends Thread{

	public static double currentTime = 0.0;

	//Stores the map information
	private Map map;

	//Keep track of the current generation
	private int generation = 1;

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
    private NeuralNetwork neuralNetworkToUse = null;

    //Stores information about all the generations
    private ArrayList<Generation> generations;

    private ArrayList<SimulationListener> simulationListeners = new ArrayList<>();

	/**
	 * Initialize variables
	 */
	public Simulation() {
		super();

		Simulation.currentTime = 0;
		this.running = true;
		this.generations = new ArrayList<>();
		this.map = Map.loadMapFromPreferences();


		this.initializeCars(null);
	}

	/**
	 * Main loop of the simulation
	 */
	public void run() {

		long currentTime = System.currentTimeMillis();
		long timePassed = 0;
		while(running) {

			if(!pausing) {
				if(especesOpen.size() != 0 && Simulation.currentTime < UserPrefs.TIME_LIMIT) {
					//Add the current time to the Simulation.currentTime
					currentTime = System.currentTimeMillis();
					Simulation.currentTime += msBetweenFrames;

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
			if(UserPrefs.REAL_TIME) {
				try {
					timePassed = System.currentTimeMillis() - currentTime;
					Simulation.sleep((long) ((msBetweenFrames - timePassed) > 0 ?msBetweenFrames - timePassed: 0));
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}else{
				try{
					//DO NOT REMOVE THIS LINE OR WILL BUG
					Simulation.sleep(0);
				}catch (Exception e){

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
			if(neuralNetworkToUse != null){
				initializeCars(neuralNetworkToUse);
			}
			else{
				initializeCars(neuralNetworkToUse);
			}
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
	 * Called when you try to load a neural network file. It resets all cars and initialize
	 * them with the new neural network
	 * @param nn The neural network to change the cars to
	 */
	public void changeNeuralNetwork(NeuralNetwork nn){
	    generation = 0;
        neuralNetworkToUse = nn;
        shouldResetAndAddEspece = true;
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

	    //Reload les préférences du user
	    UserPrefs.load();

	    //Kill all cars and add them to especesClosed
        for(int i = 0; i < especesOpen.size(); i++) {
            Espece espece = especesOpen.get(i);

            espece.kill();
            especesClosed.add(espece);
            especesOpen.remove(i);
        }

        for(Espece e : especesClosed){
        	e.resetEspece();
		}

        //Mutate the cars
        this.mutate();

        //Reset the time of the simulation
        Simulation.currentTime = 0;
    }

    private void restart(){
		this.especesOpen = new ArrayList<>();
		this.especesClosed = new ArrayList<>();
		this.generation = 1;
		this.generations = new ArrayList<>();
		Simulation.currentTime = 0;

		for(SimulationListener listener : this.simulationListeners){
			listener.simulationRestarted();
		}

		this.initializeCars(null);

	}

	/**
	 * Destroy all cars and create an array containing new cars the size of the
	 * max number of car (set by the user in settings)
	 * @param neuralNetworkToUse if you want the new cars to replicate a neural network
	 */
	private void initializeCars(NeuralNetwork neuralNetworkToUse) {
		//Reset both car arrays
        especesOpen = new ArrayList<>();
		especesClosed = new ArrayList<>();

		//Retreive the number of cars the user want
		int numberOfCar = UserPrefs.preferences.getInt(UserPrefs.KEY_NUMBER_OF_CAR, UserPrefs.DEFAULT_NUMBER_OF_CAR);

		//Add a new car until you reach the max number of car
		while(especesOpen.size() < numberOfCar) {
			especesOpen.add(new Espece(this));
		}
		
	}

	/**
	 * Start the mutation process by instantiating a NaturalSelection object
	 * from the especesClosed array and retreive the new array to especesOpen
	 */
	public void mutate() {
		NaturalSelection selection = new NaturalSelection(this, especesClosed);
		this.especesOpen = selection.getMutatedList(map);
		this.especesClosed = new ArrayList<Espece>();
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
}
