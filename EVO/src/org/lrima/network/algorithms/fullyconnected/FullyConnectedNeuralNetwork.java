package org.lrima.network.algorithms.fullyconnected;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;

import org.apache.commons.math3.linear.MatrixUtils;
import org.lrima.network.interfaces.NeuralNetwork;
import org.lrima.network.interfaces.NeuralNetworkReceiver;
import org.lrima.network.interfaces.NeuralNetworkTransmitter;
import org.apache.commons.math3.linear.RealMatrix;
import org.lrima.Interface.options.OptionsDisplayPanel;
import org.lrima.Interface.options.Option;
import org.lrima.utils.Random;

public class FullyConnectedNeuralNetwork extends NeuralNetwork implements Serializable {
	private Genotype genotype;
	private int[] topology;

	public FullyConnectedNeuralNetwork(LinkedHashMap<String, Option> options) {
		super(options);
	}

	private FullyConnectedNeuralNetwork(LinkedHashMap<String, Option> options, Genotype genotype){
		super(options);
		this.genotype = genotype;
	}

	@Override
	public void init(ArrayList<? extends NeuralNetworkTransmitter> transmitters, NeuralNetworkReceiver receiver) {
		super.init(transmitters,receiver);

		int numberOfLayers = (int)this.options.get("NUMBER_OF_LAYERS").getValue();
		int deepLevel = (int) this.options.get("DEEP_LEVEL").getValue();

		/*this.topology = new int[numberOfLayers + 2]; //the input and output layer

		this.topology[0] = transmitters.size();
		for(int i = 1; i <= numberOfLayers; i++)
			this.topology[i] = deepLevel;
		this.topology[this.topology.length-1] = receiver.getSize();*/

		this.topology = new int[]{6, 4, 2};

		genotype = new Genotype(this.topology);
	}

	@Override
	public FullyConnectedNeuralNetwork crossOver(NeuralNetwork network1, NeuralNetwork network2) {
		FullyConnectedNeuralNetwork parent1 = (FullyConnectedNeuralNetwork) network1;
		FullyConnectedNeuralNetwork parent2 = (FullyConnectedNeuralNetwork) network2;

		Genotype mutatedGenotype = parent1.getGenotype().getChildren(parent2.getGenotype());
		return new FullyConnectedNeuralNetwork(options, mutatedGenotype);
	}

	private Genotype getGenotype() {
		return this.genotype;
	}

	public void feedForward(){
		double[] values = this.transmitters.stream().mapToDouble(NeuralNetworkTransmitter::getNeuralNetworkInput).toArray();

		Layer previousLayer = new Layer(values, genotype.getSubsetForLayer(1));
		for(int i = 1; i < topology.length-1; i++){
			Layer currentLayer = new Layer(topology[i], genotype.getSubsetForLayer(i));

			currentLayer.calculateSum(previousLayer);
			previousLayer = currentLayer;
		}



		receiver.setNeuralNetworkOutput(previousLayer.getOutput());

	}

	@Override
	public void generationFinish() {

	}

	@Override
	public void draw(Graphics2D g, Dimension panelDimensions) {

	}

}
