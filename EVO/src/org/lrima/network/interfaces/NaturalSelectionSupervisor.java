package org.lrima.network.interfaces;

import org.lrima.core.UserPrefs;
import org.lrima.espece.Espece;
import org.lrima.simulation.Simulation;
import org.lrima.utils.Random;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.function.Consumer;

public class NaturalSelectionSupervisor implements NeuralNetworkSuperviser {
    private Espece best = null;
    private ArrayList<Espece> halfBestEspece = new ArrayList<>();

    @Override
    public ArrayList<Espece> alterEspeceListAtGenerationFinish(ArrayList<Espece> especes, Simulation simulation) {
        best = especes.get(0);
        this.kill50(especes);
        this.repopulate(especes, simulation);

        return especes;
    }

    /**
     * Kill half of the cars to keep the best half
     */
    private void kill50(ArrayList<Espece> especes) {
        int numberOfCar = UserPrefs.getInt(UserPrefs.KEY_NUMBER_OF_CAR);

        while(especes.size() > numberOfCar/2) {
            especes.remove(especes.size() - 1);
        }

        this.halfBestEspece = new ArrayList<>(especes);
    }

    /**
     * Create the cars that was destroyed in kill50 to always keep the same number of cars
     */
    private void repopulate(ArrayList<Espece> especes, Simulation simulation ) {
        int numberOfCar = UserPrefs.getInt(UserPrefs.KEY_NUMBER_OF_CAR);

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

            Espece e = new Espece(simulation);
            NeuralNetwork childNeuralNetwork = neuralNetworkParent1.crossOver(neuralNetworkParent1, neuralNetworkParent2);
            e.setNeuralNetwork(childNeuralNetwork);

            e.getNeuralNetwork().generationFinish();

            newCars.add(e);
        }
        especes.addAll(newCars);

        for(Espece e : especes){
            e.setFitness(0.0);
        }
    }
}
