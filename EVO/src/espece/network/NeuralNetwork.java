package espece.network;

import java.awt.Dimension;
import java.util.ArrayList;

import utils.Random;

public class NeuralNetwork {
	
	private ArrayList<Layer> layers;
	private ArrayList<Connection> allConnections;
	private int nbInputs;
	private int nbOutputs;
	
	public NeuralNetwork(int inputs, int outputs) {
		nbInputs = inputs;
		nbOutputs = outputs;
		layers = new ArrayList<Layer>();
		createDefaultLayers();
	}
	
	public void update(double...inputs) {
		layers.get(0).setValues(inputs);
		
		for(int i = 1; i < layers.size(); i++) {
			layers.get(i).update();
		}
	}
	
	public double[] getOutputValues() {
		return this.layers.get(layers.size()-1).getNeuronValues();
	}
	
	private void createDefaultLayers() {
		Layer layer1 = new Layer(0);
		for(int i = 0; i < this.nbInputs; i++) {
			layer1.addNeuron();
		}
		this.layers.add(layer1);
		
		Layer layer2 = new Layer(1);
		for(int i = 0; i < this.nbOutputs; i++) {
			layer2.addNeuron();
		}
		this.layers.add(layer2);
		
		this.addRandomConnection();
		this.addRandomConnection();
		
	}
	
	private void addRandomConnection() {
		int chosenLayerOutput = Random.getRandomIntegerValue(1, layers.size());
		int chosenNeuronOutput = Random.getRandomIntegerValue(layers.get(chosenLayerOutput).getSize());
		
		int chosenLayerInput = Random.getRandomIntegerValue(chosenLayerOutput);
		int chosenNeuronInput = Random.getRandomIntegerValue(layers.get(chosenLayerInput).getSize());
		
		Connection c = new Connection(this.layers.get(chosenLayerInput).getNeuron(chosenNeuronInput));
		this.layers.get(chosenLayerOutput).addConnection(chosenNeuronOutput, c);
	}
	
	private void addLayer() {
		this.layers.add(layers.size()-2, new Layer(layers.size()-2));
		this.layers.get(layers.size()-1).setIndex(layers.size()-1);
	}
	
	private void addRandomNeuron() {
		if(this.getSize() >=2)
			this.addLayer();
		
	}

	public int getSize() {
		return layers.size();
	}

	public Layer getLayer(int i) {
		return layers.get(i);
	}

	public Neuron getNeuron(int i, int j) {
		return getLayer(i).getNeuron(j);
	}
	
	

}
