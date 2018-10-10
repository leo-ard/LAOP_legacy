package org.lrima.network.algorithms.fullyconnected;

import org.lrima.network.functions.Function;

public class Layer{
    private double[] neurons;
    private double[] weights;

    public Layer(double[] neurons, double[] weights){
        this.neurons = neurons;
        this.weights = weights;
    }

    public Layer(int layerSize, double[] weights){
        this.neurons = new double[layerSize];
        this.weights = weights;
    }

    public void calculateSum(Layer prevousLayer){
        for(int i = 0; i < this.neurons.length; i++){
            double tempNeuronvalue = 0;
            for(int j = 0; j < prevousLayer.neurons.length; j++){
                tempNeuronvalue += prevousLayer.neurons[j] * weights[prevousLayer.neurons.length * i + j];
            }

            neurons[i] = Function.SIGMOID.getValue(tempNeuronvalue);
        }
    }

    public double[] getOutput(){
        return this.neurons;
    }
}
