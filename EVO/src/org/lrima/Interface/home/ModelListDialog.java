package org.lrima.Interface.home;

import org.lrima.Interface.home.pages.CompareAlgorithmsPanel;
import org.lrima.network.algorithms.AlgorithmManager;
import org.lrima.network.interfaces.NeuralNetworkModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class ModelListDialog extends JDialog {
    ArrayList<JCheckBox> modelCheckBoxes = new ArrayList<>();
    CompareAlgorithmsPanel compareAlgorithmsPanel;

    public ModelListDialog(CompareAlgorithmsPanel parent){
        compareAlgorithmsPanel = parent;
        this.setTitle("Add Model");

        JPanel checkBoxPanel = new JPanel();

        checkBoxPanel.setLayout(new GridLayout(AlgorithmManager.algorithms.size(), 2));

        for(int i = 0 ; i < AlgorithmManager.algorithms.size() ; i++){
            JCheckBox checkBox = new JCheckBox();
            modelCheckBoxes.add(checkBox);
            JLabel label = new JLabel(AlgorithmManager.algorithmsName.get(i));

            checkBoxPanel.add(checkBox);
            checkBoxPanel.add(label);
        }

        this.add(checkBoxPanel);

        JButton addButton = new JButton("Add");
        addButton.addActionListener(e -> {
            ArrayList<NeuralNetworkModel> modelsToAdd = new ArrayList<>();

            for(int i = 0 ; i < modelCheckBoxes.size() ; i++){
                if(modelCheckBoxes.get(i).isSelected()){
                    modelsToAdd.add(NeuralNetworkModel.getInstanceOf(AlgorithmManager.algorithms.get(i)));
                }
            }

            compareAlgorithmsPanel.addModels(modelsToAdd);
            dispose();
        });

        this.add(addButton, BorderLayout.SOUTH);

        this.pack();
    }
}
