package org.lrima.laop.core;

import org.lrima.laop.simulation.SimulationListener;
import org.lrima.laop.simulation.SimulationManager;
import org.lrima.laop.simulation.SimulationManagerModel;
import org.lrima.laop.simulation.SimulationModel;

public class LaopMain {

    public static void main(String[] args){
        SimulationManagerModel simulationManagerModel = new SimulationManagerModel(3);
        SimulationManager simulationManager = new SimulationManager(simulationManagerModel);

        simulationManagerModel.addBatch(new SimulationModel("Algorithm 1"));
        simulationManagerModel.addBatch(new SimulationModel("Algorithm 2"));

        simulationManager.addSimulationListener(new SimulationListener() {
            @Override
            public void simulationFinished() {
                System.out.println("Simulation finished");
            }

            @Override
            public void batchFinished() {
                System.out.println("Batch finished");

            }

            @Override
            public void simulationManagerFinished() {
                System.out.println("SimulationManager finished");

            }

            @Override
            public void dataReady() {
                System.out.println("Data ready");
            }
        });

        System.out.println("Starting the simulationManager with two algorithms. Update time is fixed at 300 ms.");

        simulationManager.start();

    }
}
