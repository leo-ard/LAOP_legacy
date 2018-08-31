package org.lrima.simulation;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;

import javafx.scene.input.KeyCode;
import org.lrima.core.UserPrefs;
import org.lrima.espece.Espece;
import org.lrima.espece.network.NetworkPanel;
import org.lrima.map.MapPanel;
import org.lrima.simulation.Interface.Actions.*;
import org.lrima.simulation.Interface.EspeceInfoPanel;
import org.lrima.simulation.Interface.GraphicPanel;
import org.lrima.simulation.Interface.SimulationPanel;

public class FrameManager extends JFrame implements MouseListener, KeyListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private NetworkPanel networkPanel;
	private MapPanel mapPanel;
	
	public Simulation simulation;
	public static FrameManager frame;
	public static GraphicPanel graphicPanel;
	public static SimulationPanel simulationPanel;
	public static EspeceInfoPanel especeInfoPanel;

    public static SimulationInfos simulationInfos;

	//Pour le menu
    private JCheckBoxMenuItem realtime;
    private JCheckBoxMenuItem randomMap;
    private JCheckBoxMenuItem graphique;
    private JCheckBoxMenuItem neuralNet;
    private JCheckBoxMenuItem followBest;
    private JCheckBoxMenuItem especeInfo;

    boolean haveToRestart = false;
	
	public FrameManager(String s, Simulation simulation) {
		//super(s);

		//Setup pour etre plein ecran
		setExtendedState(JFrame.MAXIMIZED_BOTH);

        this.simulation = simulation;
        simulationInfos = new SimulationInfos(simulation);

        //setup les paneaux
		mapPanel = new MapPanel(simulation.getMap(), getSize().width, getSize().height);
		networkPanel = new NetworkPanel(simulation.especesOpen.get(0), 1000, 200);
        graphicPanel = new GraphicPanel(simulationInfos);
		especeInfoPanel = new EspeceInfoPanel(simulation);

		this.add(mapPanel);
		//this.add(networkPanel, BorderLayout.SOUTH);
		simulationPanel = new SimulationPanel(simulationInfos);
		//this.add(simulationPanel, BorderLayout.WEST);

        this.add(graphicPanel, BorderLayout.SOUTH);

		this.addKeyListener(simulation);
		this.addMouseListener(this);
		this.addKeyListener(this);
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

        //randomMap = new JCheckBoxMenuItem(new RandomMapAction("Random Map"));
        //simulationMenu.add(randomMap);

        followBest = new JCheckBoxMenuItem(new FollowBestAction("Follow best", mapPanel));
        simulationMenu.add(followBest);

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

        //JMenuItem changeMap = new JMenuItem(new ChangeMapAction("Get Random Map", this));
        //map.add(changeMap);

        //Menu window
        JMenu window = new JMenu("Window");
        menuBar.add(window);

        graphique = new JCheckBoxMenuItem(new WindowAddPanel("Graphiques", this, simulationPanel, "West", UserPrefs.KEY_WINDOW_GRAPHIQUE));
        window.add(graphique);

        neuralNet = new JCheckBoxMenuItem(new WindowAddPanel("Neural Network", this, networkPanel, "South", UserPrefs.KEY_WINDOW_NEURAL_NET));
        window.add(neuralNet);

        especeInfo = new JCheckBoxMenuItem(new WindowAddPanel("Car info", this, especeInfoPanel, "East", UserPrefs.KEY_WINDOW_ESPECE_INFO));
        window.add(especeInfo);

        load_pref();
    }

    private void load_pref(){
        realtime.setState(UserPrefs.REAL_TIME);
        //randomMap.setState(UserPrefs.RANDOM_MAP);
        followBest.setState(UserPrefs.FOLLOW_BEST);

	    graphique.setState(UserPrefs.SHOW_WINDOW_GRAPHIQUE);
	    neuralNet.setState(UserPrefs.SHOW_WINDOW_NEURAL_NETWORK);
	    especeInfo.setState(UserPrefs.SHOW_WINDOW_ESPECE_INFO);

	    if(graphique.getState()){
            add(simulationPanel, "West");
        }
        if(neuralNet.getState()){
	        add(networkPanel, "South");
        }
        if(especeInfo.getState()){
	        add(especeInfoPanel, "East");
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
		//simulationInfos.addGeneration(especes);
		//simulationPanel.update();
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

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_W){
            simulation.shouldGoToNextGeneration = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
