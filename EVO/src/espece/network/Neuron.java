package espece.network;

import java.util.ArrayList;

public class Neuron {
	private ArrayList<Connection> inputs;
	private Layer layer;
	private int index;
	private double value;
	
	public Neuron(Layer l, int index) {
		this.layer = l;
		this.index = index;
		inputs = new ArrayList<Connection>();
	}
	
	public Neuron(Layer l, int index, double value) {
		this(l, index);
		this.value = value;
	}
	
	public void calculateWeightedSum(Neuron bias) {
		float sum = 0;
		sum += bias.getValue();
		for(Connection c : inputs) {
			sum += c.getWeight() * c.getNeuron().getValue();
		}
		this.value = sum;
	}
	
	public void setValue(double value) {
		this.value=value;
	}
	
	public double getValue() {
		return this.value;
	}

	public void addConnection(Connection c) {
		inputs.add(c);
	}

	public ArrayList<Connection> getConnections() {
		return inputs;
	}

	public Layer getLayer() {
		return layer;
	}

	public int getIndex() {
		return index;
	}

}
