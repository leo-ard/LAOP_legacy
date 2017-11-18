package simulation;

import javax.swing.JFrame;
import javax.swing.JLabel;

import map.MapPanel;

public class FrameManager extends JFrame{
	
	MapPanel mapPanel;
	Simulation simulation;
	
	public FrameManager(String s, Simulation sim, MapPanel p) {
		super(s);
		this.simulation = sim;
		mapPanel = p;
		this.add(mapPanel);
		this.pack();
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}

}
