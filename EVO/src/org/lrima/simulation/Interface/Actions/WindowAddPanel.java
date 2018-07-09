package org.lrima.simulation.Interface.Actions;

import org.lrima.core.UserPrefs;
import org.lrima.simulation.FrameManager;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class WindowAddPanel extends AbstractAction {

    FrameManager frameManager;
    JPanel panel;
    String corner, prefKey;

    public WindowAddPanel(String name, FrameManager frameManager, JPanel panelToAdd, String corner, String prefKey){
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

        UserPrefs.preferences.putBoolean(prefKey, state);
        frameManager.revalidate();
    }
}
