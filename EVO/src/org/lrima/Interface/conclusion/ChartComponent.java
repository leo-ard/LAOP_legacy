package org.lrima.Interface.conclusion;

import org.knowm.xchart.*;
import org.knowm.xchart.style.markers.SeriesMarkers;
import org.lrima.network.interfaces.NeuralNetworkModel;
import org.lrima.simulation.SimulationInformation;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.ImageFilter;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;

public class ChartComponent extends JComponent {

    private XYChart batchChart;
    private double[][] dataX;
    private double[][] dataY;

    private ArrayList<SimulationInformation> simulations;
    private NeuralNetworkModel algorithm;

    private Dimension screensize;
    private double max, min;

    private JButton saveButton = new JButton("Save");

    public ChartComponent(NeuralNetworkModel algorithm, ArrayList<SimulationInformation> simulations, double max, double min){
        super();
        this.algorithm = algorithm;
        this.simulations = simulations;
        this.screensize = Toolkit.getDefaultToolkit().getScreenSize();
        this.min = min;
        this.max = max;

        this.setLayout(new BorderLayout(0, 10));

        this.showChart();
        this.add(saveButton, BorderLayout.SOUTH);

        //When click on save button
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser(new File("."));
                fileChooser.setApproveButtonText("Save");


                FileNameExtensionFilter jpgFilter = new FileNameExtensionFilter("JPG files", "jpg");
                fileChooser.setFileFilter(jpgFilter);

                int response = fileChooser.showOpenDialog(ChartComponent.this.getParent());
                if(response == JFileChooser.APPROVE_OPTION){
                    File fileToSave = fileChooser.getSelectedFile();

                    //Save the file
                    try {
                        //System.out.println(fileToSave.getName());
                        if(fileToSave.getName().split("\\.")[1].toLowerCase() != "jpg"){
                            throw new IOException("The file name should be of type jpg !");
                        }

                        BitmapEncoder.saveJPGWithQuality(batchChart, fileToSave.getAbsolutePath(), 1f);
                    }catch (IOException ex){
                        JOptionPane.showMessageDialog(ChartComponent.this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }

                }
            }
        });
    }

    private void showChart(){
        int chartWidth = (screensize.width / 2) - (ChartPanel.MARGIN_BETWEEN_CHARTS);
        batchChart = new XYChartBuilder().title(algorithm.getAlgorithmInformationAnnotation().name()).xAxisTitle("Generation").yAxisTitle("Fitness").width(chartWidth).height(screensize.height / 3).build();
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
