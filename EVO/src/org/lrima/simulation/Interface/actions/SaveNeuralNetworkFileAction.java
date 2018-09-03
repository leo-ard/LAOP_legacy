package org.lrima.simulation.Interface.actions;

import org.lrima.espece.network.NetworkStructure;
import org.lrima.espece.network.NeuralNetwork;
import org.lrima.simulation.Simulation;
import org.lrima.simulation.selection.NaturalSelection;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

/**
 * Saves the best neural network into a file
 */
public class SaveNeuralNetworkFileAction extends AbstractAction {

    private NeuralNetwork nn;
    private JFrame parent;
    private Simulation simulation;

    public SaveNeuralNetworkFileAction(String name, JFrame parent, Simulation simulation){
        super(name);
        this.parent = parent;
        this.simulation = simulation;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //Met en pause la simulation
        this.simulation.setPausing(true);

        //Get the best neural network
        this.nn = NaturalSelection.best.neuralNetwork;

        //Setup the file chooser
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File("."));
        fileChooser.setApproveButtonText("Save");

        int returnVal = fileChooser.showOpenDialog(parent);

        if(returnVal == JFileChooser.APPROVE_OPTION){
            File savedFileSelected = fileChooser.getSelectedFile();
            save(savedFileSelected);

            //Reprend la simulation
            simulation.setPausing(false);
        }
    }

    /**
     * Saves the neural network to a file
     * @param file the file to save the neural network into
     */
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