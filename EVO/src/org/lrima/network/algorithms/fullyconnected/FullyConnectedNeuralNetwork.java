package org.lrima.network.algorithms.fullyconnected;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.apache.commons.math3.linear.MatrixUtils;
import org.lrima.network.interfaces.NeuralNetwork;
import org.lrima.network.interfaces.NeuralNetworkReceiver;
import org.lrima.network.interfaces.NeuralNetworkTransmitter;
import org.apache.commons.math3.linear.RealMatrix;
import org.lrima.Interface.options.OptionsDisplayPanel;
import org.lrima.Interface.options.Option;

public class FullyConnectedNeuralNetwork extends NeuralNetwork implements Serializable {

	private final Integer[] nbHiddenNodes = {2};

	//private ArrayList<Layer> layers;
	private Integer nbInputs = 0;
	private Integer nbOutputs = 0;
	private ArrayList<Layer> layers = new ArrayList<>();

	private ArrayList<RealMatrix> layerInputs = new ArrayList<>();
	private ArrayList<RealMatrix> layerOutputs = new ArrayList<>();
	private ArrayList<RealMatrix> weigthMatrices = new ArrayList<>();

	public FullyConnectedNeuralNetwork(LinkedHashMap<String, Option> options) {
		super(options);
		OptionsDisplayPanel optionPanel = new OptionsDisplayPanel(this.options);
	}


	public void draw(Graphics2D graphics, Dimension panelDimensions){

	}

	@Override
	public void init(ArrayList<? extends NeuralNetworkTransmitter> transmitters, NeuralNetworkReceiver receiver) {
		this.transmitters = transmitters;
		this.receiver = receiver;

		int numberOfInputNodes = transmitters.size();
		int numberOfOutputNodes = 2;


		//Setup the number of nodes in each of the layers
		this.nbInputs = numberOfInputNodes;
		this.nbOutputs = numberOfOutputNodes;

		this.layers.add(new Layer(nbInputs));
		for(int i = 0 ; i < nbHiddenNodes.length ;i++){
			this.layers.add(new Layer(nbHiddenNodes[i]));
		}
		this.layers.add(new Layer(nbOutputs));

		for(int i = 0 ; i < layers.size() - 1 ; i++){
			layers.get(i).initWeights(layers.get(i + 1));
		}

	}

	@Override
	public NeuralNetwork crossOver(NeuralNetwork network1, NeuralNetwork network2) {
		return new FullyConnectedNeuralNetwork(this.options);
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

	public void generationFinish(){
		for(Layer layer : this.layers){
			layer.mutate();
		}
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
