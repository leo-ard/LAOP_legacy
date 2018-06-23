package core;

import java.awt.EventQueue;

import simulation.FrameManager;
import simulation.Simulation;

public class EVO {
	
	public static Simulation simulation;
	public static FrameManager frame;

	public static void main(String[] args) {
		simulation =  new Simulation();
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frame = new FrameManager("EVO", simulation);
					frame.start();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		simulation.start();
	}

}
