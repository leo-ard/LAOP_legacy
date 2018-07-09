package org.lrima.espece.network;

import java.io.*;

public class NetworkStructure implements Serializable {
	
	private int layerSizes[];
	private double connectionWeight[];
	private int connectionPos[][];

	public double fitness;

	public NetworkStructure(NeuralNetwork nn) {
		this.fitness = nn.getFitness();

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


    /**
     * Sauvegarde un Neural Network dans un fichier en le transformant en NetworkStructure
     * @param fileName le nom du fichier à créer
     * @param nn le Neural Network à sauvegarder
     */
	static public void save(String fileName, NeuralNetwork nn){
        try {
            FileOutputStream fos = new FileOutputStream(fileName);
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            oos.writeObject( new NetworkStructure(nn) );

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Load un NetworkStructure à partir d'un fichier
     * @param fileName Nom du fichier qui contient le NetworkStructure
     * @return le NetworkStructure sauvegardé
     */
    static public NetworkStructure load(String fileName){
        try{
            FileInputStream fis = new FileInputStream(fileName);
            ObjectInputStream ois = new ObjectInputStream(fis);

            Object object = ois.readObject();
            return (NetworkStructure) object;


        } catch (FileNotFoundException e) {
            System.out.println(fileName + ". File not found");
        } catch (IOException e) {
            System.out.println("Erreur lors de l'ouverture du fichier: " + fileName);
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("Il n'y a pas de classe dans le fichier: " + fileName);
        }

        return null;
    }

}
