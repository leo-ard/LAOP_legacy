package org.lrima.Interface.conclusion;

import org.lrima.simulation.SimulationBatch;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ConclusionFrame extends JFrame {

    public static void main(String[] args) {
        ConclusionFrame c = new ConclusionFrame(new ArrayList<SimulationBatch>());

        c.setVisible(true);
    }

    private ArrayList<SimulationBatch> simulationBatches;

    public ConclusionFrame(ArrayList<SimulationBatch> simulationBatches){
        Dimension screenSizes = Toolkit.getDefaultToolkit().getScreenSize();
        this.setSize(screenSizes);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Conclusion");
        this.setResizable(false);

        this.simulationBatches = simulationBatches;


        setupPanels();
    }

    private void setupPanels(){
        JTabbedPane tabbedPane = new JTabbedPane();

        ChartPanel chartPanel = new ChartPanel(simulationBatches);
        TablePanel tablePanel = new TablePanel();

        JScrollPane chartScrollPane = new JScrollPane(chartPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        tabbedPane.addTab("Charts", chartScrollPane);
        tabbedPane.addTab("Table", tablePanel);

        this.getContentPane().add(tabbedPane);
    }
}
