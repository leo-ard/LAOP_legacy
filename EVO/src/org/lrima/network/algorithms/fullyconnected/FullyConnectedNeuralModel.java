package org.lrima.network.algorithms.fullyconnected;

import org.lrima.Interface.options.types.OptionInt;
import org.lrima.network.annotations.AlgorithmInformation;
import org.lrima.network.interfaces.NeuralNetworkModel;
import org.lrima.Interface.options.Option;

import java.util.LinkedHashMap;

@AlgorithmInformation(name="Fully Connected", description = "Fully connected network with one hidden layer containing 2 neurons. All neurons are connected to each neurons in the next layer.")
public class FullyConnectedNeuralModel extends NeuralNetworkModel<FullyConnectedNeuralNetwork> {
    @Override
    public LinkedHashMap<String, Option> getDefaultOptions() {
        this.options.put("NUMBER_OF_LAYERS", new OptionInt(2));
        this.options.put("DEEP_LEVEL", new OptionInt(4));

        return this.options;
    }

    @Override
    public Class<FullyConnectedNeuralNetwork> getNeuralNetworkClass() {
        return FullyConnectedNeuralNetwork.class;
    }
}
