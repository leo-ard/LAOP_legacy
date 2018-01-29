package espece.network;

import utils.Random;

public class Connection {
	
	private double weight;
	private Neuron neuronInput;
	
	public Connection(float w) {
		this.weight = w;
	}
	
	public Connection(Neuron neuronInput) {
		this.weight = Random.getRandomDoubleValue(0.2, 1);
		this.neuronInput = neuronInput;
	}
	
	public double getWeight() {
		return weight;
	}

	public Neuron getNeuron() {
		// TODO Auto-generated method stub
		return neuronInput;
	}

	public void setWeight(double weight) {
		this.weight = weight;
		
	}

}
