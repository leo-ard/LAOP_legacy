package org.lrima.Interface.actions;

import org.lrima.core.UserPrefs;
import org.lrima.Interface.options.types.OptionBoolean;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Toggles the follow best car preference
 */
public class FollowBestAction extends AbstractAction {

    public FollowBestAction(String name){
        super(name);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //Sets the state of the checkbox into the preferences of the user
        boolean state = ((JCheckBoxMenuItem)e.getSource()).getState();
        UserPrefs.set(UserPrefs.KEY_FOLLOW_BEST, new OptionBoolean(state));
    }
}
