package org.lrima.simulation;

import org.lrima.espece.Espece;

import java.util.ArrayList;
import java.util.Collections;

public class GenerationInformation {
    //All the cars in this generation
    private ArrayList<Espece> especes;
    private double medianFitness;

    public GenerationInformation(ArrayList<Espece> especes){
        this.especes = especes;
        this.calculateMedian();
    }

    public ArrayList<Double> getAllFitnesses(){
        ArrayList<Double> fitnessList = new ArrayList<>();

        for(Espece e : this.especes){
            fitnessList.add(e.getFitness());
        }

        return fitnessList;
    }

    /**
     * Sets the value of the median fitness score
     */
    private void calculateMedian(){
        //Start by sorting the cars by their fitness score
        Collections.sort(this.especes);

        //Get the car in the middle of the list
        Espece medianEspece = this.especes.get((int)(especes.size() / 2));

        this.medianFitness = medianEspece.getFitness();
    }
}
