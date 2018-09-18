package org.lrima.espece.network.interfaces;

import org.apache.commons.math3.linear.RealMatrix;

import java.awt.*;
import java.util.ArrayList;

//TODO: Devenir une classe abstraite
public interface NeuralNetwork {
    RealMatrix query(double[] inputs);
    void feedForward();
    void mutate();
    void draw(Graphics2D g);
    void init(ArrayList<? extends NeuralNetworkTransmitter> transmitters, NeuralNetworkReceiver receiver);
    NeuralNetwork crossOver(NeuralNetwork network1, NeuralNetwork network2);
    void setTransmitters(ArrayList<?extends NeuralNetworkTransmitter> transmitters);
    void setReceiver(NeuralNetworkReceiver receiver);
}
