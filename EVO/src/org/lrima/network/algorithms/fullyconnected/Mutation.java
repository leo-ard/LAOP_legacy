package org.lrima.network.algorithms.fullyconnected;

import org.apache.commons.math3.linear.RealMatrix;
import org.lrima.utils.EVOMatrixUtils;
import org.lrima.utils.Random;

import java.util.ArrayList;

public abstract class Mutation {

    /**
     * Goes through all the weights and adds a random value between -delta and delta to it.
     * @param layer the layer to modify
     * @param percentage the percentage of chance that a weight will be changed
     * @param delta the max amount you want to add or subtract to the node
     */
    public static void mutateRandomWeights(Layer layer, double percentage, double delta){
            for(int neuron = 0 ; neuron < layer.getWeights().getRowDimension() ; neuron++){
                for(int connection = 0 ; connection < layer.getWeights().getColumnDimension() ; connection++){
                    int mutationchance = Random.getRandomIntegerValue(100);
                    if(mutationchance < percentage){
                        double valueBeforeChange = layer.getWeights().getEntry(neuron, connection);
                        double weightChange = Random.getRandomDoubleValue(-delta, delta);
                        double newValue = valueBeforeChange + weightChange;

                        layer.getWeights().setEntry(neuron, connection, newValue);
                    }
                }
            }
    }

    /**
     * Sets the a random weight to 0
     * @param layer the layer to modify
     */
    public static void muteRandomConnection(Layer layer){
        int neuronChosen = Random.getRandomIntegerValue(0, layer.getWeights().getRowDimension() - 1);
        int connectionChosen = Random.getRandomIntegerValue(0, layer.getWeights().getColumnDimension() - 1);

        layer.getWeights().setEntry(neuronChosen, connectionChosen, 0.0);
    }

    /**
     * Used to make two neural network reproduce by taking all their weight matrices and mixing them together
     * to create a new array of layers
     * @param parent1 the first parent to reproduce
     * @param parent2 the parent to reproduce with parent1
     * @return an arrayList of all the layers of the new NeuralNetwork
     */
    public static ArrayList<Layer> reproduct(FullyConnectedNeuralNetwork parent1, FullyConnectedNeuralNetwork parent2){
        ArrayList<Layer> childLayers = new ArrayList<>();
        ArrayList<Layer> parent1Layers = parent1.getLayers();
        ArrayList<Layer> parent2Layers = parent2.getLayers();

        for(int i = 0 ; i < parent1Layers.size() ; i++){
            Layer parent1Layer = parent1Layers.get(i);
            Layer parent2Layer = parent2Layers.get(i);

            //Copy the arrays
            childLayers.add(new Layer(parent1Layer.getNeuronsAsArray().length));
            RealMatrix childWeights = EVOMatrixUtils.mix(parent1Layer.getWeights(), parent2Layer.getWeights());
            childLayers.get(i).setWeights(childWeights);
        }

        return childLayers;
    }

    /**
     * Goes through all the weights of the parent and has a chance to copy it to its child
     * @param parent the parent's neural network
     * @param child the child's neural network
     * @param percentage the percentage of chance a parent will give a weight to its child
     */
    public static void reproduceWith(FullyConnectedNeuralNetwork parent, FullyConnectedNeuralNetwork child, double percentage){
        for(int i = 0 ; i < parent.getLayers().size() ; i++){
            Layer layer = parent.getLayers().get(i);

            for (int neuron = 0; neuron < layer.getWeights().getRowDimension(); neuron++) {
                for (int connection = 0; connection < layer.getWeights().getColumnDimension(); connection++) {
                    int copyChance = Random.getRandomIntegerValue(100);
                    if (copyChance < percentage) {
                        double copyValue = layer.getWeights().getEntry(neuron, connection);
                        child.getLayers().get(i).getWeights().setEntry(neuron, connection, copyValue);
                    }
                }
            }
        }

    }
}
