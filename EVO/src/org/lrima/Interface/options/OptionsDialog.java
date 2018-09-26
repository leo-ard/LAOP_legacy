package org.lrima.Interface.options;

import org.lrima.core.EVO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OptionsDialog extends JDialog implements ActionListener {

    private OptionVoiturePanel optionVoiturePanel;
    private OptionSimulationPanel optionSimulationPanel;
    private JButton saveButton, cancelButton;

    public OptionsDialog(){
        setTitle("Options");
        this.setupSize();
        this.setupButtons();
        this.setupTabs();
    }

    /**
     * Sets the size and position of the dialog
     */
    private void setupSize(){
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        //Set the site of the dialog and centers it on the screen
        int width =  screenSize.width / 3;
        int heigth = screenSize.height / 3;
        int x = (screenSize.width / 2) - (width / 2);
        int y = (screenSize.height / 2) - (heigth);

        setBounds(x, y, width, heigth);
        setResizable(false);
    }

    /**
     * Creates a button to save the options and one to cancel
     */
    private void setupButtons(){
        //The save button
        saveButton = new JButton("Save");
        saveButton.addActionListener(this);

        //The cancel button
        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(this);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        //Add the two button to the bottom of the dialog box
        add(buttonPanel, "South");
    }

    /**
     * Adds the options tabs to the dialog
     */
    private void setupTabs(){
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setTabPlacement(JTabbedPane.LEFT);

        optionVoiturePanel = new OptionVoiturePanel();
        optionSimulationPanel = new OptionSimulationPanel();

        tabbedPane.addTab("Voiture", optionVoiturePanel);
        tabbedPane.addTab("Simulation", optionSimulationPanel);

        add(tabbedPane);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == cancelButton){
            setVisible(false);
        }
        else if(e.getSource() == saveButton){
            //Notify the user that the simulation will restart
            boolean erreur = false;

            //There was an error in the values of the options in the car tab
            if (!optionVoiturePanel.save()) {
                JOptionPane.showMessageDialog(this, "Veuillez verifier les informations pour les voitures", "Erreur", JOptionPane.ERROR_MESSAGE);
                erreur = true;
            }
            //There was an error in the values of the simulation tab
            if (!optionSimulationPanel.save()) {
                JOptionPane.showMessageDialog(this, "Veuillez verifier les informations pour la simulation", "Erreur", JOptionPane.ERROR_MESSAGE);
                erreur = true;
            }

            //If all the options are valid
            if(!erreur){
                dispose();
                //EVO.restart();
            }
        }
    }
}
