package org.lrima.network.algorithms.improved_neat;

import org.lrima.network.annotations.AlgorithmInformation;
import org.lrima.network.interfaces.NeuralNetwork;
import org.lrima.network.interfaces.NeuralNetworkReceiver;
import org.lrima.network.interfaces.NeuralNetworkTransmitter;
import org.lrima.Interface.options.Option;
import org.lrima.utils.Random;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class ImprovedNeatGenome extends NeuralNetwork {

    private ArrayList<ConnectionGene> connections;
    private ArrayList<NodeGene> nodes;

    private final int nbOutput = 2;

    public ImprovedNeatGenome(LinkedHashMap<String, Option> neuralNetworkModel){
        super(neuralNetworkModel);
        this.connections = new ArrayList<>();
        this.nodes = new ArrayList<>();
    }

    /**
     * Init the NeatGenome with the inputs and outputs nodes.
     * It also initializes the NeatGenome with two connections from random inputs
     */
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
     * Create a child NeatGenome from two parent NeatGenome
     * @param network1 the first parent
     * @param network2 the second parent
     * @return a child genome from parent1 and parent2
     */
    @Override
    public NeuralNetwork crossOver(NeuralNetwork network1, NeuralNetwork network2) {
        ImprovedNeatGenome child = new ImprovedNeatGenome(this.options);

        ImprovedNeatGenome parent1 = (ImprovedNeatGenome) network1;
        ImprovedNeatGenome parent2 = (ImprovedNeatGenome) network2;

        for(NodeGene node : parent1.getNodes()){
            if(parent2.getNodes().contains(node)){
                NodeGene node2 = parent2.getNodeWithInnovation(node.getInnovation());
                NodeGene childNode = Random.getRandomBoolean() ? node.copy() : node2.copy();
                child.addNode(childNode);
            }
            else{ //Excess node, copy from the fittest parent
                child.addNode(node.copy());
            }
        }

        for(ConnectionGene connection : parent1.getConnections()){
            if(parent2.getConnections().contains(connection)){
                ConnectionGene parent2Connection = parent2.getConnectionWithInnovation(connection.getInnovation());
                ConnectionGene childConnection = Random.getRandomBoolean() ? connection.copy() : parent2Connection.copy();
                child.addConnection(childConnection);
            }
            else{ //Excess or disjoint
                //Get connection from fittest parrent
                NodeGene inputNode = connection.getInput();
                NodeGene outputNode = connection.getOutput();

                NodeGene childInputNode = child.getNodeWithInnovation(inputNode.getInnovation());
                NodeGene childOutputNode = child.getNodeWithInnovation(outputNode.getInnovation());

                child.addConnection(new ConnectionGene(childInputNode, childOutputNode));
            }
        }

        return child;
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

    /**
     * Mutate the NeatGenome.
     * 80% chance to mutate the weights
     * 10% chance of adding a connection
     * 10% chance of adding a node between a connection
     */
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

    /**
     * Do a mutation that changes the weights. 90% chance of uniformaly modify all weights and 10% chance to reset all weights
     * to a random value between -1.0 and 1.0
     * @param delta the absolute value of the maximum you want your weights to be modified by
     */
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
    private void addConnectionMutation(){
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
    private void addNodeMutation(){
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

                node.calculateWeightedSum(nodesConnectingToIt, weightsConnectingToIt);
            }
        }

        ArrayList<NodeGene> outputNodes = this.getOutputNodes();

        double[] outputNodesValues = new double[outputNodes.size()];

        for(int i = 0 ; i < outputNodes.size() ; i++){
            NodeGene outputNode = outputNodes.get(i);

            //To have a value between 0 and 1.
            //double mappedValue = (outputNode.getValue() + 1)/(2);

            outputNodesValues[i] = outputNode.getValue();
        }

        this.receiver.setNeuralNetworkOutput(outputNodesValues);
    }

    @Override
    public void generationFinish() {
        this.mutate();
    }

    /**
     * Finds all the nodes leading into another node
     * @param node the node to get the nodes going into
     * @return all the nodes going into the specified node
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

    /**
     * Find all the weights leading to a certain node
     * @param node the node to get the weights going into
     * @return an arrayList containing all the weights going in the specified node
     */
    private ArrayList<Double> getWeightsIntoNode(NodeGene node) {
        ArrayList<Double> weights = new ArrayList<>();
        for (ConnectionGene connection : this.connections) {
            if (connection.getOutput() == node) {
                weights.add(connection.getWeight());
            }
        }

        return weights;
    }

    /**
     * Goes through all the connections to find the one with a certain innovation number
     * @param innovation the innovation number
     * @return the connection with the specified innovation number
     */
    private ConnectionGene getConnectionWithInnovation(int innovation){
        for(ConnectionGene connection : this.connections){
            if(connection.getInnovation() == innovation){
                return connection;
            }
        }
        return null;
    }

    /**
     * Goes through all the nodes and find the one with the specified innovation number
     * @param innovation the innovation number
     * @return the node with the specified innovation number
     */
    private NodeGene getNodeWithInnovation(int innovation){
        for(NodeGene node : this.nodes){
            if(node.getInnovation() == innovation){
                return node;
            }
        }
        return null;
    }


    private final int NODE_MARGIN = 75;
    private final int NODE_SIZE = 50;
    private final int INPUT_NODE_POSITION_X = 0;
    private final int OUTPUT_NODE_POSITION_X = (NODE_SIZE + NODE_MARGIN) * 3;
    private final int HIDDEN_NODE_POSITION_X = (INPUT_NODE_POSITION_X + OUTPUT_NODE_POSITION_X) / 2;

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
            g.drawString(String.format("%.2f", node.getValue()), position.x, position.y + 25);
        }

        for(int i = 0 ; i < hiddenNodes.size(); i++){
            NodeGene node = hiddenNodes.get(i);

            Point position = new Point(HIDDEN_NODE_POSITION_X, i * NODE_MARGIN);
            nodePositions.put(node, position);

            g.drawOval(position.x, position.y, NODE_SIZE, NODE_SIZE);
            g.drawString(String.format("%.2f", node.getValue()), position.x, position.y + 25);
        }

        for(int i = 0 ; i < outputNodes.size(); i++){
            NodeGene node = outputNodes.get(i);

            Point position = new Point(OUTPUT_NODE_POSITION_X, i * NODE_MARGIN);
            nodePositions.put(node, position);

            g.drawOval(position.x, position.y, NODE_SIZE, NODE_SIZE);
            g.drawString(String.format("%.2f", node.getValue()), position.x, position.y + 25);
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

    private void addConnection(ConnectionGene connection){
        this.connections.add(connection);
    }

    private ArrayList<ConnectionGene> getConnections() {
        return connections;
    }

    private double getFitness() {
        return fitness;
    }

    private ArrayList<NodeGene> getNodes() {
        return nodes;
    }

    private void addNode(NodeGene node){
        this.nodes.add(node);
    }
}
