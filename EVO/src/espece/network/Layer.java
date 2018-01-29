package espece.network;


import java.util.ArrayList;
import java.util.Iterator;

import utils.Random;

public class Layer implements Iterable<Neuron>{
	private Neuron bias;
	private ArrayList<Neuron> neurons;
	private int index;
	
	public Layer(int index) {
		neurons = new ArrayList<Neuron>();
		bias = new Neuron(this, 0, Random.getRandomFloatValue(1));
		this.index = index;
	}
	
	public void addNeuron() {
		Neuron n = new Neuron(this, neurons.size());
		neurons.add(n);
	}
	
	public Neuron getNeuron(int nb) {
		return neurons.get(nb);
	}

	public void setValues(double[] inputs) {
		for(int i = 0; i < inputs.length; i++) {
			this.neurons.get(i).setValue(inputs[i]);
		}
		
	}

	public void update() {
		for(Neuron n : neurons) {
			n.calculateWeightedSum(bias);
		}
		
	}

	@Override
	public Iterator<Neuron> iterator() {
		return neurons.iterator();
	}

	public double[] getNeuronValues() {
		double[] neuronValues = new double[this.neurons.size()];
		for(int i = 0; i < this.neurons.size(); i++) {
			neuronValues[i] = this.neurons.get(i).getValue();
		}
		
		return neuronValues;
	}

	public int getSize() {
		return this.neurons.size();
	}
	
	public void addConnection(int chosenNeuronOutput, Connection c) {
		for (Connection currentConnection : neurons.get(chosenNeuronOutput).getConnections()) {
			if(currentConnection.getNeuron() == c.getNeuron()) {
				c.setWeight(c.getWeight());
				return;
			}
		}
		this.neurons.get(chosenNeuronOutput).addConnection(c);	
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
	

}
