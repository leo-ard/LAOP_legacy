package org.lrima.simulation;

import org.lrima.espece.network.interfaces.NeuralNetwork;
import org.lrima.espece.network.interfaces.NeuralNetworkModel;

import java.util.ArrayList;

public class SimulationBatch implements SimulationListener {
    private Simulation[] simulations;
    private int currentSimulation = 0;
    private int numberInBatch;
    private NeuralNetworkModel algorithmModel;
    private ArrayList<SimulationInformation> simulationInformations = new ArrayList<>();

    private ArrayList<BatchListener> batchListeners = new ArrayList<>();

    public SimulationBatch(NeuralNetworkModel algorithmModel, int numberInBatch){
        this.simulations = new Simulation[numberInBatch];
        this.numberInBatch = numberInBatch;
        this.algorithmModel = algorithmModel;

        for(int i = 0 ; i < simulations.length ; i++){
            simulations[i] = new Simulation(algorithmModel);
            simulations[i].addSimulationListener(this);
        }
    }

    public void startBatch(){
        simulations[0].start();
    }


    public double[] getAverageFitnessPerGeneration(){
        double[] fitnesses = new double[this.simulations[0].getGenerationList().size()];

        for(int generation = 0 ; generation < this.simulations[0].getGenerationList().size() ; generation++){
            int simulationIndex = 0;
            for(Simulation simulation : this.simulations){
                fitnesses[generation] = fitnesses[generation] + simulation.getGenerationList().get(generation).getMoyenneFitness();
                simulationIndex++;
            }
            fitnesses[generation] = fitnesses[generation] / (simulationIndex + 1);
        }

        return fitnesses;
    }


    public Simulation[] getSimulations() {
        return simulations;
    }

    public Simulation getCurrentSimulation(){
        return this.simulations[currentSimulation];
    }

    public int getCurrentSimulationIndex(){
        return this.currentSimulation;
    }

    @Override
    public void simulationEnded() {
        //Add the information
        this.addSimulationInformation();

        getCurrentSimulation().terminate();

        //Starts the next simulation
        if(currentSimulation < numberInBatch - 1) {
            this.currentSimulation++;
            this.getCurrentSimulation().start();
        }
        else{
            this.batchEnd();
        }
    }

    private void addSimulationInformation(){

        SimulationInformation information = new SimulationInformation(this.getCurrentSimulation().getGenerations());
        this.simulationInformations.add(information);
    }

    private void batchEnd(){
        for(BatchListener listener : this.batchListeners){
            listener.batchFinished();
        }
    }

    @Override
    public void onNextGeneration() {

    }

    @Override
    public void simulationRestarted() {

    }

    public void addBatchListener(BatchListener listener){
        this.batchListeners.add(listener);
    }

    public NeuralNetworkModel getAlgorithmModel() {
        return algorithmModel;
    }

    public ArrayList<SimulationInformation> getSimulationInformations() {
        return simulationInformations;
    }
}
