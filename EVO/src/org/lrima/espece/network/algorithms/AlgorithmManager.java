package org.lrima.espece.network.algorithms;

import org.lrima.espece.network.algorithms.fullyconnected.FullyConnectedNeuralNetwork;
import org.lrima.espece.network.algorithms.improved_neat.ImprovedNeatGenome;
import org.lrima.espece.network.algorithms.neat.NeatGenome;
import org.lrima.espece.network.interfaces.NeuralNetwork;

/**
 * Used to store all the algorithm classes so that the user can choose which algorithm to simulate
 */
public class AlgorithmManager {

    //If you created a new algorithm, add it to this array.
    public static Class<?extends NeuralNetwork>[] algorithms = new Class[]{
            FullyConnectedNeuralNetwork.class,
            NeatGenome.class,
            ImprovedNeatGenome.class

    };

}
