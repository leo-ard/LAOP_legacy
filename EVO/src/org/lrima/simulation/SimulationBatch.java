package org.lrima.simulation;

import org.lrima.espece.network.interfaces.NeuralNetwork;

import java.util.ArrayList;
import java.util.HashMap;

public class SimulationBatch implements SimulationListener {
    private Simulation[] simulations;
    private int currentSimulation = 0;
    private int numberInBatch;
    private Class<?extends NeuralNetwork> algorithm;
    private ArrayList<SimulationInformation> simulationInformations = new ArrayList<>();

    private ArrayList<BatchListener> batchListeners = new ArrayList<>();

    public SimulationBatch(Class<?extends NeuralNetwork> algorithm, int numberInBatch){
        this.simulations = new Simulation[numberInBatch];
        this.numberInBatch = numberInBatch;
        this.algorithm = algorithm;

        for(int i = 0 ; i < simulations.length ; i++){
            simulations[i] = new Simulation(algorithm);
            simulations[i].addSimulationListener(this);
        }
    }

    public void startBatch(){
        simulations[0].start();
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
        Class<?extends NeuralNetwork> algorithm = this.getAlgorithm();

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

    public Class<? extends NeuralNetwork> getAlgorithm() {
        return algorithm;
    }

    public ArrayList<SimulationInformation> getSimulationInformations() {
        return simulationInformations;
    }
}
