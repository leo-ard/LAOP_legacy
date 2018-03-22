package simulation;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;

import core.EVO;
import espece.Espece;
import espece.network.NetworkPanel;
import map.MapPanel;
import simulation.selection.SimulationPanel;

public class FrameManager extends JFrame{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private NetworkPanel networkPanel;
	private MapPanel mapPanel;
	
	public static Simulation simulation;
	public static FrameManager frame;
	
	public FrameManager(String s, Simulation simulation) {
		super(s);
		mapPanel = new MapPanel(simulation.map, 1000, 500);
		networkPanel = new NetworkPanel(simulation.especesOpen.get(0), 1000, 200);
		this.add(mapPanel);
		this.add(networkPanel, BorderLayout.SOUTH);
		//this.add(new SimulationPanel(simulation), BorderLayout.WEST);
		this.addKeyListener(simulation);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
	}
	
	public void start() {
		mapPanel.start();
		networkPanel.start();
		
	}
	
	public void pack() {
		super.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

	public void changeNetworkFocus(Espece e) {
		networkPanel.setEspece(e);
		
	}

}
