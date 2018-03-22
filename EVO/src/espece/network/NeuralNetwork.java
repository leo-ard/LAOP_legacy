package espece.network;

import java.util.ArrayList;

import javax.swing.JFrame;

import espece.Espece;
import utils.Random;

public class NeuralNetwork {
	boolean[] things = new boolean[10];

	// FOR TESTS
	public static void main(String args[]) {
		for(int i = 10; i > 0 ; i--) {
			JFrame f = new JFrame("TEST");
			NeuralNetwork nn = new NeuralNetwork(3, 2);
			
	
			nn.addRandomConnection();
			nn.addRandomConnection();
			nn.addRandomConnection();
			nn.addRandomConnection();
			
			nn.mutateRandomConnection();
			nn.mutateRandomConnection();
			nn.mutateRandomConnection();
			nn.mutateRandomConnection();
			
			NeuralNetwork n1 = new NeuralNetwork(nn);
			
			nn.printState();
			nn.update(1, 1, 1);
	
			Espece e = new Espece();
			e.neuralNetwork = nn;
			f.add(new NetworkPanel(e, 800, 300));
			f.pack();
			f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			f.setVisible(true);
		}
	}
	
	//for test
	private int nbWeightMut = 0;
	private int nbNeuronMut = 0;

	// for test
	private void printState() {
		System.out.println("=======");
		for (Layer l : this.layers) {
			for (Neuron n : l) {
				String txt = "";
				for (Connection c : n.getConnections()) {
					txt += String.format("<-(%d,%d)", c.getNeuronInput().getLayer().getIndex(),
							c.getNeuronInput().getIndex());
				}
				System.out.printf("(%d,%d)%s \t", l.getIndex(), n.getIndex(), txt);
			}
			System.out.println();
		}
		System.out.println("=======");
	}

	private ArrayList<Layer> layers;
	private int nbInputs;
	private int nbOutputs;

	public NeuralNetwork(NeuralNetwork nn) {
		this(new NetworkStructure(nn));
	}
	
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
		
	}

	public NeuralNetwork(int inputs, int outputs) {
		nbInputs = inputs;
		nbOutputs = outputs;
		layers = new ArrayList<Layer>();
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

		/*
		 * for(Neuron neuron1:layers.get(0)) { for(Neuron neuron2:layers.get(1)) {
		 * neuron2.addConnection(new Connection(neuron1)); } }
		 */
		// this.addRandomConnection();

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
		int ran = Random.getRandomIntegerValue(20);
		
		if (ran <= 5)
			this.addRandomConnection();
		else if(ran <= 10)
			this.mutateRandomConnection();
		else 
			this.minimalModificationWeight();
	}
	
	public void minimalModificationWeight() {
		int ran = Random.getRandomIntegerValue(this.getAllConnections().size());
		Connection c = this.getAllConnections().get(ran);
		double ranModifi = Random.getRandomDoubleValue(-0.2, 0.2);
		c.setWeight(c.getWeight()+ranModifi);
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

}
