package espece.network;

import java.util.ArrayList;

import espece.network.functions.Function;
import espece.network.functions.IFunction;

public class Neuron {
	private ArrayList<Connection> inputs;
	private Layer layer;
	private int index;
	private double value;
	private IFunction function;
	
	public Neuron(Layer l, int index) {
		this.function = Function.getRandom();
		this.layer = l;
		this.index = index;
		this.inputs = new ArrayList<Connection>();
	}
	
	public Neuron(Layer l, int index, double value) {
		this(l, index);
		this.value = value;
	}
	
	public Neuron(Layer l, Neuron n) {
		this.value = n.getValue();
		this.function = n.function;
		this.index = n.index;
		this.layer = l;
		this.inputs = new ArrayList<Connection>();
		for(Connection c : n.inputs) {
			this.inputs.add(new Connection(c.getNeuronInput(), this));
		}
		
	}

	public void calculateWeightedSum(Neuron bias) {
		float sum = 0;
		sum += bias.getValue();
		for(Connection c : inputs) {
			sum += c.getWeight() * c.getNeuronInput().getValue();
		}
		this.value = this.function.getValue(sum);
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
	
	public void addConnection(Neuron n) {
		inputs.add(new Connection(n, this));
	}

	public ArrayList<Connection> getConnections() {
		ArrayList<Connection> con = new ArrayList<Connection>();
		for(Connection c : inputs) {
			if(c.isEnable())
				con.add(c);
		}
		return con;
	}

	public Layer getLayer() {
		return layer;
	}

	public int getIndex() {
		return index;
	}

}
