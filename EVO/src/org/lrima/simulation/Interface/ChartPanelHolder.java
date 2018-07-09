package org.lrima.simulation.Interface;

import org.knowm.xchart.*;
import org.knowm.xchart.style.Styler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class ChartPanelHolder {
    public static int nombreDeVoitureParLigneBigScreen = 10;
    public static int nombreDeVoitureParLigneNormal = 10;



    public static XChartPanel getChartPanel(int x, int y, double[]xData, double[] yData, boolean bigScreen) {
        //System.out.println("è");
        CategoryChart chart = new CategoryChartBuilder().width(800).height(600).theme(Styler.ChartTheme.Matlab).xAxisTitle("Fitness").yAxisTitle("Nombre d'espèce").title("Le nombre d'espèce en fonction de la Fitness").build();
        List<String> xDat = new ArrayList<String>();
        List<Integer> yDat = new ArrayList<Integer>();

        chart.getStyler().setChartTitleFont(chart.getStyler().getChartTitleFont().deriveFont(28.0f));
        chart.getStyler().setAxisTitleFont(chart.getStyler().getAxisTitleFont().deriveFont(18.0f));
        chart.getStyler().setAxisTickLabelsFont(chart.getStyler().getAxisTickLabelsFont().deriveFont(12.0f));
        chart.getStyler().setOverlapped(true);

        chart.getStyler().setLegendVisible(false);
        int nombreDeVoitureParLigne;
        if(bigScreen)
            nombreDeVoitureParLigne = nombreDeVoitureParLigneBigScreen;
        else
            nombreDeVoitureParLigne = nombreDeVoitureParLigneNormal;


        if(xData.length == 0 || yData.length == 0){
            return new XChartPanel<CategoryChart>(chart);
        }

        int index1 = 0;

        for (int i = 0; i <= yData[yData.length-1]/ nombreDeVoitureParLigne; i++) {
            if(!bigScreen)
               xDat.add(""+((i+1)%5==0?(i+1)* nombreDeVoitureParLigne :" "));
            else
                xDat.add(i*nombreDeVoitureParLigne+"-"+(i+1)* nombreDeVoitureParLigne +"");
            int counter = 0;
            while(index1 + counter < xData.length && yData[index1 + counter] < (i+1)* nombreDeVoitureParLigne){
               counter++;
            }
            index1 += counter;
            yDat.add(counter);
        }

        //System.out.println(xDat + " " + yDat);

        chart.addSeries("idk", xDat, yDat);
        XChartPanel<CategoryChart> xChartPanel = new XChartPanel<CategoryChart>(chart);

        if(!bigScreen)
        xChartPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getButton() == 1 && e.getClickCount() == 2){
                JFrame f = new JFrame();
                    f.setLayout(new BorderLayout());
                    f.add(getChartPanel(300,300,xData, yData, true), BorderLayout.CENTER);
                    f.pack();
                    f.setVisible(true);
                }
            }
        });

        return xChartPanel;
    }

    public static XChartPanel buildNormalGraph(int x, int y, double[]xData, double[]yData){
        XYChart chart = new XYChartBuilder().width(x).height(y).title("simple name").xAxisTitle("org/lrima/espece").yAxisTitle("score").build();
        System.out.println("MEEE : " + yData.length);

        chart.getStyler().setLegendVisible(false);

        for (int i = 0; i < yData.length; i++) {
            System.out.println(xData[i] + " "+ yData[i]);
        }
        if(yData.length != 0)
            chart.addSeries("one", xData, yData);
        return new XChartPanel<XYChart>(chart);
    }


}
