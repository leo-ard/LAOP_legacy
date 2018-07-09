package org.lrima.simulation.Interface.Actions;

import org.lrima.simulation.Interface.options.OptionsDialog;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class MoreOptionsAction extends AbstractAction {

    OptionsDialog dialog;

    public MoreOptionsAction(String name, JFrame parent){
        super(name);

        dialog = new OptionsDialog(parent);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        dialog.setVisible(true);
    }
}
