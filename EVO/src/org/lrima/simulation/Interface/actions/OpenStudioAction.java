package org.lrima.simulation.Interface.actions;

import org.lrima.map.Studio.Studio;
import org.lrima.simulation.Simulation;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Opens the map studio
 */
public class OpenStudioAction extends AbstractAction {

    private Simulation simulation;

    public OpenStudioAction(String name, Simulation simulation){
        super(name);
        this.simulation = simulation;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //Set the simulation to pause before displaying the map studio
        simulation.setPausing(true);

        Studio studio = new Studio(simulation);
        studio.setVisible(true);
    }
}
