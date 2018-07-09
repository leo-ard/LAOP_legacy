package org.lrima.simulation;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;

import org.lrima.core.UserPrefs;
import org.lrima.espece.Espece;
import org.lrima.espece.network.NetworkPanel;
import org.lrima.map.MapPanel;
import org.lrima.simulation.Interface.Actions.*;
import org.lrima.simulation.Interface.SimulationPanel;

public class FrameManager extends JFrame implements MouseListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private NetworkPanel networkPanel;
	private MapPanel mapPanel;
	
	public Simulation simulation;
	public static FrameManager frame;
	public static SimulationInfos simulationInfos;
	public static SimulationPanel simulationPanel;

	//Pour le menu
    private JCheckBoxMenuItem realtime;
    private JCheckBoxMenuItem randomMap;
    private JCheckBoxMenuItem graphique;
    private JCheckBoxMenuItem neuralNet;

    boolean haveToRestart = false;
	
	public FrameManager(String s, Simulation simulation) {
		//super(s);

		//Setup pour etre plein ecran
		setExtendedState(JFrame.MAXIMIZED_BOTH);

		mapPanel = new MapPanel(simulation.getMap(), getSize().width, getSize().height);
		networkPanel = new NetworkPanel(simulation.especesOpen.get(0), 1000, 200);
		simulationInfos=new SimulationInfos(simulation);
		this.simulation = simulation;
		this.add(mapPanel);
		//this.add(networkPanel, BorderLayout.SOUTH);
		simulationPanel = new SimulationPanel(simulationInfos);
		//this.add(simulationPanel, BorderLayout.WEST);
		this.addKeyListener(simulation);
		this.addMouseListener(this);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//this.pack();
		this.setAutoRequestFocus(true);
        this.setVisible(true);
        //Create menu
        createMenu();

		//this.requestFocus();
	}

    /**
     * Crée un menu
     */
	private void createMenu(){
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);



        //Menu file
        JMenu file = new JMenu("File");
        menuBar.add(file);

        JMenuItem save = new JMenuItem(new SaveFileAction("Save", this, simulation));
        save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
        file.add(save);

        JMenuItem load = new JMenuItem(new LoadFileAction("Load", this, simulation));
        load.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
        file.add(load);

        //Menu simulation
        JMenu simulationMenu = new JMenu("Simulation");
        menuBar.add(simulationMenu);

        realtime = new JCheckBoxMenuItem(new RealTimeAction("Real time"));
        simulationMenu.add(realtime);

        randomMap = new JCheckBoxMenuItem(new RandomMapAction("Random Map"));
        simulationMenu.add(randomMap);

        JMenuItem pause = new JMenuItem(new PauseAction("Pause", simulation));
        pause.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, InputEvent.CTRL_MASK));
        simulationMenu.add(pause);

        JMenuItem options = new JMenuItem(new MoreOptionsAction("Options", this));
        simulationMenu.add(options);

        //Menu map
        JMenu map = new JMenu("Map");
        menuBar.add(map);

        JMenuItem mapEditor = new JMenuItem(new OpenStudioAction("Open Studio", simulation));
        map.add(mapEditor);

        JMenuItem changeMap = new JMenuItem(new ChangeMapAction("Get Random Map", this));
        map.add(changeMap);

        //Menu window
        JMenu window = new JMenu("Window");
        menuBar.add(window);

        graphique = new JCheckBoxMenuItem(new WindowAddPanel("Graphiques", this, simulationPanel, "West", UserPrefs.KEY_WINDOW_GRAPHIQUE));
        window.add(graphique);

        neuralNet = new JCheckBoxMenuItem(new WindowAddPanel("Neural Network", this, networkPanel, "South", UserPrefs.KEY_WINDOW_NEURAL_NET));
        window.add(neuralNet);

        load_pref();
    }

    private void load_pref(){
        realtime.setState(UserPrefs.REAL_TIME);
        randomMap.setState(UserPrefs.RANDOM_MAP);

	    graphique.setState(UserPrefs.SHOW_WINDOW_GRAPHIQUE);
	    neuralNet.setState(UserPrefs.SHOW_WINDOW_NEURAL_NETWORK);

	    if(graphique.getState()){
            add(simulationPanel, "West");
        }
        if(neuralNet.getState()){
	        add(networkPanel, "South");
        }
    }

    public MapPanel getMapPanel() {
        return mapPanel;
    }

    public Simulation getSimulation() {
        return simulation;
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

    public NetworkPanel getNetworkPanel() {
        return networkPanel;
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
