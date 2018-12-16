package org.lrima.network.algorithms.fullyconnected;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;

import org.apache.commons.math3.linear.MatrixUtils;
import org.lrima.network.interfaces.NeuralNetwork;
import org.lrima.network.interfaces.NeuralNetworkReceiver;
import org.lrima.network.interfaces.NeuralNetworkTransmitter;
import org.apache.commons.math3.linear.RealMatrix;
import org.lrima.Interface.options.OptionsDisplayPanel;
import org.lrima.Interface.options.Option;
import org.lrima.utils.Random;

public class FullyConnectedNeuralNetwork extends NeuralNetwork implements Serializable {
	private Genotype genotype;
	private int[] topology;

	public FullyConnectedNeuralNetwork(LinkedHashMap<String, Option> options) {
		super(options);
	}

	private FullyConnectedNeuralNetwork(LinkedHashMap<String, Option> options, Genotype genotype){
		super(options);
		this.genotype = genotype;
	}

	@Override
	public void init(ArrayList<? extends NeuralNetworkTransmitter> transmitters, NeuralNetworkReceiver receiver) {
		super.init(transmitters,receiver);

		int numberOfLayers = (int)this.options.get("NUMBER_OF_LAYERS").getValue();
		int deepLevel = (int) this.options.get("DEEP_LEVEL").getValue();

		this.topology = new int[numberOfLayers + 2]; //the input and output layer

		this.topology[0] = transmitters.size();
		for(int i = 1; i <= numberOfLayers; i++)
			this.topology[i] = deepLevel;
		this.topology[this.topology.length-1] = receiver.getSize();

		genotype = new Genotype(this.topology);
	}

	@Override
	public FullyConnectedNeuralNetwork crossOver(NeuralNetwork network1, NeuralNetwork network2) {
		FullyConnectedNeuralNetwork parent1 = (FullyConnectedNeuralNetwork) network1;
		FullyConnectedNeuralNetwork parent2 = (FullyConnectedNeuralNetwork) network2;

		Genotype mutatedGenotype = parent1.getGenotype().getChildren(parent2.getGenotype());
		return new FullyConnectedNeuralNetwork(options, mutatedGenotype);
	}

	private Genotype getGenotype() {
		return this.genotype;
	}

	public void feedForward(){
		double[] values = this.transmitters.stream().mapToDouble(NeuralNetworkTransmitter::getNeuralNetworkInput).toArray();

		Layer previousLayer = new Layer(values, genotype.getSubsetForLayer(1));
		for(int i = 1; i < topology.length-1; i++){
			Layer currentLayer = new Layer(topology[i], genotype.getSubsetForLayer(i));

			currentLayer.calculateSum(previousLayer);
			previousLayer = currentLayer;
		}

		receiver.setNeuralNetworkOutput(previousLayer.getOutput());
	}

	@Override
	public void generationFinish() {
        this.genotype.mutate(this.mutationChance, this.weightModificationChance);
	}

	@Override
	public void draw(Graphics2D g, Dimension panelDimensions) {
        final int gapX = 10;
        final int gapY = 10;
        final int neuronneGandeur = 30;

        int plusGrandNbNeuronne = 0;
        for(int neuronCount : this.topology)
            if(neuronCount > plusGrandNbNeuronne)
                plusGrandNbNeuronne = neuronCount;

        final int minGapY = (int) ((panelDimensions.height - (gapY * 2 + neuronneGandeur)) / (double)(plusGrandNbNeuronne-1));
        final int maxGandeur = (panelDimensions.height - (gapY * 2 + neuronneGandeur));
        g.setColor(Color.BLACK);

        ArrayList<ArrayList<Point>> allNeurons = new ArrayList<>();

        //Get the neurons in an array
        for(int i = 0; i < this.topology.length; i++){
            int neuronCount = this.topology[i];
            ArrayList<Point> neurons = new ArrayList<>();
            for(int j = 0; j < neuronCount; j++){
                int thisMaxGrandeur = minGapY * (neuronCount-1) ;

                int x = (int) (gapX + neuronneGandeur/2 + ((double)(panelDimensions.width - (gapX*2 + neuronneGandeur))/(double)(this.topology.length-1))*(double)i);
                int y = (maxGandeur-thisMaxGrandeur)/2+ gapY + neuronneGandeur/2 + (minGapY) * j;

                neurons.add(new Point(x-neuronneGandeur/2, y-neuronneGandeur/2));
            }
            allNeurons.add(neurons);
        }

        double[] values = this.transmitters.stream().mapToDouble(NeuralNetworkTransmitter::getNeuralNetworkInput).toArray();
        Layer firstLayer = new Layer(values, genotype.getSubsetForLayer(1));
        ArrayList<Layer> layers = new ArrayList<>();
        layers.add(firstLayer);

        //Calculate the values of the layer
        for(int i = 1; i < this.topology.length; i++){
            Layer l = new Layer(topology[i], this.genotype.getSubsetForLayer(i));
            l.calculateSum(layers.get(i-1));
            layers.add(l);
        }

        //draws the lines
        for(int i = 1; i < layers.size(); i++){
            for(int j = 0; j < allNeurons.get(i).size(); j++)
                for(int k = 0; k < allNeurons.get(i-1).size(); k++){
                    Point p = allNeurons.get(i).get(j);
                    Point p2 =allNeurons.get(i-1).get(k);
                    g.drawLine(p.x + neuronneGandeur/2, p.y + neuronneGandeur/2, p2.x + neuronneGandeur/2, p2.y+neuronneGandeur/2);
                }
        }

        //draw the neurons
        for(int i = 0; i < layers.size(); i++){
            Layer layer = layers.get(i);
            for(int j = 0; j < layer.getOutput().length; j++){
                Point p = allNeurons.get(i).get(j);
                g.setColor(Color.white);
                g.fillOval(p.x, p.y, neuronneGandeur, neuronneGandeur);
                int red = 0, green = 0;
                if(layer.getOutput()[j] > .5) green = (int) ((layer.getOutput()[j] - .5) * 255 * 2);
                else red = 255-(int) (layer.getOutput()[j] *255*2);
                g.setColor(new Color(red,green,0));
                g.drawOval(p.x, p.y, neuronneGandeur, neuronneGandeur);
                g.drawString(String.format("%.2f", layer.getOutput()[j]), p.x + neuronneGandeur/2-10, p.y+neuronneGandeur/2+5);

                g.setColor(Color.BLACK);

            }
        }

    }

}
