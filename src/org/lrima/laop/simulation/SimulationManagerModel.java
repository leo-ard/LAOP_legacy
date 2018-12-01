package org.lrima.laop.simulation;

import java.util.ArrayList;

public class SimulationManagerModel {
    private ArrayList<SimulationModel> simulationModels;
    private int maxSimulation;
    //HashMap<String, Option> options;

    /**
     *
     * @param maxSimulation the simulation's maximum per batch
     */
    public SimulationManagerModel(int maxSimulation){
        simulationModels = new ArrayList<>();
        this.maxSimulation = maxSimulation;
    }

    /**
     * @param batchCount the batch number
     * @return the simulationModel in function of the batchNumber
     */
    public SimulationModel getSimulationModel(int batchCount) {
        return simulationModels.get(batchCount);
    }

    public int getMaxBatch() {
        return simulationModels.size();
    }

    public int getMaxSimulation() {
        return maxSimulation;
    }

    /**
     * Adds a new batch that will have the simulationModel as model
     *
     * @param simulationModel the simulationModel to add
     */
    public void addBatch(SimulationModel simulationModel){
        this.simulationModels.add(simulationModel);
    }
}
