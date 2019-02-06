package org.lrima.network.algorithms.neat;

import org.lrima.espece.Espece;
import org.lrima.espece.capteur.Capteur;
import org.lrima.network.interfaces.*;
import org.lrima.Interface.options.Option;
import org.lrima.simulation.Simulation;
import org.lrima.utils.Random;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

public class NeatGenome extends NeuralNetwork {
    private ArrayList<ConnectionGene> connections;
    private ArrayList<NodeGene> nodes;
    private int currentNodeInnovation = 0;

    private final int nbOutput = 2;

    public static void main(String[] args) {
        NeuralNetworkModel model = new NeatModel();
        Simulation simulation = new Simulation(model);

        Espece e1 = new Espece(simulation);
        Capteur c1 = new Capteur(e1, 0, 0, 0);
        Capteur c2 = new Capteur(e1, 0, 0, 0);
        Capteur c3 = new Capteur(e1, 0, 0, 0);
        ArrayList<Capteur> capteurs1 = new ArrayList<>();
        capteurs1.add(c1);
        capteurs1.add(c2);
        capteurs1.add(c3);
        NeatGenome parent1 = new NeatGenome(null);
        parent1.init(capteurs1, e1);
        parent1.test1();

        Espece e2 = new Espece(simulation);
        Capteur c12 = new Capteur(e2, 0, 0, 0);
        Capteur c22 = new Capteur(e2, 0, 0, 0);
        Capteur c32 = new Capteur(e2, 0, 0, 0);
        ArrayList<Capteur> capteurs12 = new ArrayList<>();
        capteurs12.add(c12);
        capteurs12.add(c22);
        capteurs12.add(c32);
        NeatGenome parent2 = new NeatGenome(null);
        parent2.init(capteurs12, e2);
        parent2.test2();

        NeatGenome child = (NeatGenome) parent1.crossOver(parent1, parent2);


        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(1000, 600);
        GridLayout layout = new GridLayout(2, 2);

        JPanel content = new JPanel();
        content.setLayout(layout);

        content.add(new TestPanel(parent1));
        content.add(new TestPanel(parent2));
        content.add(new TestPanel(child));

        frame.add(new JScrollPane(content));

        frame.setVisible(true);
    }

    public NeatGenome(LinkedHashMap<String, Option> options) {
        super(options);
        this.connections = new ArrayList<>();
        this.nodes = new ArrayList<>();
    }

    public NeatGenome(LinkedHashMap<String, Option> options, int currentNodeInnovation){
        this(options);
        this.currentNodeInnovation = currentNodeInnovation;
    }


    /**
     * Init the NeatGenome with the inputs and outputs nodes.
     * It also initializes the NeatGenome with two connections from random inputs
     */
    @Override
    public void init(ArrayList<? extends NeuralNetworkTransmitter> transmitters, NeuralNetworkReceiver receiver){
        super.init(transmitters, receiver);

        if(this.nodes.size() == 0) {
            //Create the default nodes
            //Inputs
            for (NeuralNetworkTransmitter transmitter : transmitters) {
                nodes.add(new NodeGene(NodeGene.Type.INPUT, ++currentNodeInnovation));
            }
            //Output
            for (int i = 0; i < nbOutput; i++) {
                nodes.add(new NodeGene(NodeGene.Type.OUTPUT, ++currentNodeInnovation));
            }
        }

        if(this.connections.size() == 0) {
            //A connection between a random input and output
            ArrayList<NodeGene> inputNodes = this.getInputNodes();
            ArrayList<NodeGene> outputNodes = this.getOutputNodes();
            NodeGene randomNodeInput1 = inputNodes.get(Random.getRandomIntegerValue(inputNodes.size()));
            NodeGene randomNodeOutput = outputNodes.get(Random.getRandomIntegerValue(outputNodes.size()));

            this.connections.add(new ConnectionGene(randomNodeInput1, randomNodeOutput));
        }



    }

