package org.lrima.network.algorithms.improved_neat;

import org.lrima.network.annotations.AlgorithmInformation;
import org.lrima.network.interfaces.NeuralNetworkModel;
import org.lrima.Interface.options.Option;
import org.lrima.Interface.options.types.OptionInt;

import java.util.LinkedHashMap;

@AlgorithmInformation(name="Improved NEAT", description = "Neural Network using a modified NEAT algorithm. The network starts with two connections from random inputs that goes into the two outputs. With each generations, the network grows to try to find the best solution.")
public class ImprovedNeatModel extends NeuralNetworkModel<ImprovedNeatGenome> {
    @Override
    public LinkedHashMap<String, Option> getDefaultOptions() {
        options.put("options 1", new OptionInt(10));
        return this.options;
    }

    @Override
    public Class<ImprovedNeatGenome> getNeuralNetworkClass() {
        return ImprovedNeatGenome.class;
    }
}
