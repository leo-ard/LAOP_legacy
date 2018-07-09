package org.lrima.simulation.Interface.Actions;

import org.lrima.map.Map;
import org.lrima.simulation.FrameManager;
import org.lrima.simulation.Simulation;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class ChangeMapAction extends AbstractAction {
    FrameManager manager;

    public ChangeMapAction(String name, FrameManager manager){
        super(name);
        this.manager = manager;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        Simulation simulation = manager.getSimulation();
        simulation.shouldGetNewMap = true;
    }
}
