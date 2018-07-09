package org.lrima.simulation.Interface.Actions;

import org.lrima.core.UserPrefs;
import org.lrima.simulation.Simulation;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class RealTimeAction extends AbstractAction {

    public RealTimeAction(String nom){
        super(nom);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        UserPrefs.preferences.putBoolean(UserPrefs.KEY_REAL_TIME, ((JCheckBoxMenuItem) e.getSource()).getState());
        UserPrefs.load();
    }
}
