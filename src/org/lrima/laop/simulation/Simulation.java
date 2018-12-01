package org.lrima.laop.simulation;

class Simulation{
    private SimulationManager simulationManager;
    private SimulationModel simulationmodel;

    //For test purposes
    private int i = 0;

    Simulation(SimulationManager simulationManager, SimulationModel simulationModel){
        this.simulationManager = simulationManager;
        this.simulationmodel = simulationModel;
    }

    /**
     * Update at each loop count
     */
    void update(){

        //TODO :
        // Update the cars
        // Verify the collision, change the state of the ones that collide with walls. Gets the map with simulationManager.getMap(). Gets the arrayList of cars with simulationManager.getCars().

        System.out.println(this.simulationmodel.name + ", Simulation Count : "+ this.simulationManager.getSimulationCount() + " frame : "+ i);

        // For test purposes
        i++;
        if(i >= 20){
            simulationManager.nextSimulation();
        }
    }

}
