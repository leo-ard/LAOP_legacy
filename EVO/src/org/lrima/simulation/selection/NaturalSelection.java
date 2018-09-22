package org.lrima.simulation.selection;

import java.util.ArrayList;
import java.util.Collections;

import org.lrima.core.UserPrefs;
import org.lrima.espece.Espece;
import org.lrima.espece.network.interfaces.NeuralNetwork;
import org.lrima.map.Map;
import org.lrima.simulation.Simulation;
import org.lrima.utils.Random;

public class  NaturalSelection {
	private ArrayList<Espece> especes;
	private static Espece best = null;
	private Simulation simulation;
	private ArrayList<Espece> halfBestEspece = new ArrayList<>();
	
	public NaturalSelection(Simulation simulation, ArrayList<Espece> especes) {
		this.simulation = simulation;
		this.especes = especes;
	}
	
	public ArrayList<Espece> getMutatedList(Map m) {
		Collections.sort(this.especes);
		this.getBest();
		this.kill50();
		//this.mutate();
		this.resetList(m);
		this.repopulate(m);

		return especes;



	}

	private void getBest(){
	    best = especes.get(0);
    }

	/**
	 * Create the cars that was destroyed in kill50 to always keep the same number of cars
	 * @param m the map
	 */
	private void repopulate(Map m) {

		int numberOfCar = UserPrefs.preferences.getInt(UserPrefs.KEY_NUMBER_OF_CAR, UserPrefs.DEFAULT_NUMBER_OF_CAR);

		ArrayList<Espece> newCars = new ArrayList<>();

		while(especes.size() + newCars.size() < numberOfCar) {
			int randomParent1 = Random.getRandomIntegerValue(halfBestEspece.size() - 1);
			int randomParent2;

			//Select two parrents
			NeuralNetwork neuralNetworkParent1 = best.getNeuralNetwork();
			NeuralNetwork neuralNetworkParent2;
			do {
				randomParent2 = Random.getRandomIntegerValue(halfBestEspece.size() - 1);
				neuralNetworkParent2 = halfBestEspece.get(randomParent2).getNeuralNetwork();
			}while(randomParent1 == randomParent2);

			Espece e = new Espece(this.simulation);
			NeuralNetwork childNeuralNetwork = neuralNetworkParent1.crossOver(neuralNetworkParent1, neuralNetworkParent2);
			e.setNeuralNetwork(childNeuralNetwork);

			e.getNeuralNetwork().mutate();

			newCars.add(e);
		}
		this.especes.addAll(newCars);

		for(Espece e : this.especes){
			e.setFitness(0.0);
			//e.getNeuralNetwork().mutate();
		}
	}

	/**
	 * Kill half of the cars to keep the best half
	 */
	private void kill50() {
        int numberOfCar = UserPrefs.preferences.getInt(UserPrefs.KEY_NUMBER_OF_CAR, UserPrefs.DEFAULT_NUMBER_OF_CAR);

		while(especes.size() > numberOfCar/2) {
			especes.remove(especes.size() - 1);
		}

		this.halfBestEspece = new ArrayList<>(especes);
	}

	/**
	 * Teleport all cars to spawn
	 * @param m la map
	 */
	private void resetList(Map m) {
		for(Espece e : especes){
			e.tpLikeNew();
		}
	}
}
