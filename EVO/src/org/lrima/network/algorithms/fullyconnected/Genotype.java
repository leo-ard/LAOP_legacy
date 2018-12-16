package org.lrima.network.algorithms.fullyconnected;

import org.lrima.network.functions.Function;
import org.lrima.utils.Random;

public class Genotype {
    private double[] genomes;
    private int[] topology;
    private int[] weightSize;

    public Genotype(int[] topology) {

        //Calculate the the number of weight of each layer
        int numberOfWeight = 0;
        weightSize = new int[topology.length];
        weightSize[0] = 0;
        for(int i = 1; i < topology.length; i++){
            numberOfWeight += topology[i-1] * topology[i];
            weightSize[i] = numberOfWeight;
        }

        genomes = new double[numberOfWeight];
        for(int i = 0; i < genomes.length; i++){
            genomes[i] = Random.getRandomDoubleValue(-5.0,5.0);
        }

        this.topology = topology;
    }

    private Genotype(double[] genomes, int[] topology, int[] weightSize){
        this.genomes = genomes;
        this.topology = topology;
        this.weightSize = weightSize;
    }

    public void mutate(double mutationChance, double weightChangeChance){
        double mutationChanceRandom = Random.getRandomDoubleValue(1);
        if(mutationChanceRandom < mutationChance) {
            for (int i = 0; i < genomes.length; i++) {
                double weightChangeChanceRandom = Random.getRandomDoubleValue(1.0);
                if (weightChangeChanceRandom < weightChangeChance) {
                    genomes[i] += Random.getRandomDoubleValue(-0.01, 0.01);
                }
            }
        }
    }

    public Genotype getChildren(Genotype parent2Object){
        double[] parent1 = this.genomes;
        double[] parent2 = parent2Object.genomes;

        //Checks for the meaning of life and death
        if(parent1.length != parent2.length){
            System.err.println("THIS IS THE BIGGEST MISTAKE THAT YOU WILL EVER MAKE ! (I know thats its not professionnal, but -.- )");
            return null;
        }

        double[] newGenotype = new double[parent1.length];

        for(int i = 0; i < newGenotype.length; i++){
            newGenotype[i] = Random.getRandomBoolean() ? parent1[i] : parent2[i];
            if(Random.getRandomIntegerValue(100 ) < 1){
                newGenotype[i] += Random.getRandomDoubleValue(-1, 1);
            }
        }

        return new Genotype(newGenotype, topology, weightSize);
    }

    /**
     * Get the weights according to the receiving layer
     *
     * @param layer, cannot be 0 because the layer 0 doesnt receive any
     * @return the weights
     */
    public double[] getSubsetForLayer(int layer){
        if(layer <= 0 || layer > this.topology.length-1) {
            System.err.println("Cannot retrieve subset");
            return null;
        }

        return getSubset(weightSize[layer-1], weightSize[layer]);
    }

    private double[] getSubset(int min, int max){
        double[] sub = new double[max-min];
        for(int i = min; i < max; i++)
            sub[i-min] = this.genomes[i];

        return sub;
    }

    public double[] toArray() {
        return this.genomes;
    }
}
