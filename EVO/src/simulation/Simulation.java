package simulation;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import espece.Espece;
import espece.capteur.Capteur;
import map.Map;
import map.MapPanel;
import map.obstacle.Obstacle;

public class Simulation extends Thread implements KeyListener{
	
	Map map;
	MapPanel mapPanel;
	
	public ArrayList<Espece> especes;
	
	FrameManager frame;
	
	boolean running = true;
	
	
	//temporaire
	boolean D, G;
	
	public double time;
	
	public Simulation() {
		super();
		especes = new ArrayList<>();
		
		
		
		//init
		map = new Map("TODO", this);
		mapPanel = new MapPanel(map, 1000,500);
		frame = new FrameManager("EVO", this, mapPanel);
		
		
		
		for(int i = 0; i < 1; i++) {
			especes.add(new Espece(map.depart, map.orientation));
		}
		
		frame.setVisible(true);
		frame.addKeyListener(this);
		frame.requestFocus();
		
		running = true;
		
		time = 0;
		
		
		
	}
	
	public void run() {
		double dt = 1000/60;
		long currTime = System.currentTimeMillis();
		long timePassed;
		while(true) {
			currTime = System.currentTimeMillis();
			
			time += dt;
			for(Espece e : especes) {
				e.resetCapteur();
				for(Obstacle o : map.obstacles) {
					if(o.fastCollision(e)) {
						for(Capteur c : e.capteurs) {
							c.setValue(o.getCapteurValue(c));
						}
						
						
						if(o.collision(e)) {
							e.tp(map.depart);
							e.setAngle(map.orientation);
						}
					}
				}
			}
			
			for(Espece e : especes) {
				e.update(dt, D?1:0, G?1:0);
			}
			
			
			
			try {
				timePassed = System.currentTimeMillis() - currTime;
				if(timePassed > 16) {
					System.out.println("ERROR - NOT ANOUGHT TIME");
				}
				//System.out.println(timePassed);
				Simulation.sleep((long) ((dt - timePassed) > 0 ?dt - timePassed: 0));
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			
			
			
		}
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_D) {
			D = true;
		}
		if(e.getKeyCode() == KeyEvent.VK_A) {
			G = true;
		}
		
		
	}
	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_D) {
			D = false;
		}
		if(e.getKeyCode() == KeyEvent.VK_A) {
			G = false;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {}

}
