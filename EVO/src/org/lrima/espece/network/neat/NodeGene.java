package org.lrima.espece.network.neat;

import javax.xml.soap.Node;
import java.util.ArrayList;

public class NodeGene {

    enum Type{
        INPUT,
        HIDDEN,
        OUTPUT,
        ;
    }

    private static int currentInnovation = 0;

    private Type type;
    private double value;
    private int innovation;

    public NodeGene(Type type){
        this.type = type;
        this.innovation = NodeGene.currentInnovation;
        NodeGene.currentInnovation = this.innovation + 1;
    }

    public NodeGene(Type type, int innovation){
        this.type = type;
        this.innovation = innovation;
    }

    public Type getType() {
        return type;
    }

    public void calculateWeightedSum(ArrayList<NodeGene> genes, ArrayList<Double> weights){
        double sum = 0;

        for(int i = 0 ; i < genes.size() ; i++){
            NodeGene gene = genes.get(i);
            double weight = weights.get(i);

            sum += weight * gene.getValue();
        }

        this.value = sum;
    }


    public NodeGene copy(){
        NodeGene gene = new NodeGene(type, this.innovation);
        return gene;
    }

    public double getValue() {
        return value;
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
}
