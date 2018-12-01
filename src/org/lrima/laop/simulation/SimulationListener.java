package org.lrima.laop.simulation;

public interface SimulationListener {
    void simulationFinished();
    void batchFinished();
    void simulationManagerFinished();

    //TODO : implements this one
    void dataReady();
}
