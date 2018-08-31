package org.lrima.espece.network;

import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.JFrame;

import org.lrima.espece.Espece;
import org.lrima.utils.Random;

public class NeuralNetwork implements Serializable {
	boolean[] things = new boolean[10];
	int mutationRate = 100;
	private double fitness;

	//for test
	private int nbWeightMut = 0;
	private int nbNeuronMut = 0;

	private ArrayList<Layer> layers;
	private int nbInputs;
	private int nbOutputs;

	
	public NeuralNetwork(NetworkStructure ns) {
		this.nbInputs = ns.getLayerSizes()[0];
		this.nbOutputs = ns.getLayerSizes()[ns.getLayerSizes().length-1];
		
		layers = new ArrayList<Layer>();
		for(int i = 0; i < ns.getLayerSizes().length; i++) {
			layers.add(new Layer(i));
			for(int j = 0; j < ns.getLayerSizes()[i]; j++) {
				layers.get(i).addNeuron();
			}
		}
		
		for(int i = 0; i < ns.getConnectionWeight().length; i++) {
			int inputLayerIndex = ns.getConnectionPos()[i][0];
			int inputNeuronIndex = ns.getConnectionPos()[i][1];
			int outputLayerIndex = ns.getConnectionPos()[i][2];
			int outputNeuronIndex = ns.getConnectionPos()[i][3];
			double connectionWeight = ns.getConnectionWeight()[i];
			
			Connection c = new Connection(layers.get(inputLayerIndex).getNeuron(inputNeuronIndex), layers.get(outputLayerIndex).getNeuron(outputNeuronIndex));
			layers.get(outputLayerIndex).addConnection(outputNeuronIndex, c);
			
		}

		this.fitness = ns.fitness;
	}

	public NeuralNetwork(int inputs, int outputs, boolean allconnection) {
        nbInputs = inputs;
        nbOutputs = outputs;
        layers = new ArrayList<Layer>();
        createDefaultLayers();
        if(!allconnection) {
			this.addRandomConnection();
		}
		else{
        	this.addAllConnection();
		}
    }

    public NeuralNetwork(int inputs,int hidden, int outputs) {
        nbInputs = inputs;
        nbOutputs = outputs;
        layers = new ArrayList<Layer>();

        for(int i = 0; i < hidden; i++) {
            layers.add(new Layer(i));
            /*for(int j = 0; j < ns.getLayerSizes()[i]; j++) {
                layers.get(i).addNeuron();
            }*/
        }

        createDefaultLayers();
        this.addRandomConnection();
    }

	public void update(double... inputs) {
		layers.get(0).setValues(inputs);

		for (int i = 1; i < layers.size(); i++) {
			layers.get(i).update();
		}
	}

	public double[] getOutputValues() {
		return this.layers.get(layers.size() - 1).getNeuronValues();
	}

	private void createDefaultLayers() {
		Layer layer1 = new Layer(0);
		for (int i = 0; i < this.nbInputs; i++) {
			layer1.addNeuron();
		}
		this.layers.add(layer1);

		Layer layer2 = new Layer(1);
		for (int i = 0; i < this.nbOutputs; i++) {
			layer2.addNeuron();
		}
		this.layers.add(layer2);

	}

	private void addRandomConnection() {
		this.nbWeightMut++;
		int chosenLayerOutput = Random.getRandomIntegerValue(1, layers.size());
		int chosenNeuronOutput = Random.getRandomIntegerValue(layers.get(chosenLayerOutput).getSize());

		int chosenLayerInput = Random.getRandomIntegerValue(chosenLayerOutput);
		int chosenNeuronInput = Random.getRandomIntegerValue(layers.get(chosenLayerInput).getSize());

		// System.out.printf("%d:%d %d:%d", chosenLayerInput, chosenNeuronInput,
		// chosenLayerOutput, chosenNeuronOutput);

		Connection c = new Connection(this.layers.get(chosenLayerInput).getNeuron(chosenNeuronInput),
		this.getLayer(chosenLayerOutput).getNeuron(chosenNeuronOutput));
		this.layers.get(chosenLayerOutput).addConnection(chosenNeuronOutput, c);
	}

