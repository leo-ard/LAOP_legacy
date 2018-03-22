package espece.network;

import java.util.ArrayList;
import java.util.Iterator;

public class NetworkStructure {
	
	private int layerSizes[];
	private double connectionWeight[];
	private int connectionPos[][];

	public NetworkStructure(NeuralNetwork nn) {
		layerSizes = new int[nn.getSize()];
		for(int i = 0; i < layerSizes.length; i++) {
			layerSizes[i] = nn.getAllLayers().get(i).getSize();
		}
		
		connectionWeight = new double[nn.getAllConnections().size()];
		connectionPos = new int[nn.getAllConnections().size()][4];
		
		for(int i = 0; i < nn.getAllConnections().size(); i++) {
			Connection c = nn.getAllConnections().get(i);
			connectionWeight[i] = c.getWeight();
			connectionPos[i][0] = c.getNeuronInput().getLayer().getIndex();
			connectionPos[i][1] = c.getNeuronInput().getIndex();
			connectionPos[i][2] = c.getNeuronOutput().getLayer().getIndex();
			connectionPos[i][3] = c.getNeuronOutput().getIndex();
		}
		
	}
	
	public NeuralNetwork getNeuralNetwork() {
		return new NeuralNetwork(this);
	}

	public int[] getLayerSizes() {
		return layerSizes;
	}

	public double[] getConnectionWeight() {
		return connectionWeight;
	}

	public int[][] getConnectionPos() {
		return connectionPos;
	}

}
