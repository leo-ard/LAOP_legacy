package org.lrima.espece.network.neat;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.lrima.espece.network.interfaces.NeuralNetwork;
import org.lrima.espece.network.interfaces.NeuralNetworkReceiver;
import org.lrima.espece.network.interfaces.NeuralNetworkTransmitter;
import org.lrima.utils.Random;

import javax.xml.soap.Node;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Genome implements NeuralNetwork {

    private ArrayList<ConnectionGene> connections;
    private ArrayList<NodeGene> nodes;
    private double fitness;

    private ArrayList<? extends NeuralNetworkTransmitter> transmitters;
    private NeuralNetworkReceiver receiver;

    private final int nbOutput = 2;

    public Genome(){
        this.connections = new ArrayList<>();
        this.nodes = new ArrayList<>();
    }

    @Override
    public void init(ArrayList<? extends NeuralNetworkTransmitter> transmitters, NeuralNetworkReceiver receiver){
        this.transmitters = transmitters;
        this.receiver = receiver;

        //Create the default nodes
        //Inputs
        for(NeuralNetworkTransmitter transmitter : transmitters){
            nodes.add(new NodeGene(NodeGene.Type.INPUT));
        }
        //Output
        for(int i = 0 ; i < nbOutput ; i++){
            nodes.add(new NodeGene(NodeGene.Type.OUTPUT));
        }
        //A connection between a random input and output
        ArrayList<NodeGene> inputNodes = this.getInputNodes();
        ArrayList<NodeGene> outputNodes = this.getOutputNodes();
        NodeGene randomNodeInput1 = inputNodes.get(Random.getRandomIntegerValue(inputNodes.size()));
        NodeGene randomNodeInput2;
        do {
            randomNodeInput2 = inputNodes.get(Random.getRandomIntegerValue(inputNodes.size()));
        }while(randomNodeInput2 == randomNodeInput1);

        this.connections.add(new ConnectionGene(randomNodeInput1,  this.getOutputNodes().get(0)));
        this.connections.add(new ConnectionGene(randomNodeInput2,  this.getOutputNodes().get(1)));

    }

    /**
     * Search in the NodeGene array for all the nodes that are output
     * @return an ArrayList containing all output nodes
     */
    private ArrayList<NodeGene> getOutputNodes(){
        ArrayList<NodeGene> outputNodes = new ArrayList<>();
        for(NodeGene gene : this.nodes){
            if(gene.getType() == NodeGene.Type.OUTPUT){
                outputNodes.add(gene);
            }
        }

        return outputNodes;
    }

    /**
     * Search in the NodeGene array for all the nodes that are input nodes
     * @return an ArrayList containing all inputs nodes
     */
    private ArrayList<NodeGene> getInputNodes(){
        ArrayList<NodeGene> inputNodes = new ArrayList<>();
        for(NodeGene gene : this.nodes){
            if(gene.getType() == NodeGene.Type.INPUT){
                inputNodes.add(gene);
            }
        }

        return inputNodes;
    }

    /**
     * Search in the NodeGene array for all the nodes that are hidden nodes
     * @return an ArrayList containing all hidden nodes
     */
    private ArrayList<NodeGene> getHiddenNodes(){
        ArrayList<NodeGene> hiddenNodes = new ArrayList<>();
        for(NodeGene gene : this.nodes){
            if(gene.getType() == NodeGene.Type.HIDDEN){
                hiddenNodes.add(gene);
            }
        }

        return hiddenNodes;
    }

    public void minimalMutation(){
        for(ConnectionGene connection : this.connections){
            double oldWeight = connection.getWeight();
            double delta = Random.getRandomDoubleValue(-0.5, 0.5);
            connection.setWeight(oldWeight + delta);
        }
    }

    @Override
    public void mutate() {
        int chanceWeightMutation = Random.getRandomIntegerValue(100);
        int chanceAddConnection = Random.getRandomIntegerValue(100);
        int chanceAddNode = Random.getRandomIntegerValue(100);

        if(chanceWeightMutation < 80){
            this.changeWeightMutation(0.2);
        }
        if(chanceAddConnection < 10){
            this.addConnectionMutation();
        }
        if(chanceAddNode < 10){
            this.addNodeMutation();
        }
    }

    private void changeWeightMutation(double delta){
        int chance = Random.getRandomIntegerValue(100);

        if(chance < 90){
            //All weights are uniformaly modified
            double perturbation = Random.getRandomDoubleValue(-delta, delta);
            for(ConnectionGene connection : this.connections){
                connection.setWeight(connection.getWeight() + perturbation);
            }
        }
        else{
            //All weights are reseted to a random value
            double weight = Random.getRandomDoubleValue(-1.0, 1.0);
            for(ConnectionGene connection : this.connections){
                connection.setWeight(weight);
            }
        }
    }

    /**
     * Add a connection between two random nodes
     */
    public void addConnectionMutation(){
        NodeGene nodeGene1 = nodes.get(Random.getRandomIntegerValue(nodes.size()));
        NodeGene nodeGene2;

        do{
            nodeGene2 = nodes.get(Random.getRandomIntegerValue(nodes.size()));
        }while(nodeGene1 == nodeGene2 || (nodeGene1.getType() == NodeGene.Type.INPUT && nodeGene2.getType() == NodeGene.Type.INPUT) || ((nodeGene1.getType() == NodeGene.Type.OUTPUT && nodeGene2.getType() == NodeGene.Type.OUTPUT)));



        //Put nodeGene1 and nodeGene2 in the correct order
        if(nodeGene1.getType() == NodeGene.Type.HIDDEN && nodeGene2.getType() == NodeGene.Type.INPUT){
            NodeGene tmp = nodeGene1;
            nodeGene1 = nodeGene2;
            nodeGene2 = tmp;
        }
        else if(nodeGene1.getType() == NodeGene.Type.OUTPUT && nodeGene2.getType() == NodeGene.Type.HIDDEN){
            NodeGene tmp = nodeGene1;
            nodeGene1 = nodeGene2;
            nodeGene2 = tmp;
        }
        else if(nodeGene1.getType() == NodeGene.Type.OUTPUT && nodeGene2.getType() == NodeGene.Type.INPUT){
            NodeGene tmp = nodeGene1;
            nodeGene1 = nodeGene2;
            nodeGene2 = tmp;
        }

        //Check if the connection already exist
        ConnectionGene connectionAlreadyExist = null;
        for(ConnectionGene connection : connections){
            if(connection.getInput().equals(nodeGene1) && connection.getOutput().equals(nodeGene2)){
                connectionAlreadyExist = connection;
                break;
            }
        }

        //If the connection doesn't already exist, create it
        if(connectionAlreadyExist == null){
            this.connections.add(new ConnectionGene(nodeGene1, nodeGene2));
        }
    }

    /**
     * Take a random connection and put a new node in between.
     * It creates two new connections and the old connection is set to expressed(false)
     */
    public void addNodeMutation(){
        ConnectionGene connection = connections.get(Random.getRandomIntegerValue(connections.size() - 1));
        connection.setExpresed(false);

        NodeGene input = connection.getInput();
        NodeGene output = connection.getOutput();
        NodeGene newNode = new NodeGene(NodeGene.Type.HIDDEN);

        //Create two connections
        ConnectionGene startConnection = new ConnectionGene(input, newNode, 1.0);
        ConnectionGene endConnection = new ConnectionGene(newNode, output, connection.getWeight());

        this.nodes.add(newNode);
        this.connections.add(startConnection);
        this.connections.add(endConnection);
    }

    /**
     * Create a child Genome from two parent Genome
     * @param parent1
     * @param parent2
     * @return
     */
    public static Genome crossOver(Genome parent1, Genome parent2){
        Genome child = new Genome();

        for(NodeGene node : parent1.getNodes()){
            child.addNode(node.copy());
        }

        for(ConnectionGene connection : parent1.getConnections()){
            ConnectionGene childConnection = new ConnectionGene(connection.getInput(), connection.getOutput());
            child.addConnection(childConnection);
        }

        /*for(ConnectionGene connection : parent1.getConnections()){
            if(parent2.getConnections().contains(connection)){ //Matching gene
                ConnectionGene randomParentConnection = Random.getRandomBoolean() ? connection.copy() : parent2.getConnectionWithInnovation(connection.getInnovation()).copy();
                child.addConnection(randomParentConnection);
            }
            else{ //Disjoint or excess
                ConnectionGene childConnection = connection;
                for(ConnectionGene connection2 : parent2.getConnections()) {
                    childConnection = Random.getRandomBoolean() ? connection : connection2;
                }
                child.addConnection(childConnection.copy());
            }
        }*/

        return child;
    }

    public void setTransmitters(ArrayList<? extends NeuralNetworkTransmitter> transmitters) {
        this.transmitters = transmitters;
    }

    public void setReceiver(NeuralNetworkReceiver receiver) {
        this.receiver = receiver;
    }

    @Override
    public RealMatrix query(double[] inputs) {


        return null;
    }

    @Override
    public void feedForward() {
        //Set the value to the input nodes
        ArrayList<NodeGene> inputNodes = this.getInputNodes();
        for(int i = 0 ; i < inputNodes.size() ; i++){
            NodeGene input = inputNodes.get(i);
            input.setValue(this.transmitters.get(i).getNeuralNetworkInput());
        }

        for(NodeGene node : this.nodes){
            if(node.getType() != NodeGene.Type.INPUT) {
                ArrayList<NodeGene> nodesConnectingToIt = this.getInputsIntoNode(node);
                ArrayList<Double> weightsConnectingToIt = this.getWeightsIntoNode(node);

                if(nodesConnectingToIt.size() > 0){
                }

                node.calculateWeightedSum(nodesConnectingToIt, weightsConnectingToIt);
            }
        }

        ArrayList<NodeGene> outputNodes = this.getOutputNodes();
        double[] outputNodesValues = new double[outputNodes.size()];

        for(int i = 0 ; i < outputNodes.size() ; i++){
            NodeGene outputNode = outputNodes.get(i);

            //To have a value between 0 and 1.
            double mappedValue = (outputNode.getValue() + 1)/(2);

            outputNodesValues[i] = mappedValue;
        }

        this.receiver.setNeuralNetworkOutput(outputNodesValues);
    }

    /**
     * Finds all the nodes going into another node
     * @param node the nodes to get the inputs from
     * @return all the nodes connecting to 'node'
     */
    private ArrayList<NodeGene> getInputsIntoNode(NodeGene node){
        ArrayList<NodeGene> inputs = new ArrayList<>();
        for(ConnectionGene connection : this.connections){
            if(connection.getOutput() == node){
                inputs.add(connection.getInput());
            }
        }

        return inputs;
    }

    private ArrayList<Double> getWeightsIntoNode(NodeGene node) {
        ArrayList<Double> weights = new ArrayList<>();
        for (ConnectionGene connection : this.connections) {
            if (connection.getOutput() == node) {
                weights.add(connection.getWeight());
            }
        }

        return weights;
    }

    public ConnectionGene getConnectionWithInnovation(int innovation){
        for(ConnectionGene connection : this.connections){
            if(connection.getInnovation() == innovation){
                return connection;
            }
        }
        return null;
    }


    final int NODE_MARGIN = 75;
    final int NODE_SIZE = 50;
    final int INPUT_NODE_POSITION_X = 0;
    final int OUTPUT_NODE_POSITION_X = (NODE_SIZE + NODE_MARGIN) * 3;
    final int HIDDEN_NODE_POSITION_X = (INPUT_NODE_POSITION_X + OUTPUT_NODE_POSITION_X) / 2;

    @Override
    public void draw(Graphics2D g) {

        ArrayList<NodeGene> inputNodes = this.getInputNodes();
        ArrayList<NodeGene> outputNodes = this.getOutputNodes();
        ArrayList<NodeGene> hiddenNodes = this.getHiddenNodes();
        HashMap<NodeGene, Point> nodePositions = new HashMap<>();

        for(int i = 0 ; i < inputNodes.size(); i++){
            NodeGene node = inputNodes.get(i);

            Point position = new Point(INPUT_NODE_POSITION_X, i * NODE_MARGIN);
            nodePositions.put(node, position);

            g.drawOval(position.x, position.y, NODE_SIZE, NODE_SIZE);
        }

        for(int i = 0 ; i < hiddenNodes.size(); i++){
            NodeGene node = hiddenNodes.get(i);

            Point position = new Point(HIDDEN_NODE_POSITION_X, i * NODE_MARGIN);
            nodePositions.put(node, position);

            g.drawOval(position.x, position.y, NODE_SIZE, NODE_SIZE);
        }

        for(int i = 0 ; i < outputNodes.size(); i++){
            NodeGene node = outputNodes.get(i);

            Point position = new Point(OUTPUT_NODE_POSITION_X, i * NODE_MARGIN);
            nodePositions.put(node, position);

            g.drawOval(position.x, position.y, NODE_SIZE, NODE_SIZE);
        }

        for(ConnectionGene connection : this.connections){
            if(connection.isExpresed()) {
                NodeGene input = connection.getInput();
                NodeGene output = connection.getOutput();

                Point lineStart = nodePositions.get(input);
                Point lineEnd = nodePositions.get(output);

                try {
                    g.drawLine(lineStart.x, lineStart.y, lineEnd.x, lineEnd.y);
                }catch (Exception e){
                    System.out.println("Input: " + inputNodes.size());
                    System.out.println("Hidden: " + hiddenNodes.size());
                    System.out.println("Output: " + outputNodes.size());
                    System.out.println("Start: " + lineStart);
                    System.out.println("End: " + lineEnd);
                }
            }
        }

    }

    public void addConnection(ConnectionGene connection){
        this.connections.add(connection);
    }

    public ArrayList<ConnectionGene> getConnections() {
        return connections;
    }

    public double getFitness() {
        return fitness;
    }

    public ArrayList<NodeGene> getNodes() {
        return nodes;
    }

    public void addNode(NodeGene node){
        this.nodes.add(node);
    }
}
