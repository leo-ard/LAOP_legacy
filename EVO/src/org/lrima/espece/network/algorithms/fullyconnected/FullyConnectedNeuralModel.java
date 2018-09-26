package org.lrima.espece.network.algorithms.fullyconnected;

import org.lrima.espece.network.annotations.AlgorithmInformation;
import org.lrima.espece.network.interfaces.NeuralNetwork;
import org.lrima.espece.network.interfaces.NeuralNetworkModel;
import org.lrima.espece.network.interfaces.options.Option;

import java.util.HashMap;
import org.lrima.espece.network.interfaces.options.*;

@AlgorithmInformation(name="Fully Connected", description = "Fully connected network with one hidden layer containing 2 neurons. All neurons are connected to each neurons in the next layer.")
public class FullyConnectedNeuralModel extends NeuralNetworkModel<FullyConnectedNeuralNetwork> {


    @Override
    public HashMap<String, Option> getDefaultOptions() {
        this.options.put("Nombre de lien", new OptionInt(10));

        return this.options;
    }

    @Override
    public Class<FullyConnectedNeuralNetwork> getNeuralNetworkClass() {
        return FullyConnectedNeuralNetwork.class;
    }
}
