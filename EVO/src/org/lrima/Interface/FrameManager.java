package org.lrima.Interface;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
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
import org.lrima.simulation.BatchListener;
import org.lrima.simulation.Simulation;
import org.lrima.simulation.SimulationBatch;
import org.lrima.simulation.SimulationListener;

public class FrameManager extends JFrame implements SimulationListener, BatchListener {

    //All the panels
	//private NetworkPanel networkPanel;
	private MapPanel mapPanel;
    private GraphicPanel graphicPanel;
    private EspeceInfoPanel especeInfoPanel;

    //the simulation and its information
	private ArrayList<SimulationBatch> simulationBatches = new ArrayList<>();
	private int currentBatch = 0;

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
    public void addBatch(Class<?extends NeuralNetwork> algorithm, int numberInBatch){
        SimulationBatch simulationBatch = new SimulationBatch(algorithm, numberInBatch);
        this.simulationBatches.add(simulationBatch);
        simulationBatch.addBatchListener(this);

        for(Simulation simulation : simulationBatch.getSimulations()){
            simulation.addSimulationListener(this);
        }

    }

    public void startBatches(){
        simulationBatches.get(currentBatch).startBatch();
        this.reloadPanels();
    }

    private void reloadPanels(){

        if(this.mapPanel != null) {
            mapPanel.stop();
            this.remove(mapPanel);

        }
        if(graphicPanel != null){
            this.remove(graphicPanel);
        }

        this.mapPanel = new MapPanel(simulationBatches.get(currentBatch).getCurrentSimulation());
        this.graphicPanel = new GraphicPanel(simulationBatches.get(currentBatch).getCurrentSimulation());

        mapPanel.setCurrentSimulationBatch(simulationBatches.get(currentBatch));
        mapPanel.setCurrentBatchNumber(currentBatch + 1);
        mapPanel.setMaxBatch(simulationBatches.size());


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
                    simulationBatches.get(currentBatch).getCurrentSimulation().goToNextGeneration();
                }
                if(e.getKeyCode() == KeyEvent.VK_W){
                    simulationBatches.get(currentBatch).getCurrentSimulation().getBest().setSelected(true);
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
        JMenuItem pause = new JMenuItem(new PauseAction("Pause", simulationBatches.get(currentBatch).getCurrentSimulation()));
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
        JMenuItem mapEditor = new JMenuItem(new OpenStudioAction("Open Studio", simulationBatches.get(currentBatch).getCurrentSimulation()));
        map.add(mapEditor);

        JMenuItem loadMapButton = new JMenuItem(new LoadMapAction("Load Map", this.mapPanel, simulationBatches.get(currentBatch).getCurrentSimulation()));
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
	}

	public void stop(){
        mapPanel.stop();
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
        this.graphicPanel.updateChart(this.simulationBatches.get(currentBatch).getCurrentSimulation());
        this.getContentPane().repaint();
    }

    @Override
    public void simulationRestarted() {
        Map map = this.mapPanel.getMap();
        this.simulationBatches.get(currentBatch).getCurrentSimulation().setMap(map);
    }

    @Override
    public void simulationEnded() {
        reloadPanels();
    }

    @Override
    public void batchFinished() {
        if(currentBatch + 1 < this.simulationBatches.size()) {
            this.currentBatch++;
            //Next batch
            this.startBatches();
        }
        else{
            this.allBatchFinished();
        }
    }

    private void allBatchFinished(){
        System.out.println("All batches finished");
    }
}
