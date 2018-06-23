package espece;

import espece.network.NetworkStructure;

import java.awt.*;

public class EspeceInfo {

    private double fitness;
    private NetworkStructure networkStructure;


    public EspeceInfo(Espece e){
        this.fitness = e.getFitness();
        this.networkStructure = new NetworkStructure(e.getNeuralNetwork());
    }

    public double getFitness() {
        return fitness;
    }

    public void setFitness(int fitness) {
        this.fitness = fitness;
    }

    public NetworkStructure getNetworkStructure() {
        return networkStructure;
    }

    public String getType(){
        int networkLength = networkStructure.getLayerSizes().length;
        int connectionLength = networkStructure.getConnectionWeight().length;
        return String.format("E%d%d", networkLength-2, connectionLength);
    }

    public Color getColor(){
        double sum = 0;
        for(double c : networkStructure.getConnectionWeight()){
            sum += c;
        }
        sum/=networkStructure.getConnectionWeight().length;
        return Color.getHSBColor(.8f, .8f, (float)sum);
    }
}
