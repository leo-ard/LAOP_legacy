package org.lrima.simulation.selection;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;

import org.lrima.core.UserPrefs;
import org.lrima.espece.Espece;
import org.lrima.espece.network.NeuralNetwork;
import org.lrima.map.Map;
import org.lrima.simulation.FrameManager;
import org.lrima.simulation.Simulation;
import org.lrima.utils.Random;

public class  NaturalSelection {
	ArrayList<Espece> especes;
	public static Espece best = null;
	
	public NaturalSelection(ArrayList<Espece> especes) {
		this.especes = especes;
	}
	
	public void sort() {
		especes.sort(new Comparator<Espece>() {
			@Override
			public int compare(Espece e1, Espece e2) {
				return (int)(e2.getFitness() - e1.getFitness());
			}
		});
	}
	
	public ArrayList<Espece> getMutatedList(Map m) {
		this.sort();
		this.getBest();
		FrameManager.addGeneration(especes);
		this.kill50();
		//this.mutateBest();
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

		while(especes.size() < numberOfCar) {
			NeuralNetwork newNeuralNetwork = best.getNeuralNetwork();
			newNeuralNetwork.randomize();

			newNeuralNetwork = new NeuralNetwork(6, 2, true);

			Espece e = new Espece(m, newNeuralNetwork);
			e.bornOnGeneration = m.simulation.getGeneration();
			especes.add(e);
		}
		
	}

	/**
	 * Chance to change something on the neural network of the best cars
	 */
	private void mutateBest(){
		for(Espece e : especes){
			e.mutate();
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
	}

	/**
	 * Teleport all cars to spawn
	 * @param m la map
	 */
	public void resetList(Map m) {
		for(Espece e : especes){
			e.tpLikeNew(m.depart, m.orientation);
		}
	}
}
