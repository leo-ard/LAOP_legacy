package org.lrima.simulation;

import org.lrima.Interface.FrameManager;
import org.lrima.Interface.conclusion.ConclusionFrame;
import org.lrima.network.interfaces.NeuralNetworkModel;

import javax.swing.*;
import java.util.ArrayList;

public class SimulationManager implements BatchListener {
    //the simulation and its information
    private ArrayList<SimulationBatch> simulationBatches = new ArrayList<>();
    private int currentBatch = 0;
    private FrameManager frameManager;

    public void restart(){
        ArrayList<SimulationBatch> newArray = new ArrayList<>();
        getCurrentSimulation().terminate();

        simulationBatches.forEach(simulationBatch -> {
            //TODO : ressemble beaucoup à addBash() (optimisation)
            SimulationBatch newBatch = new SimulationBatch(simulationBatch.getAlgorithmModel(), simulationBatch.getBatchSize());
            newArray.add(newBatch);
            newBatch.addBatchListener(this);
        });
        simulationBatches = newArray;
        currentBatch = 0;

        start();
    }

    @Override
    public void batchFinished() {
        if(currentBatch + 1 >= this.simulationBatches.size()) {
            allSimulationFinish();
        }
        else {
            this.currentBatch++;
            this.startBatch();
        }
    }

    private void allSimulationFinish(){
        frameManager.dispose();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                ConclusionFrame conclusionFrame = new ConclusionFrame(simulationBatches);
                conclusionFrame.setVisible(true);
            }
        });
    }

    public Simulation getCurrentSimulation(){
        if(currentBatch >= this.simulationBatches.size())
            System.err.println("CURRENT SIMULATION IS CALLED EVEN AFTER THE SIMULATION IS FINISHED");
        return simulationBatches.get(currentBatch).getCurrentSimulation();
    }

    private SimulationBatch getCurrentBatch(){
        if(currentBatch >= this.simulationBatches.size())
            System.err.println("CURRENT BATCH IS CALLED EVEN AFTER THE SIMULATION IS FINISHED");
        return simulationBatches.get(currentBatch);
    }

    /**
     * Sets the algorithm to use. It creates a new simulation and resets all the panels using the simulation
     *
     * @param algorithm the algorithm to use
     */
    public void addBatch(NeuralNetworkModel algorithm, int numberInBatch) {
        SimulationBatch simulationBatch = new SimulationBatch(algorithm, numberInBatch);
        this.simulationBatches.add(simulationBatch);
        simulationBatch.addBatchListener(this);
    }

    private void startBatch() {
        simulationBatches.get(currentBatch).startBatch();
    }

    public void start(){
        this.startBatch();
    }

    public String getCurrentInfos() {
        return ("Algorithm: " + getCurrentSimulation().getAlgorithm().getAlgorithmInformationAnnotation().name() + " ("+ currentBatch +" / " + getMaxBatches() +")" +
            "\nSimulation " + (getCurrentBatch().getCurrentSimulationIndex() + 1) + " / " + getCurrentBatch().getSimulations().length +
            "\nGeneration: " + getCurrentSimulation().getGeneration() + " / " + getCurrentSimulation().getMaxGenerations() +
            "\nTime: " + getCurrentSimulation().simulationTime / 1000);
    }

    private int getMaxBatches (){
        return simulationBatches.size();
    }

    public void setFrameManager(FrameManager frameManager) {
        this.frameManager = frameManager;
    }

    public void addSimulationListener(SimulationListener simulationListener){
        this.simulationBatches.forEach(simulationBatch -> simulationBatch.addSimulationListener(simulationListener));
    }
}
