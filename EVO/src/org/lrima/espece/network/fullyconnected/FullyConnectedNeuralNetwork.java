package org.lrima.espece.network.fullyconnected;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;

import org.apache.commons.math3.linear.MatrixUtils;
import org.lrima.espece.network.exceptions.NeuralNetworkNotInitiated;
import org.lrima.espece.network.interfaces.NeuralNetwork;
import org.lrima.espece.network.interfaces.NeuralNetworkReceiver;
import org.lrima.espece.network.interfaces.NeuralNetworkTransmitter;
import org.apache.commons.math3.linear.RealMatrix;

public class FullyConnectedNeuralNetwork implements Serializable, NeuralNetwork {
	//private ArrayList<Layer> layers;
	private Integer nbInputs = 0;
	private Integer[] nbHidden;
	private Integer nbOutputs = 0;
	private ArrayList<Layer> layers = new ArrayList<>();

	private ArrayList<RealMatrix> layerInputs = new ArrayList<>();
	private ArrayList<RealMatrix> layerOutputs = new ArrayList<>();
	private ArrayList<RealMatrix> weigthMatrices = new ArrayList<>();

	private boolean hasBeenInitiated = false;

	private ArrayList<?extends NeuralNetworkTransmitter> transmitters;
	private NeuralNetworkReceiver receiver;

	public static void main(String[] args) {

	}

	public FullyConnectedNeuralNetwork(ArrayList<Layer> layers, ArrayList<?extends NeuralNetworkTransmitter> transmitters, NeuralNetworkReceiver receiver){
		this(transmitters, receiver);
		this.layers = layers;
	}

	public FullyConnectedNeuralNetwork(int numberOfInputNodes, int numberOfOutputNodes, Integer ... numberOfHiddenNodes) {
		this.init(numberOfInputNodes, numberOfOutputNodes, numberOfHiddenNodes);
	}

	private FullyConnectedNeuralNetwork(ArrayList<?extends NeuralNetworkTransmitter> transmitters, NeuralNetworkReceiver receiver) {
		this.transmitters = transmitters;
		this.receiver = receiver;
		this.init(transmitters.size(), 2, new Integer[]{4, 3});
	}

	/**
	 * Initialize the variables of the neural network. It sets the number of input, hidden and output nodes.
	 * It also sets the value of each neuron randomly
	 */
	private void init(int numberOfInputNodes, int numberOfOutputNodes, Integer ... numberOfHiddenNodes){
		//Check that the number of nodes is valid
		if(numberOfInputNodes <= 0){
			System.err.println("Initialization failed. " + numberOfInputNodes + " is not valid for the number of input nodes.");
			return;
		}
		for(int hiddenNodes : numberOfHiddenNodes){
			if(hiddenNodes <= 0){
				System.err.println("Initialization failed. " + hiddenNodes + " is not valid for the number of hidden nodes.");
				return;
			}
		}
		if(numberOfOutputNodes <= 0){
			System.err.println("Initialization failed. " + numberOfOutputNodes + " is not valid for the number of output nodes.");
			return;
		}

		//Setup the number of nodes in each of the layers
		this.nbInputs = numberOfInputNodes;
		this.nbHidden = numberOfHiddenNodes;
		this.nbOutputs = numberOfOutputNodes;

		this.layers.add(new Layer(nbInputs));
		for(int i = 0 ; i < numberOfHiddenNodes.length ;i++){
			this.layers.add(new Layer(numberOfHiddenNodes[i]));
		}
		this.layers.add(new Layer(nbOutputs));

		for(int i = 0 ; i < layers.size() - 1 ; i++){
			layers.get(i).initWeights(layers.get(i + 1));
		}

		this.hasBeenInitiated = true;
	}

	public void draw(Graphics2D graphics){

	}

	@Override
	public void init(ArrayList<? extends NeuralNetworkTransmitter> transmitters, NeuralNetworkReceiver receiver) {

	}

	public void feedForward(){
		try {
			RealMatrix output = this.query(getTransmitterValue());
			this.receiver.setNeuralNetworkOutput(output.getRow(0));
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * Used to get the value of all the transmitter into an array
	 * @return an array containing all the transmitter values
	 */
	private double[] getTransmitterValue(){
		double[] values = new double[this.transmitters.size()];
		for(int i = 0 ; i < transmitters.size() ; i++){
			NeuralNetworkTransmitter transmitter = transmitters.get(i);
			values[i] = transmitter.getNeuralNetworkInput();
		}

		return values;
	}

	public RealMatrix query(double[] inputs) {
			double[] output = inputs;

			for(int i = 0 ; i < layers.size() ; i++){
				Layer layer = layers.get(i);
				layer.setInputs(output);
				output = layer.calculateWeightedSum();
			}

			return MatrixUtils.createRowRealMatrix(output);

	}

	private void setupInputs(double[] inputs){
		this.layerInputs = new ArrayList<>();
		this.layerOutputs = new ArrayList<>();

		//Setup the input for the input layer
		double[] inputsWithBias = new double[inputs.length + 1];
		for(int i = 0 ; i < inputs.length - 1 ; i++){
			inputsWithBias[i] = inputs[i];
		}
		inputsWithBias[inputsWithBias.length - 1] = 1.0;

		this.layerInputs.add(MatrixUtils.createColumnRealMatrix(inputsWithBias));
		this.layerOutputs.add(layerInputs.get(0).copy());
	}


	public void mutate(){
		for(Layer layer : this.layers){
			layer.mutate();
		}
	}

	@Override
	public void minimalMutation(double delta) {

	}

	public void setLayers(ArrayList<Layer> layers) {
		this.layers = layers;
	}

	protected ArrayList<Layer> getLayers() {
		return layers;
	}

	public ArrayList<RealMatrix> getWeigthMatrices() {
		return weigthMatrices;
	}

	public void setWeigthMatrices(ArrayList<RealMatrix> weigthMatrices) {
		this.weigthMatrices = weigthMatrices;
	}
}
