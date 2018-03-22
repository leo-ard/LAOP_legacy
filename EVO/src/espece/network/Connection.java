package espece.network;

import utils.Random;

public class Connection {
	
	private double weight;
	private Neuron neuronInput;
	private Neuron neuronOutput;
	private boolean enable;
	
	public Connection(Neuron neuronInput, Neuron neuronOutput, float w) {
		this(neuronInput,neuronOutput);
		this.weight = w;
	}
	
	public Connection(Neuron neuronInput, Neuron neuronOutput) {
		this.weight = Random.getRandomDoubleValue(0, 1);
		this.neuronInput = neuronInput;
		this.enable = true;
		this.neuronOutput = neuronOutput;
	}
	

	public double getWeight() {
		return enable?weight:0;
	}

	public Neuron getNeuronInput() {
		return neuronInput;
	}
	
	public Neuron getNeuronOutput() {
		return neuronOutput;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}
	
	public void disable() {
		enable = false;
	}

	public boolean isEnable() {
		return enable;
	}

}
