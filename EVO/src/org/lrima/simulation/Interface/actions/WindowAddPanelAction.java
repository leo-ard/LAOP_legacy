package org.lrima.simulation.Interface.actions;

import org.lrima.core.UserPrefs;
import org.lrima.simulation.FrameManager;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Toggles the display of a panel at a certain side of the screen
 */
public class WindowAddPanelAction extends AbstractAction {

    private FrameManager frameManager;
    private JPanel panel;
    private String corner, prefKey;

    public WindowAddPanelAction(String name, FrameManager frameManager, JPanel panelToAdd, String corner, String prefKey){
        super(name);

        this.frameManager = frameManager;
        this.panel = panelToAdd;
        this.corner = corner;
        this.prefKey = prefKey;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        boolean state = ((JCheckBoxMenuItem) e.getSource()).getState();

        if(state) {
            frameManager.add(panel, corner);
        }
        else{
            frameManager.remove(panel);
        }

        //Save the state into the preferences
        UserPrefs.preferences.putBoolean(prefKey, state);
        frameManager.revalidate();
    }
}
