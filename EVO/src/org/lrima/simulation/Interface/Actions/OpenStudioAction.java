package org.lrima.simulation.Interface.Actions;

import org.lrima.map.Studio.Studio;
import org.lrima.simulation.Simulation;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class OpenStudioAction extends AbstractAction {

    private Simulation simulation;

    public OpenStudioAction(String name, Simulation simulation){
        super(name);
        this.simulation = simulation;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        simulation.setPausing(true);

        Studio studio = new Studio(simulation);
        studio.setVisible(true);
    }
}
