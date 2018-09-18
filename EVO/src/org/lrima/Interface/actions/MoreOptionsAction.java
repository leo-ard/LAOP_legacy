package org.lrima.Interface.actions;

import org.lrima.Interface.options.OptionsDialog;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Opens the more option dialog
 */
public class MoreOptionsAction extends AbstractAction {

    private OptionsDialog dialog;

    public MoreOptionsAction(String name, JFrame parent){
        super(name);
        dialog = new OptionsDialog();
    }

    //Open the more option dialog
    @Override
    public void actionPerformed(ActionEvent e) {
        dialog.setVisible(true);
    }
}
