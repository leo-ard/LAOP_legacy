package org.lrima.network.algorithms.improved_neat;

import org.lrima.network.algorithms.fullyconnected.Layer;
import org.lrima.network.annotations.AlgorithmInformation;
import org.lrima.network.interfaces.NeuralNetwork;
import org.lrima.network.interfaces.NeuralNetworkReceiver;
import org.lrima.network.interfaces.NeuralNetworkSuperviser;
import org.lrima.network.interfaces.NeuralNetworkTransmitter;
import org.lrima.Interface.options.Option;
import org.lrima.network.supervisors.OtherSupervisor;
import org.lrima.utils.Random;
import org.omg.CORBA.IMP_LIMIT;

import javax.xml.soap.Node;
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
        if(this.connections.size() == 0) {
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
        /*NodeGene randomNodeInput1 = inputNodes.get(Random.getRandomIntegerValue(inputNodes.size()));
        NodeGene randomNodeInput2;
        do {
            randomNodeInput2 = inputNodes.get(Random.getRandomIntegerValue(inputNodes.size()));
        }while(randomNodeInput2 == randomNodeInput1);

        this.connections.add(new ConnectionGene(randomNodeInput1,  this.getOutputNodes().get(0)));
        this.connections.add(new ConnectionGene(randomNodeInput2,  this.getOutputNodes().get(1)));*/


            //A connection between a random input and output
            NodeGene randomNodeInput1 = inputNodes.get(Random.getRandomIntegerValue(inputNodes.size()));
            NodeGene randomNodeOutput = outputNodes.get(Random.getRandomIntegerValue(outputNodes.size()));

            this.connections.add(new ConnectionGene(randomNodeInput1, randomNodeOutput));
        }

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
        int chanceWeightMutation = Random.getRandomIntegerValue(80);
        int chanceAddConnection = Random.getRandomIntegerValue(10);
        int chanceAddNode = Random.getRandomIntegerValue(10);

        if(chanceWeightMutation < 100){
           this.changeWeightMutation(0.2);
        }
        if(chanceAddConnection < 100){
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
        }while(nodeGene2.getType().getInt() == nodeGene1.getType().getInt() && nodeGene1.getType() != NodeGene.Type.HIDDEN);

        //Put nodeGene1 and nodeGene2 in the correct order

        if(nodeGene1.getType().getInt() > nodeGene2.getType().getInt()){
            NodeGene tmp = nodeGene1;
            nodeGene1 = nodeGene2;
            nodeGene2 = tmp;
        }

        //Check if the connection already exist, else redo the process of adding node
        for(ConnectionGene connection : connections){
            if(connection.getInput() == nodeGene1 && connection.getOutput() == nodeGene2){
                addConnectionMutation();
                return;
            }
        }

        //If the connection doesn't already exist, create it
        this.connections.add(new ConnectionGene(nodeGene1, nodeGene2));
    }

    /**
     * Take a random connection and put a new node in between.
     * It creates two new connections and the old connection is set to expressed(false)
     */
    private void addNodeMutation(){
        ConnectionGene connection = connections.get(Random.getRandomIntegerValue(connections.size()));
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
        for(int i = 0 ; i < transmitters.size() ; i++){
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
    public void draw(Graphics2D g, Dimension panelDimensions) {

        ArrayList<NodeGene> inputNodes = this.getInputNodes();
        ArrayList<NodeGene> outputNodes = this.getOutputNodes();
        ArrayList<NodeGene> hiddenNodes = this.getHiddenNodes();
        HashMap<NodeGene, Point> nodePositions = new HashMap<>();

        ArrayList<ArrayList<NodeGene>> topology = new ArrayList<>();
        topology.add(inputNodes);
        topology.add(hiddenNodes);
        topology.add(outputNodes);

        final int gapX = 10;
        final int gapY = 10;
        final int neuronneGandeur = 30;

        int plusGrandNbNeuronne = 0;
        plusGrandNbNeuronne = Math.max(Math.max(inputNodes.size(), outputNodes.size()), hiddenNodes.size());

        final int minGapY = (int) ((panelDimensions.height - (gapY * 2 + neuronneGandeur)) / (double)(plusGrandNbNeuronne-1));
        final int maxGandeur = (panelDimensions.height - (gapY * 2 + neuronneGandeur));
        g.setColor(Color.BLACK);

        ArrayList<ArrayList<Point>> allNeurons = new ArrayList<>();

        //Get the neurons in an array
        for(int i = 0; i < topology.size(); i++){
            int neuronCount = topology.get(i).size();
            ArrayList<Point> neurons = new ArrayList<>();
            for(int j = 0; j < neuronCount; j++){
                int thisMaxGrandeur = minGapY * (neuronCount-1) ;

                int x = (int) (gapX + neuronneGandeur/2 + ((double)(panelDimensions.width - (gapX*2 + neuronneGandeur))/(double)(topology.size()-1))*(double)i);
                int y = (maxGandeur-thisMaxGrandeur)/2+ gapY + neuronneGandeur/2 + (minGapY) * j;

                neurons.add(new Point(x-neuronneGandeur/2, y-neuronneGandeur/2));

            }
            allNeurons.add(neurons);
        }
        HashMap<NodeGene, Point[]> allConnections = new HashMap<>();

        for(ConnectionGene connectionGene : connections){
            if(!connectionGene.isExpresed()){
                continue;
            }

            NodeGene input = connectionGene.getInput();
            NodeGene output = connectionGene.getOutput();

            for(int i = 0; i < topology.size(); i++){
                for(int j = 0; j < topology.get(i).size(); j++){
                    if(input == topology.get(i).get(j)){
                        if(!allConnections.containsKey(output))
                            allConnections.put(input, new Point[]{allNeurons.get(i).get(j), null});
                        else
                            allConnections.get(output)[0] = allNeurons.get(i).get(j);
                    }

                    else if(output == topology.get(i).get(j)){
                        if(!allConnections.containsKey(input))
                            allConnections.put(output, new Point[]{null, allNeurons.get(i).get(j)});
                        else
                            allConnections.get(input)[1] = allNeurons.get(i).get(j);
                    }
                }
            }


        }

        allConnections.forEach((n, p) -> {
            if(p[0] != null && p[1] != null)
            g.drawLine(p[0].x + neuronneGandeur/2, p[0].y + neuronneGandeur/2, p[1].x + neuronneGandeur/2, p[1].y+neuronneGandeur/2);
        });


        //draw the neurons
        for(int i = 0; i < topology.size(); i++){
            ArrayList<NodeGene> layer = topology.get(i);
            for(int j = 0; j < layer.size(); j++){
                Point p = allNeurons.get(i).get(j);
                g.setColor(Color.white);
                g.fillOval(p.x, p.y, neuronneGandeur, neuronneGandeur);
                int red = 0, green = 0;
                if(layer.get(j).getValue() > .5) green = (int) ((layer.get(j).getValue() - .5) * 255 * 2);
                else red = 255-(int) (layer.get(j).getValue() *255*2);
                g.setColor(new Color(red,green,0));
                g.drawOval(p.x, p.y, neuronneGandeur, neuronneGandeur);
                g.drawString(String.format("%.2f", layer.get(j).getValue()), p.x + neuronneGandeur/2-10, p.y+neuronneGandeur/2+5);

                g.setColor(Color.BLACK);

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
