package org.lrima.Interface.home;

import org.lrima.network.algorithms.AlgorithmManager;
import org.lrima.network.interfaces.NeuralNetworkModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class ModelListDialog extends JDialog {

    ArrayList<Class<?extends NeuralNetworkModel>> models;
    ArrayList<JCheckBox> modelCheckBoxes = new ArrayList<>();
    CompareAlgorithmsFrame compareAlgorithmsFrame;

    public ModelListDialog(CompareAlgorithmsFrame parent){
        compareAlgorithmsFrame = parent;
        this.setTitle("Add Model");

        models = AlgorithmManager.algorithms;

        JPanel checkBoxPanel = new JPanel();

        checkBoxPanel.setLayout(new GridLayout(models.size(), 2));

        for(int i = 0 ; i < models.size() ; i++){
            JCheckBox checkBox = new JCheckBox();
            modelCheckBoxes.add(checkBox);
            JLabel label = new JLabel(AlgorithmManager.algorithmsName.get(i));

            checkBoxPanel.add(checkBox);
            checkBoxPanel.add(label);
        }

        this.add(checkBoxPanel);

        JButton addButton = new JButton("Add");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<Class<?extends NeuralNetworkModel>> modelsToAdd = new ArrayList<>();

                for(int i = 0 ; i < modelCheckBoxes.size() ; i++){
                    if(modelCheckBoxes.get(i).isSelected()){
                        modelsToAdd.add(models.get(i));
                    }
                }

                compareAlgorithmsFrame.addModels(modelsToAdd);

                dispose();
            }
        });

        this.add(addButton, BorderLayout.SOUTH);

        this.pack();
    }
}
