package org.lrima.simulation;

import org.lrima.espece.network.interfaces.NeuralNetwork;

import java.util.ArrayList;

public class SimulationBatch implements SimulationListener {
    private Simulation[] simulations;
    private int currentSimulation = 0;
    private int numberInBatch;

    private ArrayList<BatchListener> batchListeners = new ArrayList<>();

    public SimulationBatch(Class<?extends NeuralNetwork> algorithm, int numberInBatch){
        this.simulations = new Simulation[numberInBatch];
        this.numberInBatch = numberInBatch;

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


}
