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

    public double getMaxFitness(){
        double max = 0.0;

        for(Generation generation : this.generations){
            if(generation.getMoyenneFitness() > max){
                max = generation.getMoyenneFitness();
            }
        }

        return max;
    }

    public double getMinFitness(){
        double min = 0.0;

        for(Generation generation : this.generations){
            if(generation.getMoyenneFitness() < min){
                min = generation.getMoyenneFitness();
            }
        }

        return 0.0;
    }
}
