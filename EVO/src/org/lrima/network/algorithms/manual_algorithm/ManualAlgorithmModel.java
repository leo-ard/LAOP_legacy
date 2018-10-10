package org.lrima.network.algorithms.manual_algorithm;

import org.lrima.Interface.options.Option;
import org.lrima.Interface.options.types.OptionInt;
import org.lrima.network.annotations.AlgorithmInformation;
import org.lrima.network.interfaces.NeuralNetworkModel;

import java.util.LinkedHashMap;

@AlgorithmInformation(name="Manual Algorithm", description = "Manual implementation of how the car should move")
public class ManualAlgorithmModel extends NeuralNetworkModel<ManualAlgorithm> {
    @Override
    protected LinkedHashMap<String, Option> getDefaultOptions() {
        this.options.put("test", new OptionInt(10));
        return this.options;
    }

    @Override
    public Class<ManualAlgorithm> getNeuralNetworkClass() {
        return ManualAlgorithm.class;
    }
}
