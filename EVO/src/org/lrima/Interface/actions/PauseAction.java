package org.lrima.Interface.actions;

import org.lrima.simulation.Simulation;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Toggles the simulation's pause state
 */
public class PauseAction extends AbstractAction {

    private Simulation simulation;

    public PauseAction(String name, Simulation simulation){
        super(name);
        this.simulation = simulation;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //toggle pause
        simulation.setPausing(!simulation.isPausing());

        //Change the string of the button int the menu
        if(simulation.isPausing()){
            this.putValue(Action.NAME, "Play");
        }
        else{
            this.putValue(Action.NAME, "Pause");
        }
    }
}
