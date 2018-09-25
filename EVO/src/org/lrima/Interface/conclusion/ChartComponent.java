package org.lrima.Interface.conclusion;

import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.style.markers.Marker;
import org.knowm.xchart.style.markers.SeriesMarkers;
import org.lrima.espece.network.annotations.AlgorithmInformation;
import org.lrima.espece.network.interfaces.NeuralNetwork;
import org.lrima.simulation.SimulationInformation;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ChartComponent extends JComponent {

    private XYChart batchChart;
    private double[][] dataX;
    private double[][] dataY;

    private ArrayList<SimulationInformation> simulations;
    private Class<?extends NeuralNetwork> algorithm;

    private Dimension screensize;
    private double max, min;

    public ChartComponent(Class<?extends NeuralNetwork> algorithm, ArrayList<SimulationInformation> simulations, double max, double min){
        super();
        this.algorithm = algorithm;
        this.simulations = simulations;
        this.screensize = Toolkit.getDefaultToolkit().getScreenSize();
        this.min = min;
        this.max = max;

        this.setLayout(new BorderLayout());

        this.showChart();
    }

    private void showChart(){
        int chartWidth = (screensize.width / 2) - (ChartPanel.MARGIN_BETWEEN_CHARTS);
        batchChart = new XYChartBuilder().title(algorithm.getAnnotation(AlgorithmInformation.class).name()).xAxisTitle("Generation").yAxisTitle("Fitness").width(chartWidth).height(screensize.height / 3).build();
        batchChart.getStyler().setYAxisMax(max);
        batchChart.getStyler().setYAxisMin(min);
        this.getData();

        //Lines
        for(int i = 0 ; i < simulations.size() ; i++){
            SimulationInformation simulation = simulations.get(i);
            XYSeries serie = batchChart.addSeries("sim" + (i + 1), dataX[i], dataY[i]);
            serie.setMarker(SeriesMarkers.NONE);
        }


        XChartPanel<XYChart> chartPanel = new XChartPanel<>(batchChart);
        this.add(chartPanel);
    }

    /**
     * Gets the data from the list of simulations and separates it into xData and yData to get shown in the chart
     */
    private void getData(){
        this.dataX = new double[this.simulations.size()][this.simulations.get(0).getGenerations().size()];
        this.dataY = new double[this.simulations.size()][this.simulations.get(0).getGenerations().size()];

        for(int simulationIndex = 0 ; simulationIndex < simulations.size() ; simulationIndex++){
            SimulationInformation simulation = simulations.get(simulationIndex);
            for(int generationIndex = 0 ; generationIndex < dataY[0].length ; generationIndex++){
                dataX[simulationIndex][generationIndex] = generationIndex + 1;
                dataY[simulationIndex][generationIndex] = simulation.getGenerations().get(generationIndex).getMoyenneFitness();
            }
        }
    }

}
