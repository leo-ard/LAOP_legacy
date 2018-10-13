package org.lrima.Interface;

import org.knowm.xchart.*;
import org.knowm.xchart.style.markers.SeriesMarkers;
import org.lrima.simulation.Generation;
import org.lrima.simulation.Simulation;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

//TODO: Finir le graphique
//TODO: s'updater tout seul avec un listener
public class GraphicPanel extends JPanel {
    private ArrayList<Generation> generations;
    private XYChart chart;
    private XChartPanel<XYChart> chartPanel;
    private Dimension screenSize;

    private ArrayList<Integer> xFitnessData = new ArrayList<>();;
    private ArrayList<Double> yFitnessData = new ArrayList<>();;

    private ArrayList<Integer> xMedianData = new ArrayList<>();;
    private ArrayList<Integer> yMedianData = new ArrayList<>();;

    private ArrayList<Integer> xMoyenneData = new ArrayList<>();;
    private ArrayList<Integer> yMoyenneData = new ArrayList<>();;

    public GraphicPanel(FrameManager frameManager){
        this.generations = frameManager.getSimulationManager().getCurrentSimulation().getGenerationList();
        screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setPreferredSize(new Dimension(0, screenSize.height / 4));
        this.setLayout(new BorderLayout());

        createChart();
    }

    public void nextSimulation(Simulation simulation){
        this.generations = simulation.getGenerationList();
        this.xFitnessData = new ArrayList<>();
        this.yFitnessData = new ArrayList<>();
        this.xMedianData = new ArrayList<>();
        this.yMedianData = new ArrayList<>();
        this.xMoyenneData = new ArrayList<>();
        this.yMoyenneData = new ArrayList<>();

        getFitnessData();
        updateChart();
    }

    private void createChart(){

        getFitnessData();

        chart = new XYChartBuilder().title("Fitness over generations").xAxisTitle("Generation").yAxisTitle("Fitness").build();
        chart.getStyler().setChartBackgroundColor(new Color(230, 230, 230));

        //Serie for the fitness
        XYSeries series = chart.addSeries("fitness", new int[]{0}, new int[]{0});
        series.setXYSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Scatter);
        series.setFillColor(Color.RED);

        //Serie for the median fitness
        XYSeries medianSeries = chart.addSeries("median", new int[]{0}, new int[]{0});
        medianSeries.setMarker(SeriesMarkers.NONE);

        //Serie for the average fitness
        XYSeries moyenneSeries = chart.addSeries("average", new int[]{0}, new int[]{0});
        moyenneSeries.setMarker(SeriesMarkers.NONE);

        chartPanel = new XChartPanel<>(chart);

        this.add(chartPanel, BorderLayout.CENTER);
    }

    private void getFitnessData(){
        xFitnessData = new ArrayList<>();
        yFitnessData = new ArrayList<>();
        xMedianData = new ArrayList<>();
        yMedianData = new ArrayList<>();
        xMoyenneData = new ArrayList<>();
        yMoyenneData = new ArrayList<>();

        for(Generation gen : this.generations){
            ArrayList<Double> allFitnesses = gen.getAllFitnesses();
            for(double fitness : allFitnesses) {
                this.xFitnessData.add(gen.getGenerationNumber());
                this.yFitnessData.add(fitness);
            }

            double medianFitness = gen.getMedianFitness();
            this.xMedianData.add(gen.getGenerationNumber());
            this.yMedianData.add((int)medianFitness);

            double moyenneFitness = gen.getMoyenneFitness();
            this.xMoyenneData.add(gen.getGenerationNumber());
            this.yMoyenneData.add((int)moyenneFitness);
        }
    }

    public void updateChart(){
        //Keep a maximum of 50 generations
        if(this.generations.size() > 50){
            //Randomly remove a generation
            this.generations.remove(0);
        }

        getFitnessData();

        this.chart.updateXYSeries("fitness", xFitnessData, yFitnessData, null);
        this.chart.updateXYSeries("median", xMedianData, yMedianData, null);
        this.chart.updateXYSeries("average", xMoyenneData, yMoyenneData, null);
    }
}
