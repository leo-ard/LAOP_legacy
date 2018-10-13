package org.lrima.simulation;

public interface BatchListener {
    void batchFinished();
    void nextSimulationInBatch();
}
