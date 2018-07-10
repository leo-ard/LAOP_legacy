package org.lrima.simulation.Interface.options;

import org.lrima.core.EVO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OptionsDialog extends JDialog implements ActionListener {

    VoiturePanel voiturePanel;
    SimulationPanel simulationPanel;
    JButton save, cancel;

    public boolean haveToRestart = false;

    public OptionsDialog(JFrame parent){
        int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
        int screenHeight = Toolkit.getDefaultToolkit().getScreenSize().width;
        int width =  screenWidth/ 3;
        int heigth = screenHeight / 3;
        int x = (screenWidth / 2) - (width / 2);
        int y = (screenHeight / 2) - (heigth);

        setTitle("Options");
        setBounds(x, y, width, heigth);
        setResizable(false);

        //Setup les boutons save et cancel
        save = new JButton("Save");
        save.addActionListener(this);
        cancel = new JButton("Cancel");
        cancel.addActionListener(this);
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(save);
        buttonPanel.add(cancel);
        add(buttonPanel, "South");

        //Setup les panneaux
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setTabPlacement(JTabbedPane.LEFT);

        voiturePanel = new VoiturePanel();
        simulationPanel = new SimulationPanel();

        tabbedPane.addTab("Voiture", voiturePanel);
        tabbedPane.addTab("Simulation", simulationPanel);

        add(tabbedPane);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == cancel){
            setVisible(false);
        }
        else if(e.getSource() == save){
            boolean erreur = false;
            int warningResponse = JOptionPane.showConfirmDialog(this, "La simulation va reloader. Continuer ?", "Warning", JOptionPane.WARNING_MESSAGE);

            if (warningResponse == JOptionPane.YES_OPTION) {
                if (!voiturePanel.save()) {
                    JOptionPane.showMessageDialog(this, "Veuillez verifier les informations pour les voitures", "Erreur", JOptionPane.ERROR_MESSAGE);
                    erreur = true;
                }

                if (!simulationPanel.save()) {
                    JOptionPane.showMessageDialog(this, "Veuillez verifier les informations pour la simulation", "Erreur", JOptionPane.ERROR_MESSAGE);
                    erreur = true;
                }

                if(!erreur){
                    setVisible(false);
                    EVO.restart();
                }
            }
        }
    }
}