	private void addAllConnection() {
		for(int i = 0 ; i < nbInputs ; i++){
			for(int j = 0 ; j < nbOutputs ; j++){

				Connection c = null;

				if(i < Math.ceil(nbInputs / 2) && j < 1) {
					c = new Connection(this.layers.get(0).getNeuron(i),
							this.getLayer(1).getNeuron(0));
				}
				else if(i >= Math.floor(nbInputs / 2) && j >= 1){
					c = new Connection(this.layers.get(0).getNeuron(i),
							this.getLayer(1).getNeuron(1));
				}

				if(c != null) {
					this.layers.get(1).addConnection(j, c);
				}
			}
		}
	}

	private void mutateRandomConnection() {
		this.nbNeuronMut++;
		ArrayList<Connection> allcon = this.getAllConnections();
		int ran = Random.getRandomIntegerValue(allcon.size());
		Connection con = allcon.get(ran);
		mutateConnection(con);
	}

	private void mutateConnection(Connection c) {
		// System.out.println("--");
		Neuron connectionNeuronInput = c.getNeuronInput();
		int layer = connectionNeuronInput.getLayer().getIndex() + 1;
		int ecart = c.getNeuronOutput().getLayer().getIndex() - connectionNeuronInput.getLayer().getIndex();
		
		if (ecart <= 1) {
			this.addLayer(layer);
		}

		Neuron newNeuron = this.getLayer(layer).addNeuron();

		newNeuron.addConnection(new Connection(connectionNeuronInput, newNeuron));
		c.getNeuronOutput().addConnection(new Connection(newNeuron, c.getNeuronOutput()));
		c.disable();

	}

	public void randomize(){
		for(Connection c : getAllConnections()){
			int ran = Random.getRandomIntegerValue(100);
			c.setWeight(Random.getRandomDoubleValue(0, 1));
			if(ran < 10){
				double ranWeight = Random.getRandomDoubleValue(0, 1);
				c.setWeight(ranWeight);
			}
		}
	}

	private void addLayer(int nb) {
		// System.out.println("Adding Laya");
		this.layers.add(nb, new Layer(nb));
		for (int i = nb + 1; i < layers.size(); i++) {
			layers.get(i).setIndex(i);
		}
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

	public void mutate() {
        int ran = Random.getRandomIntegerValue(100);

        if(ran < 5){
        	//this.weightModification(0.1, false);
		}

        /*if (ran <= 10)
            this.addRandomConnection();
            if (ran <= 5){
                mutateRandomConnection();
                if(ran <= 2){
                    this.weightModification(0.7, false);
                }
            }
        else {
            this.weightModification(0.1, false);
        }*/

		/*int ran = Random.getRandomIntegerValue(100);
		if(ran < 5) {
			weightModification(0.1, false);
		}*/
	}

	public void weightModification(double magnitude, boolean allConnections){
		if(!allConnections) {
			int ran = Random.getRandomIntegerValue(this.getAllConnections().size());
			Connection c = this.getAllConnections().get(ran);
			double ranModifi = Random.getRandomDoubleValue(-magnitude, magnitude);
			c.setWeight(c.getWeight() + ranModifi);
		}
		else{
			for(Connection c : getAllConnections()){
				double ranModifi = Random.getRandomDoubleValue(-magnitude, magnitude);
				c.setWeight(c.getWeight() + ranModifi);
			}
		}
	}


	public int nbConnections() {
		int nb = 0;
		for (Layer l : layers) {
			nb += l.getAllConnections().size();
		}
		return nb;
	}

	public ArrayList<Connection> getAllConnections() {
		ArrayList<Connection> nb = new ArrayList<Connection>();
		for (Layer l : layers) {
			nb.addAll(l.getAllConnections());
		}
		return nb;
	}

	public String toString() {
		return String.format("[inputs : %d, outputs : %d, connections : %d, adress: %s]", nbInputs, nbOutputs,
				this.nbConnections(), super.toString().split("@")[1]);
	}

	public ArrayList<Layer> getAllLayers() {
		return layers;
	}

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    public double getFitness() {
        return fitness;
    }

	public int getNbInputs() {
		return nbInputs;
	}

	public int getNbOutputs() {
		return nbOutputs;
	}
}
