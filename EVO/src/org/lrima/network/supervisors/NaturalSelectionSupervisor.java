package org.lrima.network.supervisors;

import org.knowm.xchart.*;
import org.lrima.core.UserPrefs;
import org.lrima.espece.Espece;
import org.lrima.network.interfaces.NeuralNetwork;
import org.lrima.network.interfaces.NeuralNetworkSuperviser;
import org.lrima.simulation.Simulation;
import org.lrima.utils.Random;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.function.Consumer;

public class NaturalSelectionSupervisor implements NeuralNetworkSuperviser {
    private Espece best = null;
    private ArrayList<Espece> halfBestEspece = new ArrayList<>();

    /**
     * Used to display a chart of the survived cars comparing to the array before elimination
     * @param args
     */
    public static void main(String[] args) {
        ArrayList<Espece> especes = new ArrayList<>();
        Simulation simulation = new Simulation(null);

        int nbEspece = 100;
        for(int i = 0 ; i < nbEspece ; i++){
            especes.add(new Espece(simulation));
        }
        //Random fitness
        for(Espece e : especes){
            e.setFitness(Random.getRandomDoubleValue(0, 200));
        }

        //0: best
        //99: worst
        Collections.sort(especes);

        //assign weight depending on position
        HashMap<Espece, Double> weightedEspece = new HashMap<>();
        for(int i = 0 ; i < especes.size() ; i++){
            weightedEspece.put(especes.get(i),  calculateWeight(i, especes));
        }

        HashMap<Espece, Double> especesBeforeKill = new HashMap<>(weightedEspece);

        do {
            double allWeights = 0;
            for(Espece e : weightedEspece.keySet()){
                double weight = weightedEspece.get(e);
                allWeights += weight;
            }

            double randomWeight = Random.getRandomDoubleValue(1.0, allWeights);
            Espece selected = null;

            for (Espece e : weightedEspece.keySet()) {
                randomWeight -= weightedEspece.get(e);
                if (randomWeight <= 0) {
                    weightedEspece.remove(e);
                    break;
                }
            }
        }while(weightedEspece.size() > nbEspece / 2);

        ArrayList<Espece> oldEspece = new ArrayList<>(especesBeforeKill.keySet());
        Collections.sort(oldEspece);

        ArrayList<Espece> survivedEspece = new ArrayList<>(weightedEspece.keySet());
        Collections.sort(survivedEspece);

        double[] xOldData = new double[oldEspece.size()];
        double[] yOldData = new double[oldEspece.size()];

        int i = 0;
        for(Espece e : oldEspece){
            xOldData[i] = i;
            yOldData[i] = e.getFitness();
            i++;
        }

        double[] xNewData = new double[survivedEspece.size()];
        double[] yNewData = new double[survivedEspece.size()];

        i = 0;
        for(Espece e : survivedEspece){
            int xCoord = 0;
            for(Espece e2 : oldEspece){
                if(e2.equals(e)){
                    xCoord = oldEspece.indexOf(e2);
                }
            }

            xNewData[i] = xCoord;
            yNewData[i] = e.getFitness();
            i++;
        }

        // Create Chart
        XYChart chart = new XYChartBuilder().width(800).height(600).build();
        chart.setXAxisTitle("Car index");
        chart.setYAxisTitle("Fitness");
        chart.getStyler().setDefaultSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Scatter);
        chart.getStyler().setMarkerSize(16);

        chart.addSeries("Before elimination", xOldData, yOldData);
        chart.addSeries("Cars that survived", xNewData, yNewData);

        try {
            BitmapEncoder.saveBitmapWithDPI(chart, "./cars_eliminated.png", BitmapEncoder.BitmapFormat.PNG, 300);
        }catch(Exception e){
            e.printStackTrace();
        }

        // Show it
        new SwingWrapper(chart).displayChart();
    }

    private static double calculateWeight(int index, ArrayList<Espece> array){

        double b = Math.pow(1.0/99.0, 1.0/(double)(array.size()));
        double c = -(Math.log(99)/Math.log(b));

        return Math.pow(b, -(double)index - c + (array.size() - 1));
    }

    @Override
    public ArrayList<Espece> alterEspeceListAtGenerationFinish(ArrayList<Espece> especes, Simulation simulation) {
        Collections.sort(especes);
        best = especes.get(0);
        this.kill50(especes);
        this.repopulate(especes, simulation);

        return especes;
    }

    /**
     * Kill half of the cars to keep the best half
     */
    private void kill50(ArrayList<Espece> especes) {
        int numberOfCar = UserPrefs.getInt(UserPrefs.KEY_NUMBER_OF_CAR);

        //assign weight depending on position
        HashMap<Espece, Double> weightedEspece = new HashMap<>();
        for(int i = 0 ; i < especes.size() ; i++){
            weightedEspece.put(especes.get(i),  calculateWeight(i, especes));
        }

        //Kill 50% of the cars
        do {
            double allWeights = 0;
            for(Espece e : weightedEspece.keySet()){
                double weight = weightedEspece.get(e);
                allWeights += weight;
            }

            double randomWeight = Random.getRandomDoubleValue(1.0, allWeights);
            Espece selected = null;

            for (Espece e : weightedEspece.keySet()) {
                randomWeight -= weightedEspece.get(e);
                if (randomWeight <= 0) {
                    weightedEspece.remove(e);
                    break;
                }
            }
        }while(weightedEspece.size() > numberOfCar / 2);

        especes = new ArrayList<>(weightedEspece.keySet());
        Collections.sort(especes);
    }

    /**
     * Create the cars that was destroyed in kill50 to always keep the same number of cars
     */
    private void repopulate(ArrayList<Espece> especes, Simulation simulation ) {
        int numberOfCar = UserPrefs.getInt(UserPrefs.KEY_NUMBER_OF_CAR);
        this.halfBestEspece = new ArrayList<>(especes);
        ArrayList<Espece> newCars = new ArrayList<>();

        while(especes.size() + newCars.size() < numberOfCar) {
            int randomParent1 = Random.getRandomIntegerValue(halfBestEspece.size() - 1);
            int randomParent2;

            //Select two parrents
            NeuralNetwork neuralNetworkParent1 = halfBestEspece.get(randomParent1).getNeuralNetwork();
            NeuralNetwork neuralNetworkParent2;
            do {
                randomParent2 = Random.getRandomIntegerValue(halfBestEspece.size() - 1);
                neuralNetworkParent2 = halfBestEspece.get(randomParent2).getNeuralNetwork();
            }while(randomParent1 == randomParent2);

            Espece e = new Espece(simulation);
            NeuralNetwork childNeuralNetwork = neuralNetworkParent1.crossOver(neuralNetworkParent1, neuralNetworkParent2);
            e.setNeuralNetwork(childNeuralNetwork);

            e.getNeuralNetwork().generationFinish();
            newCars.add(e);
        }
        especes.addAll(newCars);

        for(Espece e : especes){
            e.setFitness(0.0);
            //e.getNeuralNetwork().generationFinish();
        }
    }
}
