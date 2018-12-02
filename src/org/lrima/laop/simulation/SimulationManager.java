package org.lrima.laop.simulation;

import org.lrima.laop.simulation.exeptions.SimulationStillRunningExeption;
import org.lrima.laop.simulation.map.Map;

import java.util.ArrayList;

public class SimulationManager implements Runnable{
    //keep track of the simulation and the batch number
    private int simulationCount, batchCount;

    private SimulationManagerModel simulationManagerModel;
    private Map map;
    private Thread simulationThread;

    //For the loop
    private boolean currentSimulationRunning = true;
    private boolean pausing = false;
    //TODO : put in settings
    private double msBetweenUpdates = 300;

    private ArrayList<SimulationListener> simulationListeners;
    private Simulation currentSimulation;


    public SimulationManager(SimulationManagerModel simulationManagerModel){
        this.simulationManagerModel = simulationManagerModel;

        this.simulationListeners = new ArrayList<>();
    }

    /**
     * Start the first simulation
     */
    public void start() {
        if(simulationThread != null && simulationThread.isAlive())
            throw new SimulationStillRunningExeption("Cannot start a SimulationManager that is still running");

        //Default settings
        simulationCount = -1;
        batchCount = 0;
        currentSimulationRunning = true;
        currentSimulation = new Simulation(this, simulationManagerModel.getSimulationModel(0));

        //Start
        simulationThread = new Thread(this);
        simulationThread.start();
    }

    public void addSimulationListener(SimulationListener simulationListener){
        this.simulationListeners.add(simulationListener);
    }

    /**
     * Main loop of the program
     *
     */
    @Override
    public void run() {
        while(iterateSimulation()){
            long currentTime;
            long timePassed;
            while(currentSimulationRunning) {
                currentTime = System.currentTimeMillis();
                if(!pausing){
                    currentSimulation.update();
                }

                //pause the Thread depending on realtime or not
                //TODO : get real time from user pref
                if(true){
                    try{
                        timePassed = System.currentTimeMillis() - currentTime;
                        System.out.println("pass time : " + ((msBetweenUpdates - timePassed) > 0 ? msBetweenUpdates - timePassed: 0));
                        Thread.sleep((long) ((msBetweenUpdates - timePassed) > 0 ? msBetweenUpdates - timePassed: 0));
                    } catch(InterruptedException e){
                        e.printStackTrace();
                    }
                }
                else{
                    try{
                        Thread.sleep(0);
                    }catch(InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * Handle the simulation and batch count.
     * Handle the call of the listeners
     * Called at each time a simulation finish
     *
     * When the simulationCount reach the max, resets it. Same thing for the batchCount.
     * Then, affect the currentSimulation in accordance of the simulationCount and the batchCount.
     *
     * @return false if the simulationCount and the batchCount reach the max (ALL the simulations are finished), true otherwise
     */
    //TODO : get the data from the current simulation
    private boolean iterateSimulation() {
        //TODO : maybe not the best to detect that it's the first run
        if(simulationCount != -1)
            simulationListeners.forEach(SimulationListener::simulationFinished);

        int maxSimulation = simulationManagerModel.getMaxSimulation();
        int maxBatch = simulationManagerModel.getMaxBatch();
        simulationCount ++;

        //true if the batch finished
        if(simulationCount >= maxSimulation){
            simulationListeners.forEach(SimulationListener::batchFinished);

            simulationCount = 0;
            batchCount ++;
            if(batchCount >= maxBatch){
                simulationListeners.forEach(SimulationListener::simulationManagerFinished);
                return false;
            }
        }
        currentSimulation = new Simulation(this, simulationManagerModel.getSimulationModel(batchCount));
        currentSimulationRunning = true;

        return true;
    }

    void nextSimulation(){
        currentSimulationRunning = false;
    }

    public Map getMap(){
        return map;
    }

    int getSimulationCount() {
        return simulationCount;
    }
}
