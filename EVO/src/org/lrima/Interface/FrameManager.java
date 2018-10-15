package org.lrima.Interface;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;

import org.lrima.Interface.conclusion.ConclusionFrame;
import org.lrima.Interface.options.OptionsDialog;
import org.lrima.core.UserPrefs;
import org.lrima.espece.Espece;
import org.lrima.network.interfaces.NeuralNetworkModel;
import org.lrima.map.Map;
import org.lrima.map.MapPanel;
import org.lrima.Interface.actions.*;
import org.lrima.simulation.*;

public class FrameManager extends JFrame implements SimulationListener, BatchListener {
    //All the panels
	private MapPanel mapPanel;
    private GraphicPanel graphicPanel;
    private EspeceInfoPanel especeInfoPanel;

	//Pour le menu
    private JCheckBoxMenuItem checkBoxRealtime;
    private JCheckBoxMenuItem checkBoxGraphique;
    private JCheckBoxMenuItem checkBoxFollowBest;
    private JCheckBoxMenuItem checkBoxEspeceInfo;

    //Simulations
    SimulationManager simulationManager;

	public FrameManager(SimulationManager simulationManager) {
	    this.setTitle("The simulations are running...");

        this.simulationManager = simulationManager;
        this.simulationManager.setFrameManager(this);
        this.simulationManager.addSimulationListener(this);
        this.simulationManager.addBatchListener(this);

        this.mapPanel = new MapPanel(this);
        this.graphicPanel = new GraphicPanel(this);

        this.setupWindow();

        this.especeInfoPanel = new EspeceInfoPanel();

        createMenu();

        this.add(mapPanel, BorderLayout.CENTER);
        displaySavedPanel();

        this.mapPanel.start();
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
                    simulationManager.getCurrentSimulation().goToNextGeneration();
                }
                if(e.getKeyCode() == KeyEvent.VK_X){
                    int oldSelectedIndex = simulationManager.getCurrentSimulation().getAllEspeces().indexOf(simulationManager.getCurrentSimulation().getSelectedEspece());
                    int newIndex = (oldSelectedIndex + 1) % simulationManager.getCurrentSimulation().getAllEspeces().size();
                    changeCarFocus(simulationManager.getCurrentSimulation().getAllEspeces().get(newIndex));
                }
                if(e.getKeyCode() == KeyEvent.VK_Z){
                    int oldSelectedIndex = simulationManager.getCurrentSimulation().getAllEspeces().indexOf(simulationManager.getCurrentSimulation().getSelectedEspece());
                    int newIndex = (oldSelectedIndex - 1) % simulationManager.getCurrentSimulation().getAllEspeces().size();
                    changeCarFocus(simulationManager.getCurrentSimulation().getAllEspeces().get(newIndex));
                }
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

        //setupFileMenu(menuBar);
        setupSimulationMenu(menuBar);
        //setupMapMenu(menuBar);
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
        //checkBoxFollowBest = new JCheckBoxMenuItem(new FollowBestAction("Follow best"));
        //simulationMenu.add(checkBoxFollowBest);

        //Pause button
        JMenuItem pause = new JMenuItem(new PauseAction("Pause", simulationManager.getCurrentSimulation()));
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
        JMenuItem mapEditor = new JMenuItem(new OpenStudioAction("Open Studio", simulationManager.getCurrentSimulation()));
        map.add(mapEditor);

        JMenuItem loadMapButton = new JMenuItem(new LoadMapAction("Load Map", this.mapPanel, simulationManager.getCurrentSimulation()));
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
        //TODO : CAN BE OPTIMISED WITH GETTING OPTION TYPE DIRRECTLY, i think
        checkBoxRealtime.setState(UserPrefs.getBoolean(UserPrefs.KEY_REAL_TIME));
//        checkBoxFollowBest.setState(UserPrefs.getBoolean(UserPrefs.KEY_FOLLOW_BEST));
        checkBoxGraphique.setState(UserPrefs.getBoolean(UserPrefs.KEY_WINDOW_GRAPHIQUE));
        checkBoxEspeceInfo.setState(UserPrefs.getBoolean(UserPrefs.KEY_WINDOW_ESPECE_INFO));
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
     * Change the car that the networkPanel uses
     * @param e the car
     */
    public void changeCarFocus(Espece e) {
        simulationManager.getCurrentSimulation().setSelected(e);
		especeInfoPanel.setEspece(e);
	}

    @Override
    public void onNextGeneration() {
        this.graphicPanel.updateChart();
        this.getContentPane().repaint();
    }

    @Override
    public void simulationRestarted() {

    }

    @Override
    public void simulationEnded() {

    }

    @Override
    public void dispose(){
        mapPanel.stop();
        super.dispose();
    }


    public SimulationManager getSimulationManager() {
        return simulationManager;
    }

    public void restart() {
        simulationManager.restart();
    }

    @Override
    public void batchFinished() {

    }

    @Override
    public void nextSimulationInBatch() {
        //this.graphicPanel.nextSimulation(this.simulationManager.getCurrentSimulation());
        //this.remove(graphicPanel);
        //this.graphicPanel = new GraphicPanel(this);
        //this.add(graphicPanel, "South");
        //this.add(graphicPanel, "South");
        //this.repaint();
    }
}
