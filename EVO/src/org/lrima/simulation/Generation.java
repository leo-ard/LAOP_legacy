package org.lrima.simulation;

import org.lrima.espece.Espece;

import java.util.ArrayList;
import java.util.Collections;

public class Generation {
    //All the cars in this generation
    private final ArrayList<Espece> especes;
    private double medianFitness;
    private double moyenneFitness;
    private int generationNumber;

    public Generation(int generationNumber, ArrayList<Espece> especes){
        this.especes = new ArrayList<>(especes);
        this.generationNumber = generationNumber;

        this.calculateMedian();
        this.calculateMoyenne();
    }

    public ArrayList<Double> getAllFitnesses(){
        ArrayList<Double> fitnessList = new ArrayList<>();

        for(Espece e : this.especes){
            //if(e.getFitness() != 0) {
            fitnessList.add(e.getFitness());
            //}
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

    private void calculateMoyenne(){
        double total = 0;
        for(Espece e : this.especes){
            total += e.getFitness();
        }
        this.moyenneFitness = total / especes.size();
    }

    public int getGenerationNumber() {
        return generationNumber;
    }

    public double getMedianFitness() {
        return medianFitness;
    }

    public double getMoyenneFitness() {
        return moyenneFitness;
    }
}
