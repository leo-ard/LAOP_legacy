package org.lrima.core;

import java.awt.EventQueue;

import org.lrima.simulation.FrameManager;
import org.lrima.simulation.Simulation;

public class EVO {
	
	public static Simulation simulation;
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
        simulation = new Simulation();
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    frame = new FrameManager(simulation);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        simulation.start();
    }

}
