package org.lrima.simulation.Interface.actions;

import org.lrima.espece.network.NetworkStructure;
import org.lrima.espece.network.NeuralNetwork;
import org.lrima.simulation.Simulation;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

/**
 * Load a neural network from a file
 */
public class LoadNeuralNetworkFileAction extends AbstractAction {

    private JFrame parent;
    private Simulation simulation;

    public LoadNeuralNetworkFileAction(String name, JFrame parent, Simulation simulation){
        super(name);
        this.parent = parent;
        this.simulation = simulation;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //Setup the file chooser
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File("."));
        fileChooser.setApproveButtonText("Load");

        int returnVal = fileChooser.showOpenDialog(parent);

        if(returnVal == JFileChooser.APPROVE_OPTION){
            NeuralNetwork nn = load(fileChooser.getSelectedFile());

            //Restart la simulation
            if(nn != null) {
                //Change the neural network to the new one
                simulation.changeNeuralNetwork(nn);
            }
        }
    }

    /**
     * Loads a neural network from a file
     * @param file the file to load the neural network from
     * @return the neural network that was found in the file or null
     */
    private NeuralNetwork load(File file){
        //Try to open the file and read the neural network
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
