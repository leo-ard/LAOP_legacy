package org.lrima.simulation.Interface.Actions;

import org.lrima.core.UserPrefs;
import org.lrima.simulation.Simulation;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class RandomMapAction extends AbstractAction {

    public RandomMapAction(String name){
        super(name);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        UserPrefs.preferences.putBoolean(UserPrefs.KEY_RANDOM_MAP, ((JCheckBoxMenuItem) e.getSource()).getState());
        UserPrefs.load();
    }
}
