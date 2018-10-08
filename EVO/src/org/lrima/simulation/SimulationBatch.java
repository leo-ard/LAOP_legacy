package org.lrima.simulation;

import org.lrima.network.interfaces.NeuralNetworkModel;

import java.util.ArrayList;
import java.util.Queue;

public class SimulationBatch implements SimulationListener {
    private Simulation[] simulations;
    private int currentSimulation = 0;
    private int numberInBatch;
    private NeuralNetworkModel algorithmModel;
    private ArrayList<SimulationInformation> simulationInformations = new ArrayList<>();

    private ArrayList<BatchListener> batchListeners = new ArrayList<>();

    private final int maxGeneration = 5;
    private ArrayList<SimulationListener> simulationListeners = new ArrayList<>();

    public SimulationBatch(NeuralNetworkModel algorithmModel, int numberInBatch){
        this.simulations = new Simulation[numberInBatch];
        this.numberInBatch = numberInBatch;
        this.algorithmModel = algorithmModel;

        for(int i = 0 ; i < simulations.length ; i++){
            simulations[i] = new Simulation(algorithmModel, maxGeneration);
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

        //the limit is hit
        if(currentSimulation + 1 >= numberInBatch ) {
            this.batchListeners.forEach(BatchListener::batchFinished);
        }
        else{
            this.currentSimulation++;
            this.getCurrentSimulation().start();
        }
    }

    private void addSimulationInformation(){
        SimulationInformation information = new SimulationInformation(this.getCurrentSimulation().getGenerations());
        this.simulationInformations.add(information);
    }

    @Override
    public void onNextGeneration() {
        this.simulationListeners.forEach(SimulationListener::onNextGeneration);
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

    public int getBatchSize() {
        return this.numberInBatch;
    }

    public void addSimulationListener(SimulationListener simulationListener) {
        simulationListeners.add(simulationListener);
    }
}
