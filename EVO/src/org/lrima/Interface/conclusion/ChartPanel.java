package org.lrima.Interface.conclusion;

import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.lrima.network.interfaces.NeuralNetworkModel;
import org.lrima.simulation.SimulationBatch;
import org.lrima.simulation.SimulationInformation;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ChartPanel extends JPanel {
    //private HashMap<Class<?extends NeuralNetwork>, ArrayList<SimulationInformation>> simulationInformations;
    private ArrayList<SimulationBatch> algorithmBatches;
    static public int MARGIN_BETWEEN_CHARTS = 20;
    private Dimension screenDimension;


    public ChartPanel(ArrayList<SimulationBatch> algorithmSimulations){
        this.algorithmBatches = algorithmSimulations;
        this.screenDimension = Toolkit.getDefaultToolkit().getScreenSize();

        this.setLayout(new BorderLayout(0, MARGIN_BETWEEN_CHARTS));
        this.setBorder(BorderFactory.createEmptyBorder(MARGIN_BETWEEN_CHARTS, MARGIN_BETWEEN_CHARTS, MARGIN_BETWEEN_CHARTS, MARGIN_BETWEEN_CHARTS));

        JPanel graphicPanel = new JPanel();
        graphicPanel.setLayout(new GridLayout((int)Math.ceil((double)algorithmSimulations.size() / 2.0), 2, MARGIN_BETWEEN_CHARTS, MARGIN_BETWEEN_CHARTS));

        this.addChartWithAllAlgorithms();

        //Finds the max and min fitness
        double max = 0;
        double min = 0;

        for(SimulationBatch batch : this.algorithmBatches){
            ArrayList<SimulationInformation> informations = batch.getSimulationInformations();
            for(SimulationInformation information : informations){
                if(max < information.getMaxFitness()){
                    max = information.getMaxFitness();
                }
                if(min > information.getMinFitness()){
                    min = information.getMinFitness();
                }
            }
        }

        for(SimulationBatch batch : this.algorithmBatches){
            NeuralNetworkModel algorithm = batch.getAlgorithmModel();
            ArrayList<SimulationInformation> simulationInformations = batch.getSimulationInformations();

            ChartComponent chart = new ChartComponent(algorithm, simulationInformations, max, min);
            graphicPanel.add(chart);
        }

        this.add(graphicPanel, BorderLayout.CENTER);
    }

    private void addChartWithAllAlgorithms(){
        int chartWidth = this.screenDimension.width - (MARGIN_BETWEEN_CHARTS * 2);
        int chartHeight = (int)(screenDimension.height / 3);
        XYChart allAlgorithmChart = new XYChartBuilder().title("Overall fitness over time").xAxisTitle("Generation").yAxisTitle("Fitness").width(chartWidth).height(chartHeight).build();

        for(SimulationBatch batch : this.algorithmBatches){
            allAlgorithmChart.addSeries(batch.getAlgorithmModel().getAlgorithmInformationAnnotation().name(), this.getGenerationsAsList(batch), batch.getAverageFitnessPerGeneration());
        }

        XChartPanel<XYChart> chartPanel = new XChartPanel<>(allAlgorithmChart);

        this.add(chartPanel, BorderLayout.NORTH);
    }

    private double[] getGenerationsAsList(SimulationBatch batch){
        double[] generations = new double[batch.getSimulations()[0].getGenerationList().size()];

        for(int i = 0 ; i < generations.length ; i++){
            generations[i] = i + 1;
        }

        return generations;
    }
}
