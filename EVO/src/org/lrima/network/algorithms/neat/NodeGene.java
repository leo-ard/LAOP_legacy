package org.lrima.network.algorithms.neat;

import org.lrima.utils.Random;

import java.util.ArrayList;

public class NodeGene {

    enum Type{
        INPUT,
        HIDDEN,
        OUTPUT,
        ;
    }

    private Type type;
    private double value;
    private int innovation;
    public boolean hasBeenCalculated = false;

    /*protected NodeGene(Type type){
        this.type = type;
        this.innovation = NodeGene.currentInnovation;
        NodeGene.currentInnovation = this.innovation + 1;
    }*/

    public NodeGene(Type type, int innovation){
        this.type = type;
        this.innovation = innovation;
        this.value = Random.getRandomDoubleValue(-1, 1);
    }

    protected Type getType() {
        return type;
    }

    protected void calculateWeightedSum(ArrayList<NodeGene> genes, ArrayList<Double> weights){
        double sum = 0;

        for(int i = 0 ; i < genes.size() ; i++){
            NodeGene gene = genes.get(i);
            double weight = weights.get(i);

            sum += weight * gene.getValue();
        }

        this.value = sum;
        hasBeenCalculated = true;
    }


    protected NodeGene copy(){
        return new NodeGene(type, this.innovation);
    }

    public double getValue() {
        return value;
    }

    public void reset(){
        this.hasBeenCalculated = false;
    }

    @Override
    public boolean equals(Object obj) {
        try{
            NodeGene node2 = (NodeGene) obj;
            return this.innovation == node2.innovation;
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public int hashCode() {
        return this.innovation;
    }

    public void setValue(double value) {
        this.value = value;
    }

    protected int getInnovation() {
        return innovation;
    }
}
