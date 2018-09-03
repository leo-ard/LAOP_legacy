package org.lrima.simulation.Interface.actions;

import org.lrima.core.UserPrefs;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Toggles the real time preference
 */
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
