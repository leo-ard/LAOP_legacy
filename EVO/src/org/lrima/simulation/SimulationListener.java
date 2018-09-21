package org.lrima.simulation;

public interface SimulationListener {
    void onNextGeneration();
    void simulationRestarted();
    void simulationEnded();
}
