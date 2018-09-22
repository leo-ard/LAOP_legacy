package org.lrima.Interface.conclusion;

import org.lrima.espece.network.algorithms.fullyconnected.FullyConnectedNeuralNetwork;
import org.lrima.espece.network.algorithms.neat.Genome;
import org.lrima.espece.network.interfaces.NeuralNetwork;
import org.lrima.simulation.SimulationInformation;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class ConclusionFrame extends JFrame {

    private HashMap<Class<?extends NeuralNetwork>, ArrayList<SimulationInformation>> simulationInformations;

    public static void main(String[] args) {
        HashMap<Class<?extends NeuralNetwork>, ArrayList<SimulationInformation>> s = new HashMap<>();
        s.put(Genome.class, new ArrayList<>());
        s.put(FullyConnectedNeuralNetwork.class, new ArrayList<>());


        ConclusionFrame frame = new ConclusionFrame(s);
        frame.setVisible(true);
    }

    public ConclusionFrame(HashMap<Class<?extends NeuralNetwork>, ArrayList<SimulationInformation>> simulationInformations){
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Conclusion");
        this.setResizable(false);

        this.simulationInformations = simulationInformations;


        setupPanels();
    }

    private void setupPanels(){
        JTabbedPane tabbedPane = new JTabbedPane();

        ChartPanel chartPanel = new ChartPanel(simulationInformations);
        TablePanel tablePanel = new TablePanel();

        tabbedPane.addTab("Charts", chartPanel);
        tabbedPane.addTab("Table", tablePanel);

        this.getContentPane().add(tabbedPane);
    }
}
