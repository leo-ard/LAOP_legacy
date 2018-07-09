package org.lrima.simulation.Interface.Actions;

import org.lrima.espece.network.NetworkStructure;
import org.lrima.espece.network.NeuralNetwork;
import org.lrima.simulation.Simulation;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

public class LoadFileAction extends AbstractAction {

    JFrame parent;
    Simulation simulation;

    public LoadFileAction(String name, JFrame parent, Simulation simulation){
        super(name);
        this.parent = parent;
        this.simulation = simulation;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File("."));
        fileChooser.setApproveButtonText("Load");

        int returnVal = fileChooser.showOpenDialog(parent);

        if(returnVal == JFileChooser.APPROVE_OPTION){
            NeuralNetwork nn = load(fileChooser.getSelectedFile());

            //Restart la simulation
            if(nn != null) {
                simulation.changeNeuralNetwork(nn);
            }
        }
    }

    private NeuralNetwork load(File file){
        try {
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);

            NetworkStructure ns = (NetworkStructure) ois.readObject();

            return new NeuralNetwork(ns);

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
