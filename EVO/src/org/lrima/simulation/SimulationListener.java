package org.lrima.simulation;

public interface SimulationListener {
    void onNextGeneration();

    void simulationEnded();
}
