package org.lrima.network.algorithms.neat;

import javax.swing.*;
import java.awt.*;

public class TestPanel extends JPanel {

    NeatGenome genome;

    public TestPanel(NeatGenome genome){
        this.genome = genome;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        //g.fillRect(0, 0, 100, 100);
        genome.draw((Graphics2D) g, null);
    }
}
