package org.lrima.network.interfaces;

public interface NeuralNetworkReceiver {
    void setNeuralNetworkOutput(double ... outputs);
    int getSize();
}
