package org.lrima.simulation.Interface.Actions;

import org.lrima.simulation.Simulation;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class PauseAction extends AbstractAction {

    Simulation simulation;

    public PauseAction(String name, Simulation simulation){
        super(name);
        this.simulation = simulation;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        simulation.pausing = !simulation.pausing;

        if(simulation.pausing){
            this.putValue(Action.NAME, "Play");
        }
        else{
            this.putValue(Action.NAME, "Pause");
        }
    }
}
