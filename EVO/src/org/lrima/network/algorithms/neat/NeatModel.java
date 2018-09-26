package org.lrima.network.algorithms.neat;

import org.lrima.network.annotations.AlgorithmInformation;
import org.lrima.network.interfaces.NeuralNetworkModel;
import org.lrima.network.interfaces.options.Option;
import org.lrima.network.interfaces.options.OptionInt;

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
