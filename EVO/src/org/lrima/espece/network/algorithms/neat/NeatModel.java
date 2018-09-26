package org.lrima.espece.network.algorithms.neat;

import org.lrima.espece.network.annotations.AlgorithmInformation;
import org.lrima.espece.network.interfaces.NeuralNetwork;
import org.lrima.espece.network.interfaces.NeuralNetworkModel;
import org.lrima.espece.network.interfaces.options.Option;
import org.lrima.espece.network.interfaces.options.OptionInt;

import java.util.HashMap;


@AlgorithmInformation(name="NEAT Algorithm", description = "Neural Network using the NEAT algorithm. The network starts with one connections from random inputs that goes into one of the outputs. With each generations, the network grows to try to find the best solution.")
public class NeatModel extends NeuralNetworkModel<NeatGenome> {
    protected Class<NeatGenome> neuralNetworkClass = NeatGenome.class;

    @Override
    public HashMap<String, Option> getDefaultOptions() {
        options.put("options 1", new OptionInt(10));
        return this.options;
    }

    @Override
    public Class getNeuralNetworkClass() {
        return NeatGenome.class;
    }
}
