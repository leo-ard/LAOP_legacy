package org.lrima.network.algorithms.manual_algorithm;

import org.lrima.Interface.options.Option;
import org.lrima.network.annotations.AlgorithmInformation;
import org.lrima.network.interfaces.NeuralNetwork;
import org.lrima.network.interfaces.NeuralNetworkReceiver;
import org.lrima.network.interfaces.NeuralNetworkTransmitter;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;


public class ManualAlgorithm extends NeuralNetwork {

    public ManualAlgorithm(LinkedHashMap<String, Option> options) {
        super(options);
    }

    @Override
    public void init(ArrayList<? extends NeuralNetworkTransmitter> transmitters, NeuralNetworkReceiver receiver) {

    }

    @Override
    public void feedForward() {

    }

    @Override
    public void generationFinish() {

    }

    @Override
    public void draw(Graphics2D g, Dimension panelDimensions) {

    }

    @Override
    public NeuralNetwork crossOver(NeuralNetwork network1, NeuralNetwork network2) {
        return null;
    }
}
