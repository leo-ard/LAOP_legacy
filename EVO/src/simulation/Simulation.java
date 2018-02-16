package simulation;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Comparator;

import core.CONSTANTS;
import espece.Espece;
import espece.capteur.Capteur;
import map.Map;
import map.MapPanel;
import map.obstacle.Obstacle;

public class Simulation extends Thread implements KeyListener{
	
	Map map;
	MapPanel mapPanel;
	
	public ArrayList<Espece> especesOpen;
	public ArrayList<Espece> especesClosed;
	
	FrameManager frame;
	
	boolean running = true;
	boolean pausing = false;
	
	
	//temporaire
	boolean D, G;
	
	double dt;
	public double time;
	
	public Simulation() {
		super();
		//init
		map = new Map("TODO", this);
		//mapPanel = new MapPanel(map, 1000,500);
		especesOpen = new ArrayList<>();
		this.resetAndAddEspeces();
		
		
	
		frame = new FrameManager("EVO", this);
		//frame.add(mapPanel);
		//frame.add(new NetworkPanel(this.especesOpen.get(0).neuralNetwork, 1000, 200), BorderLayout.SOUTH);
		//frame.pack();
		//frame.setVisible(true);
		frame.addKeyListener(this);
		//frame.requestFocus();
		
		running = true;
		
		time = 0;
	}
	
	public void run() {
		dt = 1000/60;
		long currTime = System.currentTimeMillis();
		long timePassed;
		while(true) {
			if(!pausing) {
				currTime = System.currentTimeMillis();
				boolean d = D, g = G;
				
				for(Espece e : especesOpen) {
					e.update(dt, d?1:0, g?1:0);
				}
				
				time += dt;
				for(int i = 0; i < especesOpen.size(); i++) {
					Espece e = especesOpen.get(i);
					
					e.resetCapteur();
					for(Obstacle o : map.obstacles) {
						if(o.fastCollision(e)) {
							for(Capteur c : e.getCapteursList()) {
								c.setValue(o.getCapteurValue(c));
							}
							
							if(o.collision(e)) {
								//e.update(dt,0,0);
								try {
								e.kill();
								especesClosed.add(e);
								especesOpen.remove(i);
								}catch(Exception e1) {
									System.err.println("FATAL");
								}
							}
						}
					}
				}
				
				if(especesOpen.size() == 0) {
					this.mutate();
				}
			}
			try {
				timePassed = System.currentTimeMillis() - currTime;
				if(timePassed > 16 && !pausing) {
					System.out.println("ERROR - NOT ANOUGHT TIME");
				}
				//System.out.println(timePassed);
				Simulation.sleep((long) ((dt - timePassed) > 0 ?dt - timePassed: 0));
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			
			//dt = pausing?0:1000/60;
			
			
		}
		
	}
	
	public void resetAndAddEspeces() {
		
		especesClosed = new ArrayList<>();
		
		while(especesOpen.size() < CONSTANTS.NUMBERCARS) {
			especesOpen.add(new Espece(map.depart, map.orientation));
		}
		
	}
	
	public void mutate() {
		especesClosed.sort(new Comparator<Espece>() {
			@Override
			public int compare(Espece e1, Espece e2) {
				if(e1.getFitness() == e2.getFitness())
					return 0;
				return e1.getFitness()>e2.getFitness()?-1:1;
			}
		});
		
		this.getFrameManager().changeNetworkFocus(especesClosed.get(0));
		
		/*for(int i = 0; i < especesClosed.size() && especesClosed.size() >= CONSTANTS.NUMBERCARS/2; i++) {
			if(Math.random()*CONSTANTS.NUMBERCARS+1 < i) {
				//System.out.println(especesClosed.get(i).getFitness());
				especesClosed.remove(i);
			}
			else {
				especesClosed.get(i).tpLikeNew(map.depart,map.orientation);
			}
		}*/
		//System.out.println("-----------------------------------");
		/*for(Espece e : especesClosed) {
			//System.out.println(e.getFitness());
			
			/*Espece e1 = new Espece(e);
			e1.mutate();
			especesClosed.add(e1);
		}*/
		
		/*while (especesClosed.size() < CONSTANTS.NUMBERCARS) {
			especesClosed.add(new Espece(map.depart, map.orientation));
		}*/
		
		/*especesOpen =  especesClosed;
		especesClosed = new ArrayList<Espece>();*/
		//System.out.println(especesOpen.size());
		//System.out.println(especesClosed);
		//especesOpen = especesClosed;
		this.resetAndAddEspeces();
		/*especesOpen = especesClosed;
		especesClosed = new ArrayList<Espece>();*/
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_D) {
			D = true;
		}
		if(e.getKeyCode() == KeyEvent.VK_A) {
			G = true;
		}
		
		/*if(e.getKeyCode() == KeyEvent.VK_0) {
			frame.changeNetworkFocus(this.especesClosed.get(2));
		}
		*/
		
	}
	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_D) {
			D = false;
		}
		if(e.getKeyCode() == KeyEvent.VK_A) {
			G = false;
		}
		if(e.getKeyCode() == KeyEvent.VK_SPACE) {
			pausing = !pausing;
			
			
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {}

	public ArrayList<Espece> getEspeces() {
		ArrayList<Espece> temp = new ArrayList<Espece>();
		temp.addAll(especesClosed);
		temp.addAll(especesOpen);
		
		return temp;
	}

	public FrameManager getFrameManager() {
		return frame;
	}

}
