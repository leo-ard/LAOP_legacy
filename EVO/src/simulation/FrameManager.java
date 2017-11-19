package simulation;

import javax.swing.JFrame;

import map.MapPanel;

public class FrameManager extends JFrame{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
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
