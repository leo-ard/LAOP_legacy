package org.lrima.simulation.Interface.Actions;

import org.lrima.espece.network.NetworkStructure;
import org.lrima.espece.network.NeuralNetwork;
import org.lrima.espece.network.Neuron;
import org.lrima.simulation.Simulation;
import org.lrima.simulation.selection.NaturalSelection;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

public class SaveFileAction extends AbstractAction {

    NeuralNetwork nn;
    JFrame parent;
    Simulation simulation;

    public SaveFileAction(String name, JFrame parent, Simulation simulation){
        super(name);
        this.parent = parent;
        this.simulation = simulation;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //Met en pause la simulation
        this.simulation.pausing = true;

        this.nn = NaturalSelection.best.neuralNetwork;

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File("."));
        fileChooser.setApproveButtonText("Save");

        int returnVal = fileChooser.showOpenDialog(parent);

        if(returnVal == JFileChooser.APPROVE_OPTION){
            File savedFileSelected = fileChooser.getSelectedFile();
            save(savedFileSelected);

            //Reprend la simulation
            simulation.pausing = false;
        }
    }

    private void save(File file){
        try {
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            oos.writeObject(new NetworkStructure(nn));

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
