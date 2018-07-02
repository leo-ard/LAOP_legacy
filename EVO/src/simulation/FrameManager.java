package simulation;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JFrame;

import espece.Espece;
import espece.network.NetworkPanel;
import map.MapPanel;
import simulation.Interface.SimulationPanel;

public class FrameManager extends JFrame implements MouseListener{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private NetworkPanel networkPanel;
	private MapPanel mapPanel;
	
	public static Simulation simulation;
	public static FrameManager frame;
	public static SimulationInfos simulationInfos;
	public static SimulationPanel simulationPanel;
	
	public FrameManager(String s, Simulation simulation) {
		super(s);
		mapPanel = new MapPanel(simulation.map, 1000, 500);
		networkPanel = new NetworkPanel(simulation.especesOpen.get(0), 1000, 200);
		simulationInfos=new SimulationInfos(simulation);
		this.add(mapPanel);
		this.add(networkPanel, BorderLayout.SOUTH);
		simulationPanel = new SimulationPanel(simulationInfos);
		this.add(simulationPanel, BorderLayout.WEST);
		this.addKeyListener(simulation);
		this.addMouseListener(this);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
		//this.setAutoRequestFocus(true);

		//this.requestFocus();
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

	public static void addGeneration(ArrayList<Espece> especes) {
		simulationInfos.addGeneration(especes);
		simulationPanel.update();
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		this.requestFocus();
	}

	@Override
	public void mousePressed(MouseEvent e) {

	}

	@Override
	public void mouseReleased(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}
}
