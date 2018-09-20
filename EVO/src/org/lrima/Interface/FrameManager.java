package org.lrima.Interface;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import org.lrima.core.UserPrefs;
import org.lrima.espece.Espece;
import org.lrima.espece.network.NetworkPanel;
import org.lrima.espece.network.interfaces.NeuralNetwork;
import org.lrima.map.Map;
import org.lrima.map.MapPanel;
import org.lrima.Interface.actions.*;
import org.lrima.Interface.EspeceInfoPanel;
import org.lrima.Interface.GraphicPanel;
import org.lrima.simulation.Simulation;
import org.lrima.simulation.SimulationListener;

public class FrameManager extends JFrame implements SimulationListener {

    //All the panels
	//private NetworkPanel networkPanel;
	private MapPanel mapPanel;
    private GraphicPanel graphicPanel;
    private EspeceInfoPanel especeInfoPanel;

    //the simulation and its information
	private Simulation simulation;

	//Pour le menu
    private JCheckBoxMenuItem checkBoxRealtime;
    private JCheckBoxMenuItem checkBoxGraphique;
    private JCheckBoxMenuItem checkBoxFollowBest;
    private JCheckBoxMenuItem checkBoxEspeceInfo;
	
	public FrameManager() {
	    this.setupWindow();

        AccueilDialog accueil = new AccueilDialog(this);
        accueil.setVisible(true);

        this.especeInfoPanel = new EspeceInfoPanel();
	}

    /**
     * Sets the algorithm to use. It creates a new simulation and resets all the panels using the simulation
     * @param algorithm the algorithm to use
     */
    public void setAlgorithmToUse(Class<?extends NeuralNetwork> algorithm){
	    this.simulation = new Simulation(algorithm);

	    this.simulation.addSimulationListener(this);


	    this.mapPanel = new MapPanel(simulation);
	    //this.networkPanel = new NetworkPanel(simulation);
	    this.graphicPanel = new GraphicPanel(simulation);

        createMenu();

	    this.add(mapPanel, BorderLayout.CENTER);

        displaySavedPanel();

	    this.start();
	    revalidate();
    }

    /**
     * Setup the size of the window, the listeners and basic configuration
     */
	private void setupWindow(){
        //Get the size of the screen
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        //The size if the user disable full screen
        setSize(screenSize.width / 2, screenSize.height / 2);
        setResizable(true);

        //Make it full screen
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        //Key listener
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                //Press Q to go to the next generation
                if(e.getKeyCode() == KeyEvent.VK_Q){
                    simulation.goToNextGeneration();
                }
                if(e.getKeyCode() == KeyEvent.VK_W){
                    simulation.getBest().setSelected(true);
                }
            }
        });

        //Mouse listener
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //requestFocus();
            }
        });

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setAutoRequestFocus(true);
        this.setVisible(true);
    }

    /**
     * Create the menu buttons
     */
	private void createMenu(){
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        setupFileMenu(menuBar);
        setupSimulationMenu(menuBar);
        setupMapMenu(menuBar);
        setupWindowMenu(menuBar);

        setMenuButtonStates();
    }

    /**
     * Add the File menu buttons to the menu bar
     * @param menuBar the menu bar to add the file menu
     */
    private void setupFileMenu(JMenuBar menuBar){
        JMenu file = new JMenu("File");
        menuBar.add(file);

    }

    /**
     * Add the simulation buttons on the menu bar
     * @param menuBar the menu bar to add the simulation menu onto
     */
    private void setupSimulationMenu(JMenuBar menuBar){
        JMenu simulationMenu = new JMenu("Simulation");
        menuBar.add(simulationMenu);

        //Real time checkbox
        checkBoxRealtime = new JCheckBoxMenuItem(new RealTimeAction("Real time"));
        simulationMenu.add(checkBoxRealtime);

        //Follow best checkbox
        checkBoxFollowBest = new JCheckBoxMenuItem(new FollowBestAction("Follow best"));
        simulationMenu.add(checkBoxFollowBest);

        //Pause button
        JMenuItem pause = new JMenuItem(new PauseAction("Pause", simulation));
        pause.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, InputEvent.CTRL_MASK));
        simulationMenu.add(pause);

        //Options button
        JMenuItem options = new JMenuItem(new MoreOptionsAction("Options", this));
        simulationMenu.add(options);
    }

    /**
     * Add the map menu buttons to the menu bar
     * @param menuBar the menu bar to add the map menu onto
     */
    private void setupMapMenu(JMenuBar menuBar){
        JMenu map = new JMenu("Map");
        menuBar.add(map);

        //Map editor button
        JMenuItem mapEditor = new JMenuItem(new OpenStudioAction("Open Studio", simulation));
        map.add(mapEditor);

        JMenuItem loadMapButton = new JMenuItem(new LoadMapAction("Load Map", this.mapPanel, simulation));
        map.add(loadMapButton);
    }

    /**
     * Add the window menu buttons to the menu bar
     * @param menuBar the menu bar to add the window menu onto
     */
    private void setupWindowMenu(JMenuBar menuBar){
        JMenu window = new JMenu("Window");
        menuBar.add(window);

        //Show graphic panel button
        checkBoxGraphique = new JCheckBoxMenuItem(new WindowAddPanelAction("Graphiques", this, graphicPanel, "South", UserPrefs.KEY_WINDOW_GRAPHIQUE));
        window.add(checkBoxGraphique);

        //Show car information panel
        checkBoxEspeceInfo = new JCheckBoxMenuItem(new WindowAddPanelAction("Car info", this, especeInfoPanel, "East", UserPrefs.KEY_WINDOW_ESPECE_INFO));
        window.add(checkBoxEspeceInfo);
    }

    /**
     * Set the state of the check boxes in the menu to the state that is saved
     * in the user preferences
     */
    private void setMenuButtonStates(){
        checkBoxRealtime.setState(UserPrefs.REAL_TIME);
        checkBoxFollowBest.setState(UserPrefs.FOLLOW_BEST);
        checkBoxGraphique.setState(UserPrefs.SHOW_WINDOW_GRAPHIQUE);
        checkBoxEspeceInfo.setState(UserPrefs.SHOW_WINDOW_ESPECE_INFO);
    }

    /**
     * Restore the panels that was open in the last session
     */
    private void displaySavedPanel(){
	    if(checkBoxGraphique.getState()){
	        add(graphicPanel, "South");
        }
        if(checkBoxEspeceInfo.getState()){
	        add(especeInfoPanel, "East");
        }
    }

    /**
     * Make the mapPanel and networkPanel redraw itself regularly
     */
    public void start() {
		mapPanel.start();
        simulation.start();
	}

	//TODO: On a tu vraiment besoin de ça?
	public void pack() {
		super.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

    /**
     * Change the car that the networkPanel uses
     * @param e the car
     */
    public void changeCarFocus(Espece e) {
		especeInfoPanel.setEspece(e);
	}

    @Override
    public void onNextGeneration() {
        this.graphicPanel.updateChart(this.simulation);
        this.getContentPane().repaint();
    }

    @Override
    public void simulationRestarted() {
        Map map = this.mapPanel.getMap();
        this.simulation.setMap(map);
    }
}
