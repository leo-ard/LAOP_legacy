package org.lrima.network.supervisors;

import org.lrima.espece.Espece;
import org.lrima.network.interfaces.NeuralNetwork;
import org.lrima.network.interfaces.NeuralNetworkModel;
import org.lrima.network.interfaces.NeuralNetworkSuperviser;
import org.lrima.simulation.Simulation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.function.Predicate;
import java.util.stream.Collector;

public class OtherSupervisor implements NeuralNetworkSuperviser {
    ArrayList<Espece> bestPerformingOnes = new ArrayList<>();
    final double pbestTaken = 10;
    final double pmiddle = 0.2;


    @Override
    public ArrayList<Espece> alterEspeceListAtGenerationFinish(ArrayList<Espece> especes, Simulation simulation) {
        NeuralNetworkModel model = simulation.getAlgorithm();
        Collections.sort(especes);

        System.out.println(especes.get(0).getFitness());

        this.kill(especes, model);
        this.repopulate(especes, model, simulation);


        return especes;
    }

    private void repopulate(ArrayList<Espece> especes, NeuralNetworkModel model, Simulation simulation) {
        int numberOfCar = (int) model.getSimulationOption(NeuralNetworkModel.KEY_NB_CARS);
        int numberOfSensors = (int) model.getSimulationOption(NeuralNetworkModel.KEY_NB_SENSORS);

        double mutationChance = (double) model.getGeneticOption(NeuralNetworkModel.KEY_MUTATION_CHANCE);
        double weightModifChance = (double) model.getGeneticOption(NeuralNetworkModel.KEY_WEIGHT_MODIFICATION_CHANCE);



        while(especes.size() < numberOfCar){
            Collections.sort(bestPerformingOnes);

            int randomPick = (int) (Math.random() * pbestTaken);
            int randomPick2 = (int) (Math.random() * pbestTaken);

            //check that there are not the same
            while(randomPick == randomPick2){
                randomPick2 = (int) (Math.random() * pbestTaken);
            }

            Espece e = new Espece(simulation);
            e.setNumberSensor(numberOfSensors);

            NeuralNetwork neuralNetworkParent1 = bestPerformingOnes.get(randomPick).getNeuralNetwork();
            NeuralNetwork neuralNetworkParent2 = bestPerformingOnes.get(randomPick2).getNeuralNetwork();


            NeuralNetwork childNeuralNetwork = neuralNetworkParent1.crossOver(neuralNetworkParent1, neuralNetworkParent2);
            childNeuralNetwork.setMutationChance(mutationChance);
            childNeuralNetwork.setWeightModificationChance(weightModifChance);

            childNeuralNetwork.mutate();
            e.setNeuralNetwork(childNeuralNetwork);

            especes.add(e);
        }
    }

    private void kill(ArrayList<Espece> especes, NeuralNetworkModel model) {
        int numberOfCar = (int) model.getSimulationOption(NeuralNetworkModel.KEY_NB_CARS);
        for(int i = 0; i < pbestTaken; i++){
            if(!bestPerformingOnes.contains(especes.get(i)))
            bestPerformingOnes.add(especes.get(i));
        }
        System.out.println(especes.size());
        double capFitness = especes.get((int)(pmiddle * numberOfCar)).getFitness();
        System.out.println(capFitness);
        especes.removeIf(espece -> espece.getFitness() < capFitness);
        System.out.println(especes.size());








    }
}
