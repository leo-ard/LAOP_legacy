package org.lrima.espece.network.neat;

import org.lrima.utils.Random;

public class ConnectionGene {
    public static int currentInnovation = 0;
    private NodeGene input;
    private NodeGene output;
    private double weight;
    private boolean expresed;
    private int innovation;

    public ConnectionGene(NodeGene input, NodeGene output){
        this.input = input;
        this.output = output;

        this.weight = Random.getRandomDoubleValue(-1.0, 1.0);
        this.expresed = true;
        this.innovation = ConnectionGene.currentInnovation;
        ConnectionGene.currentInnovation += 1;
    }

    public void mutate(){
        double delta = Random.getRandomDoubleValue(-0.2, 0.2);
        this.weight += delta;
    }

    public ConnectionGene(NodeGene input, NodeGene output, double weight){
        this(input, output);
        this.weight = weight;
    }

    public ConnectionGene(NodeGene input, NodeGene output, double weight, boolean expresed, int innovation){
        this.input = input;
        this.output = output;
        this.weight = weight;
        this.expresed = expresed;
        this.innovation = innovation;
    }

    public ConnectionGene copy(){
        ConnectionGene connection = new ConnectionGene(this.input.copy(), this.output.copy(), this.weight, this.expresed, this.innovation);

        return connection;
    }

    @Override
    public boolean equals(Object obj) {
        try{
            ConnectionGene connection2 = (ConnectionGene) obj;

            return connection2.innovation == this.innovation;

        }catch (Exception e){
            return false;
        }
    }

    public NodeGene getInput() {
        return input;
    }

    public NodeGene getOutput() {
        return output;
    }

    public void setExpresed(boolean expresed) {
        this.expresed = expresed;
    }

    public double getWeight() {
        return weight;
    }

    public int getInnovation() {
        return innovation;
    }

    public boolean isExpresed() {
        return expresed;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    @Override
    public int hashCode() {
        return this.innovation;
    }
}
