package org.lrima.espece.network.interfaces;

import org.apache.commons.math3.linear.RealMatrix;

import java.awt.*;
import java.util.ArrayList;

public interface NeuralNetwork {
    public RealMatrix query(double[] inputs);
    public void feedForward();
    public void mutate();
    public void minimalMutation();
    public void draw(Graphics2D g);
    public void init(ArrayList<? extends NeuralNetworkTransmitter> transmitters, NeuralNetworkReceiver receiver);
}
