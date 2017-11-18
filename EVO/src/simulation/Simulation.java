package simulation;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.Timer;

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
			especes.add(new Espece(map.depart));
		}
		
		frame.setVisible(true);
		frame.addKeyListener(this);
		frame.requestFocus();
		
		running = true;
		
		time = 0;
		
		
		
	}
	
	public void run() {
		long dt = 16;
		long currTime = System.currentTimeMillis();
		long timePassed;
		while(true) {
			timePassed = System.currentTimeMillis() - currTime;
			time += dt;
			for(Espece e : especes) {
				e.update(dt, D?1:0, G?1:0);
				for(Obstacle o : map.obstacles) {
					if(o.fastCollision(e)) {
						for(Capteur c : e.capteurs) {
							o.getCapteurValue(c);
						}
						
						
						if(o.collision(e)) {
							e.tp(map.depart);
						}
					}
				}
			}
			
			
			
			try {
				if(timePassed > 16) {
					System.out.println("ERROR - NOT ANOUGHT TIME");
				}
				
				Simulation.sleep((1000/60 - timePassed) > 0 ?1000/60 - timePassed: 0 );
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			currTime = System.currentTimeMillis();
			
			
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
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

}
