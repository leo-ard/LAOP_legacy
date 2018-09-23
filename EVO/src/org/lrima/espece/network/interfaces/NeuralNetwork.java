package org.lrima.espece.network.interfaces;

import org.apache.commons.math3.linear.RealMatrix;
import org.lrima.espece.Espece;

import java.awt.*;
import java.util.ArrayList;

public abstract class NeuralNetwork {

    protected ArrayList<?extends NeuralNetworkTransmitter> transmitters;
    protected NeuralNetworkReceiver receiver;
    protected double fitness;

    public NeuralNetwork(){

    }

    /**
     * Used when you create a NeatGenome without knowing in advance the transmitters
     * @param transmitters the transmitters this NeatGenome should use
     */
    public void setTransmitters(ArrayList<?extends NeuralNetworkTransmitter> transmitters){
        this.transmitters = transmitters;
    }

    /**
     * Used when you create a NeatGenome without knowing in advance the receiver
     * @param receiver the receiver this NeatGenome should use
     */
    public void setReceiver(NeuralNetworkReceiver receiver){
        this.receiver = receiver;
    }

    public abstract void init(ArrayList<? extends NeuralNetworkTransmitter> transmitters, NeuralNetworkReceiver receiver);
    public abstract void feedForward();
    //TODO : Change the parameter by something general (like ArrayList<NeuralNetworkReceiver>)
    //public abstract ArrayList<Espece> alterEspecesListAtGenerationFinish(ArrayList<Espece> currentBatch);
    public abstract void generationFinish();
    public abstract void draw(Graphics2D g);
    public abstract NeuralNetwork crossOver(NeuralNetwork network1, NeuralNetwork network2);
}
