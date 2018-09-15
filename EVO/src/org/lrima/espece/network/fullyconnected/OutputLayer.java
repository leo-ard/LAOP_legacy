package org.lrima.espece.network.fullyconnected;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

public class OutputLayer extends Layer {

    public OutputLayer(int nbNodes){
        super(nbNodes);

        this.weights = MatrixUtils.createRealIdentityMatrix(this.neurons.size());
    }

    public Layer clone(){
        Layer layer = new OutputLayer(this.neurons.size());
        layer.setWeights(this.weights.copy());

        return layer;
    }

    @Override
    public double[] calculateWeightedSum() {
        return this.getNeuronsAsArray();
    }

    @Override
    public RealMatrix getWeights() {
        return MatrixUtils.createRealIdentityMatrix(this.getNeuronsAsArray().length);
    }
}
