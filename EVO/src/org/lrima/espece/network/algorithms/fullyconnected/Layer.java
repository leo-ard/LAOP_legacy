package org.lrima.espece.network.algorithms.fullyconnected;


import java.util.ArrayList;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.lrima.espece.network.functions.Function;
import org.lrima.utils.Random;

public class Layer{
    protected RealMatrix weights;
    protected ArrayList<Neuron> neurons = new ArrayList<>();

    public Layer(int nbNodes){
        for(int i = 0 ; i < nbNodes ; i++){
            this.neurons.add(new Neuron());
        }

        this.weights = MatrixUtils.createRealIdentityMatrix(nbNodes);
    }

    public void initWeights(Layer nextLayer){
        //init the weights matrices for the number of layer - 1
        int numberOfNodeFirstLayer = neurons.size();
        int numberOfNodeSecondLayer = nextLayer.getNeuronsAsArray().length;
        double[][] matrix = new double[numberOfNodeSecondLayer][numberOfNodeFirstLayer];
        matrix = randomizeMatrix(matrix);
        weights = MatrixUtils.createRealMatrix(matrix);
    }

    public Layer clone(){
        Layer layer = new Layer(this.neurons.size());
        layer.setWeights(this.weights.copy());

        return layer;
    }

    private void addBias(){
        this.neurons.add(new Neuron(1));
    }

    public void mutate(){
        int chance_randomWeigthChange = Random.getRandomIntegerValue(0, 100);
		int chance_muteRandomConnection = Random.getRandomIntegerValue(0, 100);

		if(chance_randomWeigthChange <= 2){
			Mutation.mutateRandomWeights(this, 20, 0.4);
		}
		if(chance_muteRandomConnection <= 10){
			Mutation.muteRandomConnection(this);
		}
    }

    public double[] calculateWeightedSum(){
        try {
            RealMatrix inputMatrix = MatrixUtils.createColumnRealMatrix(getNeuronsAsArray());
            RealMatrix outputMatrix = weights.multiply(inputMatrix);

            outputMatrix = sigmoid(outputMatrix);

            return outputMatrix.getColumn(0);
        }catch (Exception e){
            return null;
        }
    }

    private RealMatrix sigmoid(RealMatrix matrix){
        RealMatrix newMatrix = matrix.copy();

        for(int i = 0 ; i < matrix.getColumnDimension() ; i++){
            for(int j = 0 ; j < matrix.getRowDimension() ; j++){
                double value = Function.SIGMOID.getValue(matrix.getEntry(j, i));
                newMatrix.setEntry(j, i, value);
            }
        }

        return newMatrix;
    }

    public void setInputs(double[] inputs){
        neurons = new ArrayList<>();

        for(double input : inputs){
            neurons.add(new Neuron(input));
        }
    }

    public double[] getNeuronsAsArray(){
        double[] returnNeurons = new double[neurons.size()];

        for(int i = 0 ; i < returnNeurons.length ; i++){
            returnNeurons[i] = neurons.get(i).getValue();
        }

        return returnNeurons;
    }

    /**
     * Sets all the values of a 2D array randomly from -1 to 1
     * @param matrix the 2D array to randomize
     * @return a new matrix containing the randomized values
     */
    private double[][] randomizeMatrix(double[][] matrix){
        double[][] newMatrix = new double[matrix.length][matrix[0].length];

        for(int i = 0 ; i < matrix.length ; i++){
            for(int j = 0 ; j < matrix[0].length ; j++){
                double weigth = Random.getRandomDoubleValue(-1, 1);
                newMatrix[i][j] = weigth;
            }
        }

        return newMatrix;
    }

    public RealMatrix getWeights() {
        return weights;
    }

    public void setWeights(RealMatrix weights) {
        this.weights = weights;
    }
}

/*public class Layer implements Iterable<Neuron> {
	private Neuron bias;
	private ArrayList<Node> neurons;
	private int index;
	
	public Layer(int index) {
		neurons = new ArrayList<Node>();
		bias = new Neuron(this, 0, Random.getRandomFloatValue(1));
		this.index = index;
	}
	
	public Layer(Layer l) {
		neurons = new ArrayList<Node>();
		for(Node n : l.neurons) {
			//neurons.add(new Neuron(this, n));
		}
		bias = new Neuron(this, 0, l.bias.getValue());
		this.index = index;
	}

	public Neuron addNeuron() {
		Neuron n = new Neuron(this, neurons.size());
		//neurons.add(n);
		return n;
	}
	
	/*public Neuron getNeuron(int nb) {
		return neurons.get(nb);
	}*/

	/*public void setValues(double[] inputs) {
		for(int i = 0; i < inputs.length; i++) {
			this.neurons.get(i).setValue(inputs[i]);
		}
		
	}*/

	/*public void update() {
		for(Neuron n : neurons) {
			n.calculateWeightedSum(bias);
		}
		
	}

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
			if(currentConnection.getNeuronInput() == c.getNeuronInput()) {
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

	public ArrayList<Connection> getAllConnections() {
		ArrayList<Connection> con = new ArrayList<Connection>();
		for(Neuron n: neurons) {
			con.addAll(n.getConnections());
		}
		return con;
	}

	public ArrayList<Neuron> getNeurons() {
		return neurons;
	}
}*/
