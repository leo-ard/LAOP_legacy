package org.lrima.espece.network.interfaces;

import org.apache.commons.math3.linear.RealMatrix;

import java.awt.*;
import java.util.ArrayList;

public interface NeuralNetwork {
    RealMatrix query(double[] inputs);
    void feedForward();
    void mutate();
    void minimalMutation(double delta);
    void draw(Graphics2D g);
    void init(ArrayList<? extends NeuralNetworkTransmitter> transmitters, NeuralNetworkReceiver receiver);
}
