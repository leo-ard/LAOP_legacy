package org.lrima.simulation;

import java.util.ArrayList;

public class SimulationInformation {
    private ArrayList<Generation> generations;

    public SimulationInformation(ArrayList<Generation> generations){
        this.generations = generations;
    }

    public ArrayList<Generation> getGenerations() {
        return generations;
    }
}
