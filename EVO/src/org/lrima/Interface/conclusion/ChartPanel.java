package org.lrima.Interface.conclusion;

import org.lrima.Interface.GraphicPanel;
import org.lrima.espece.network.interfaces.NeuralNetwork;
import org.lrima.simulation.SimulationInformation;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class ChartPanel extends JPanel {
    private HashMap<Class<?extends NeuralNetwork>, ArrayList<SimulationInformation>> simulationInformations;
    static public int MARGIN_BETWEEN_CHARTS = 20;

    public ChartPanel(HashMap<Class<?extends NeuralNetwork>, ArrayList<SimulationInformation>> simulationInformations){
        this.simulationInformations = simulationInformations;

        JPanel graphicPanel = new JPanel();
        graphicPanel.setLayout(new GridLayout((int)Math.ceil(simulationInformations.keySet().size() / 2), 2, MARGIN_BETWEEN_CHARTS, MARGIN_BETWEEN_CHARTS));

        for(Class<?extends NeuralNetwork> algorithm : simulationInformations.keySet()){
            ChartComponent chart = new ChartComponent(algorithm, simulationInformations.get(algorithm));
            graphicPanel.add(chart);
        }

        this.add(graphicPanel);
    }
}
