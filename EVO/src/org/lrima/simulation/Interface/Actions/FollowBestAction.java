package org.lrima.simulation.Interface.Actions;

import org.lrima.core.UserPrefs;
import org.lrima.map.MapPanel;
import org.lrima.simulation.Simulation;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class FollowBestAction extends AbstractAction {

    MapPanel panel;

    public FollowBestAction(String name, MapPanel panel){
        super(name);
        this.panel = panel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        boolean state = ((JCheckBoxMenuItem)e.getSource()).getState();

        UserPrefs.preferences.putBoolean(UserPrefs.KEY_FOLLOW_BEST, state);

        panel.followBest = state;
    }
}
