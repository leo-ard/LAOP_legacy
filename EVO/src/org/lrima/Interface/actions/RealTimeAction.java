package org.lrima.Interface.actions;

import org.lrima.core.UserPrefs;
import org.lrima.Interface.options.types.OptionBoolean;

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
        UserPrefs.set(UserPrefs.KEY_REAL_TIME, new OptionBoolean(((JCheckBoxMenuItem) e.getSource()).getState()));
    }
}