    //Cree les connections qu'on voit dans le paper de NEAT 1
    private void test1(){
        NodeGene hiddenNode1 = new NodeGene(NodeGene.Type.HIDDEN, ++currentNodeInnovation);
        ConnectionGene c1 = new ConnectionGene(getInputNodes().get(0), getOutputNodes().get(0));
        ConnectionGene c2 = new ConnectionGene(getInputNodes().get(1), getOutputNodes().get(0));
        c2.setExpresed(false);
        ConnectionGene c3 = new ConnectionGene(getInputNodes().get(2), getOutputNodes().get(0));
        ConnectionGene c4 = new ConnectionGene(getInputNodes().get(1), hiddenNode1);
        ConnectionGene c5 = new ConnectionGene(hiddenNode1, getOutputNodes().get(0));
        ConnectionGene c6 = new ConnectionGene(getInputNodes().get(0), hiddenNode1);

        this.nodes.add(hiddenNode1);

        this.connections.add(c1);
        this.connections.add(c2);
        this.connections.add(c3);
        this.connections.add(c4);
        this.connections.add(c5);
        this.connections.add(c6);
    }

    //Cree les connections qu'on voit dans le paper de NEAT 2
    private void test2(){
        NodeGene hiddenNode5 = new NodeGene(NodeGene.Type.HIDDEN, ++currentNodeInnovation);
        NodeGene hiddenNode6 = new NodeGene(NodeGene.Type.HIDDEN, ++currentNodeInnovation);

        this.nodes.add(hiddenNode5);
        this.nodes.add(hiddenNode6);

        NodeGene output = getOutputNodes().get(0);

        ConnectionGene c1 = new ConnectionGene(getInputNodes().get(0), output);
        ConnectionGene c2 = new ConnectionGene(getInputNodes().get(1), output);
        c2.setExpresed(false);
        ConnectionGene c3 = new ConnectionGene(getInputNodes().get(2), output);
        ConnectionGene c4 = new ConnectionGene(getInputNodes().get(1), hiddenNode5);
        ConnectionGene c5 = new ConnectionGene(hiddenNode5, output);
        c5.setExpresed(false);
        ConnectionGene c6 = new ConnectionGene(hiddenNode5, hiddenNode6);
        ConnectionGene c7 = new ConnectionGene(hiddenNode6, output);
        ConnectionGene c8 = new ConnectionGene(getInputNodes().get(2), hiddenNode5);
        ConnectionGene c9 = new ConnectionGene(getInputNodes().get(0), hiddenNode6);

        this.connections.add(c1);
        this.connections.add(c2);
        this.connections.add(c3);
        this.connections.add(c4);
        this.connections.add(c5);
        this.connections.add(c6);
        this.connections.add(c7);
        this.connections.add(c8);
        this.connections.add(c9);
    }

