package org.lrima.core;

import java.awt.EventQueue;

import org.lrima.Interface.FrameManager;
import org.lrima.espece.network.algorithms.neat.Genome;
import org.lrima.simulation.Simulation;

public class EVO {

	public static FrameManager frame;

	public static void main(String[] args) {
		UserPrefs prefs = new UserPrefs();
        UserPrefs.load();

		start();

	}

	public static void restart() {
        UserPrefs.load();
        frame.setVisible(false);
        start();
    }

    private static void start(){
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    frame = new FrameManager();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
