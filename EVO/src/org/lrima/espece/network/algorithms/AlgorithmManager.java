package org.lrima.espece.network.algorithms;

import org.lrima.espece.network.algorithms.fullyconnected.FullyConnectedNeuralModel;
import org.lrima.espece.network.algorithms.neat.NeatModel;
import org.lrima.espece.network.annotations.AlgorithmInformation;
import org.lrima.espece.network.interfaces.NeuralNetworkModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Used to store all the algorithm classes so that the user can choose which algorithm to simulate
 */
public class AlgorithmManager {

    /**
     * All the algorithms that are available. Set by the programmer in an array
     */
    public static ArrayList<Class<? extends NeuralNetworkModel>> algorithms;

    /**
     * Array of all the algorithms name
     */
    public static ArrayList<String> algorithmsName;

    static {
        algorithms = new ArrayList<>(Arrays.asList(
                //////////////////////////////////////////
                //// ADD YOUR ALGORITHMS MODELS BELOW ////
                //////////////////////////////////////////
                FullyConnectedNeuralModel.class,
                NeatModel.class)
        );


        algorithmsName = AlgorithmManager.algorithms
                .stream()
                .map(algorithmsModelClass -> algorithmsModelClass.getAnnotation(AlgorithmInformation.class).name())
                .collect(Collectors.toCollection(ArrayList::new));

    }

}
