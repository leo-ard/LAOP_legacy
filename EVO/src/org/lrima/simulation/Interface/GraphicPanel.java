package org.lrima.simulation.Interface;

import org.knowm.xchart.*;
import org.lrima.espece.Espece;
import org.lrima.simulation.Simulation;
import org.lrima.simulation.SimulationInfos;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class GraphicPanel extends JPanel {

    private SimulationInfos simulationInfos;
    private XYChart chart;
    XChartPanel<XYChart> chartPanel;
    Dimension screenSize;

    public GraphicPanel(SimulationInfos infos){
        this.simulationInfos = infos;
        screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBackground(Color.CYAN);

        createChart();
    }

    private void createChart(){
        Random r = new Random();

        double[] xData = {1, 1, 1, 1, 2, 2, 3};
        double[] yData = {1, 2, 3, 4, 1, 2, 7};

        chart = new XYChartBuilder().width(screenSize.width).height(screenSize.height / 5).title("Graphiques").xAxisTitle("Generation").yAxisTitle("Fitness").build();
        XYSeries series = chart.addSeries("fitness", xData, yData);
        series.setXYSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Scatter);
        series.setFillColor(Color.RED);

        chartPanel = new XChartPanel<>(chart);

        this.add(chartPanel);
    }

    public void updateChart(Simulation simulation){
        ArrayList<Integer> xData = new ArrayList<>();
        ArrayList<Double> yData = new ArrayList<>();

        for(Espece e : simulation.getAllEspeces()){
            xData.add(simulation.getGeneration());
            yData.add(e.getFitness());
        }

        this.chart.updateXYSeries("fitness", xData, yData, null);
        //chartPanel.
    }
}
