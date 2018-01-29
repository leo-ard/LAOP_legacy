package core;

import javax.swing.JFrame;

import map.Map;
import map.MapPanel;
import simulation.Simulation;

public class EVO {
	
	public static Simulation simulation;

	public static void main(String[] args) {
		/*NeuralNetwork n = new NeuralNetwork(3, 2);
		n.update(1,0.5,0.75);
		System.out.println(n.getNodeOutput());
		
		JFrame f = new JFrame("Hey");
		f.add(new NetworkPanel(n, 1000, 500));
		
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.pack();
		f.setLocationRelativeTo(null);
		f.setVisible(true);
		
		*/
		simulation =  new Simulation();
		simulation.start();
	}

}
