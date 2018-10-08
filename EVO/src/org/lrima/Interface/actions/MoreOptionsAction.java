package org.lrima.Interface.actions;

import org.lrima.Interface.FrameManager;
import org.lrima.Interface.options.OptionsDialog;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Opens the more option dialog
 */
public class MoreOptionsAction extends AbstractAction {

    private OptionsDialog dialog;

    public MoreOptionsAction(String name, FrameManager frameManager){
        super(name);
        dialog = new OptionsDialog();
        dialog.addOptionDialogListener(()->{
            int choise = JOptionPane.showConfirmDialog(frameManager, "WARNING ! To apply the changes, the simulation must be restarted and all progress will be lost. Do you want to restart the simulation ?", "Restart ?", JOptionPane.YES_NO_OPTION);
            if(choise == JOptionPane.YES_OPTION){
                frameManager.restart();
            }

        });
    }

    //Open the more option dialog
    @Override
    public void actionPerformed(ActionEvent e) {
        dialog.setVisible(true);
    }
}
