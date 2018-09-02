package org.lrima.espece.network;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.*;

import org.lrima.espece.Espece;
import org.lrima.simulation.selection.NaturalSelection;

public class NetworkPanel extends JPanel{
		

	/**
	 * 
	 */
	private static final long serialVersionUID = -9032648753101484360L;
	private NeuralNetwork network;
	private Espece espece;
	
	public final int OFFSETX = 10;
	public final int OFFSETY = 10;
	public final int OVALSIZE = 40;
	
	public NetworkPanel(Espece e, int w, int h) {
		super();
		this.setPreferredSize(new Dimension(w, h));
		this.network = e.getNeuralNetwork();
		this.espece = e;

	}

	/**
	 * Sauvegarde dans un fichier le meilleur neuralNetwork
	 */
	private void saveBest(){
		Espece espece = NaturalSelection.best;

		if(espece != null) {
			NetworkStructure.save("best_nn.dat", espece.neuralNetwork);
		}
	}
	
	public void start() {

		Timer uploadCheckerTimer = new Timer(false);
		uploadCheckerTimer.scheduleAtFixedRate(
		    new TimerTask() {
		      public void run() { repaint(); }
		    }, 0, 16);
		
	}
	
	/**
	 * Dessine le systeme de neurone
	 */
	public void paintComponent(Graphics gra) {
	    super.paintComponent(gra);
		Graphics2D g = (Graphics2D)gra;
		
		g.setColor(Color.white);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		
		//titre
		g.setColor(Color.black);
		Font f = new Font("Arial",20,20);
		g.setFont(f);
		String text = String.format("Bagage genetique de l'espece  %s", this.espece.toString(), this.espece.getFitness());
		g.drawString(text,this.getWidth()/2- getFontMetrics(f).stringWidth(text)/2, 20);


		g.setFont(new Font("Arial",12,12));
		g.setColor(Color.red);
		g.setStroke(new BasicStroke(2));
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		int netSize = this.network.getSize();
		int layerSize;
		for(int i = netSize-1; i >= 0; i--) {
			layerSize = this.network.getLayer(i).getSize();
			for(int j = 0; j < layerSize; j++) {
				
				//draw connection
				Point p1 = this.getNodePosition(i, j, netSize, layerSize);
				for(Connection c : this.network.getNeuron(i, j).getConnections()){
					int k = c.getNeuronInput().getLayer().getIndex();
					int l = c.getNeuronInput().getIndex();
					
					Point p2 = this.getNodePosition(k, l, netSize, c.getNeuronInput().getLayer().getSize());
					if(c.isEnable())
						this.drawConnectionAtPosition(g, p1, p2, c.getWeight(), this.network.getNeuron(i, j).getValue(), c.getNeuronInput().getValue());
					
				}
				
				//draw neuron
				
				Neuron neuron = this.network.getNeuron(i, j);
				drawNeuronAtPosition(g, neuron, p1);
				
				
			}
		}
	}
	
	private void drawNeuronAtPosition(Graphics2D g, Neuron neuron, Point pos) {
		g.setStroke(new BasicStroke(1.0f));
		g.setColor(Color.white);
		g.fillOval(pos.x - OVALSIZE/2, pos.y - OVALSIZE/2, OVALSIZE, OVALSIZE);
		
		
		g.setColor(getColorFromValue(neuron.getValue()));
		g.drawOval(pos.x - OVALSIZE/2, pos.y - OVALSIZE/2, OVALSIZE, OVALSIZE);
		
		g.setColor(g.getColor().darker().darker());
		g.drawString(Math.round(neuron.getValue()*100.0)/100.0+"", pos.x-OVALSIZE/2+7, pos.y+OVALSIZE/6);
	}
	
	private void drawConnectionAtPosition(Graphics2D g, Point p1, Point p2, double connectionstrength, double neuronValue1, double neuronValue2) {
		g.setStroke(new BasicStroke(1+(int)(Math.exp(connectionstrength)*4.0)));
		

		g.setPaint(new GradientPaint(p1.x, p1.y, getColorFromValue(neuronValue1), p2.x, p2.y, getColorFromValue(neuronValue2)));
		
		g.drawLine(p1.x, p1.y, p2.x, p2.y);
	}
	
	private Color getColorFromValue(double d) {
		return Color.getHSBColor((float) (d/3), 1, 1);
	}
	
	private Point getNodePosition(int layer, int row, int maxLayer, int maxRow) {
		int x = OFFSETX + (int) ((layer + .5)*(this.getWidth()/maxLayer-OFFSETX*2));
		int y = OFFSETY + (int) (((row+0.5)*(this.getHeight()-OFFSETY*2)/maxRow));
		
		return new Point(x, y);
	}
	
	public void setEspece(Espece e) {
		this.network = e.getNeuralNetwork();
		this.espece = e;
	}
		
}
