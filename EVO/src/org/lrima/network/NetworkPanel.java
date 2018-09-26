package org.lrima.espece.network;

import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.*;

import org.lrima.espece.Espece;

public class NetworkPanel extends JPanel{

	private Espece espece;
	
	public NetworkPanel(Espece espece) {
		super();
		this.setPreferredSize(new Dimension(500, 500));
		this.espece = espece;
		this.setBackground(Color.PINK);
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
		Graphics2D g = (Graphics2D) gra;


		g.setFont(new Font("Arial", 12, 12));
		g.setColor(Color.red);
		g.setStroke(new BasicStroke(2));
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		//Draw the neural network
		if (this.espece != null) {
			this.espece.getNeuralNetwork().draw(g);
		}
	}
	
	public void setEspece(Espece e) {
		this.espece = e;

		this.repaint();
	}
		
}