    /**
     * Create a child NeatGenome from two parent NeatGenome
     * @param network1 the first parent
     * @param network2 the second parent
     * @return a child genome from parent1 and parent2
     */
    @Override
    public  NeatGenome crossOver(NeuralNetwork network1, NeuralNetwork network2) {
        NeatGenome child = new NeatGenome(this.options, currentNodeInnovation);

        NeatGenome parent1 = (NeatGenome) network1;
        NeatGenome parent2 = (NeatGenome) network2;

        for(NodeGene node : parent1.getNodes()){
            for(NodeGene node2: parent2.getNodes()){
                if(node.equals(node2)){
                    if(!child.getNodes().contains(node)) {
                        NodeGene childNode = Random.getRandomBoolean() ? node.copy() : node2.copy();
                        child.addNode(childNode);
                    }
                }
                else{ //Excess node, copy from the fittest parent
                    if(!child.getNodes().contains(node)) {
                        child.addNode(node.copy());
                    }
                    if(!child.getNodes().contains(node2)){
                        child.addNode(node2.copy());
                    }
                }
            }
        }


        for(ConnectionGene connection : parent1.getConnections()){
            for(ConnectionGene connection2 : parent2.getConnections()) {
                if (connection.equals(connection2)) {
                    if(!child.getConnections().contains(connection)) {
                        ConnectionGene childConnection = Random.getRandomBoolean() ? connection.copy() : connection2.copy();

                        child.addConnection(childConnection);
                    }
                } else { //Excess or disjoint
                    //TODO: Ne devrait pas etre aleatoire, mais selon le meilleur fitness
                    if(Random.getRandomBoolean()){
                        if(!child.getConnections().contains(connection)) {
                            child.addConnection(connection.copy());
                        }
                    }
                    else{
                        if(!child.getConnections().contains(connection2)) {
                            child.addConnection(connection2.copy());
                        }
                    }


                }
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
    @Override
    public void generationFinish() {
        int chanceWeightMutation = Random.getRandomIntegerValue(100);
        int chanceAddConnection = Random.getRandomIntegerValue(100);
        int chanceAddNode = Random.getRandomIntegerValue(100);

        if(chanceWeightMutation < 80){
            this.changeWeightMutation(0.2);
        }
        if(chanceAddConnection < 105){
            this.addConnectionMutation();
        }
        if(chanceAddNode < 100){
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
        }while(nodeGene1 == nodeGene2 || (nodeGene2.getType() == NodeGene.Type.INPUT && nodeGene1.getType() == NodeGene.Type.INPUT));

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
            if(!(nodeGene1.getType() == NodeGene.Type.OUTPUT && nodeGene1.getType() == NodeGene.Type.OUTPUT)) {
                this.connections.add(new ConnectionGene(nodeGene1, nodeGene2));
            }
        }
    }

    /**
     * Take a random connection and put a new node in between.
     * It creates two new connections and the old connection is set to expressed(false)
     */
    private void addNodeMutation(){
        ConnectionGene connection;
        do {
            connection = connections.get(Random.getRandomIntegerValue(connections.size() - 1));
        }while(!connection.isExpresed());

        connection.setExpresed(false);

        NodeGene input = connection.getInput();
        NodeGene output = connection.getOutput();
        NodeGene newNode = new NodeGene(NodeGene.Type.HIDDEN, ++currentNodeInnovation);

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
            input.hasBeenCalculated = true;
            input.setValue(this.transmitters.get(i).getNeuralNetworkInput());
        }

        ArrayList<NodeGene> openSet = new ArrayList<>();
        openSet.addAll(this.nodes);


        for(NodeGene node : openSet){
            if(node.getType() == NodeGene.Type.INPUT) break;

            ArrayList<NodeGene> nodeConnect = this.getInputsIntoNode(node);
            ArrayList<Double> weightsConnect = this.getWeightsIntoNode(node);



            node.calculateWeightedSum(nodeConnect, weightsConnect);
        }



        /*
        do {
            Iterator<NodeGene> nodeGeneIterator = openSet.iterator();

            while(nodeGeneIterator.hasNext()){
                NodeGene node = nodeGeneIterator.next();

                if (node.getType() != NodeGene.Type.INPUT) {
                    ArrayList<NodeGene> nodesConnectingToIt = this.getInputsIntoNode(node);
                    ArrayList<Double> weightsConnectingToIt = this.getWeightsIntoNode(node);

                    boolean shouldCalculate = true;

                    //Compute the weighted sum only if connected nodes has been calculated
                    for(NodeGene connectedNodes : nodesConnectingToIt){
                        if(!connectedNodes.hasBeenCalculated) {
                            shouldCalculate = false;

                            if (connectedNodes.getType() == NodeGene.Type.INPUT) {
                                shouldCalculate = true;
                            }
                        }
                    }

                    if(shouldCalculate) {

                        node.calculateWeightedSum(nodesConnectingToIt, weightsConnectingToIt);
                        nodeGeneIterator.remove();
                    }
                }
                else{
                    node.hasBeenCalculated = true;
                    nodeGeneIterator.remove();
                }
            }
            /*
            System.out.println(this.connections.size());
            System.out.println(openSet.size());
            for(NodeGene gene1 : openSet){
                ArrayList<NodeGene> nodesConnectingToIt = this.getInputsIntoNode(gene1);
                for(NodeGene gene3 :nodesConnectingToIt){
                    System.out.println("\t" + gene3.getType() + " " + gene3.hasBeenCalculated);
                    ArrayList<NodeGene> nodesConnectingToIt2 = this.getInputsIntoNode(gene3);
                    for(NodeGene gene4 : nodesConnectingToIt2){
                        System.out.println("\t\t" + gene4.getType() + " " + gene4.hasBeenCalculated);
                    }
                }
            }

        }while(openSet.size() > 0);
        */


        ArrayList<NodeGene> outputNodes = this.getOutputNodes();

        double[] outputNodesValues = new double[outputNodes.size()];

        for(int i = 0 ; i < outputNodes.size() ; i++){
            NodeGene outputNode = outputNodes.get(i);

            //To have a value between 0 and 1.
            double mappedValue = (outputNode.getValue() + 1)/(2);

            outputNodesValues[i] = mappedValue;
        }




        this.receiver.setNeuralNetworkOutput(outputNodesValues);

        for(NodeGene node : this.nodes){
            node.reset();
        }


    }

    /**
     * Finds all the nodes leading into another node
     * @param node the node to get the nodes going into
     * @return all the nodes going into the specified node
     */
    private ArrayList<NodeGene> getInputsIntoNode(NodeGene node){
        ArrayList<NodeGene> inputs = new ArrayList<>();

        for(ConnectionGene connection : this.connections){
            if(connection.isExpresed()) {
                if (connection.getOutput().equals(node)) {
                    inputs.add(connection.getInput());
                }
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
            if(connection.isExpresed()) {
                if (connection.getOutput().equals(node)) {
                    weights.add(connection.getWeight());
                }
            }
        }

        return weights;
    }



    @Override
    public void draw(Graphics2D g, Dimension panelDimensions) {
        final int NODE_SIZE = panelDimensions.width / 7;
        final int NODE_MARGIN = (int)(NODE_SIZE * 1);
        final int INPUT_NODE_POSITION_X = NODE_MARGIN;
        final int OUTPUT_NODE_POSITION_X = panelDimensions.width - NODE_SIZE - NODE_MARGIN;

        ArrayList<NodeGene> inputNodes = this.getInputNodes();
        ArrayList<NodeGene> outputNodes = this.getOutputNodes();
        ArrayList<NodeGene> hiddenNodes = this.getHiddenNodes();
        HashMap<NodeGene, Point> nodePositions = new HashMap<>();

        for(int i = 0 ; i < inputNodes.size(); i++){
            NodeGene node = inputNodes.get(i);

            Point position = new Point(  INPUT_NODE_POSITION_X, i * NODE_MARGIN);
            nodePositions.put(node, position);

            g.drawOval(position.x, position.y, NODE_SIZE, NODE_SIZE);
            //g.drawString(String.format("%.2f", node.getValue()), position.x, position.y + 25);
            g.drawString(node.getInnovation() + "", position.x, position.y + 25);
            g.setColor(Color.RED);
            g.drawString(String.format("%.2f", node.getValue()), position.x + 10, position.y + 25);
            g.setColor(Color.BLACK);
        }

        for(int i = 0 ; i < hiddenNodes.size(); i++){
            NodeGene node = hiddenNodes.get(i);

            int xPosition = Random.getRandomIntegerValue(INPUT_NODE_POSITION_X, OUTPUT_NODE_POSITION_X);

            Point position = new Point((nodes.size() / node.getInnovation()) *INPUT_NODE_POSITION_X + NODE_SIZE, i * NODE_MARGIN + 50);
            nodePositions.put(node, position);

            g.drawOval(position.x, position.y, NODE_SIZE, NODE_SIZE);
            //g.drawString(String.format("%.2f", node.getValue()), position.x, position.y + 25);
            g.drawString(node.getInnovation() + "", position.x, position.y + 25);
        }

        for(int i = 0 ; i < outputNodes.size(); i++){
            NodeGene node = outputNodes.get(i);

            Point position = new Point(OUTPUT_NODE_POSITION_X, i * NODE_MARGIN);
            nodePositions.put(node, position);

            g.drawOval(position.x, position.y, NODE_SIZE, NODE_SIZE);
            //g.drawString(String.format("%.2f", node.getValue()), position.x, position.y + 25);
            g.drawString(node.getInnovation() + "", position.x, position.y + 25);
            g.setColor(Color.RED);
            g.drawString(String.format("%.2f", node.getValue()), position.x + 10, position.y + 25);
            g.setColor(Color.BLACK);
        }

        for(ConnectionGene connection : this.connections){
            if(!connection.isExpresed()) {
                g.setColor(Color.PINK);
            }
            else{
                g.setColor(Color.BLACK);
            }

                NodeGene input = connection.getInput();
                NodeGene output = connection.getOutput();

                Point lineStart = nodePositions.get(input);
                Point lineEnd = nodePositions.get(output);

                g.drawLine(lineStart.x + NODE_SIZE / 2, lineStart.y + NODE_SIZE / 2, lineEnd.x + NODE_SIZE / 2, lineEnd.y + NODE_SIZE / 2);
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
