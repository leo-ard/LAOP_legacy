package simulation;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;

import core.CONSTANTS;
import espece.Espece;
import espece.capteur.Capteur;
import espece.network.NetworkStructure;
import espece.network.NeuralNetwork;
import map.Map;
import map.MapPanel;
import map.obstacle.Obstacle;
import simulation.selection.NaturalSelection;

public class Simulation extends Thread implements KeyListener{
	
	Map map;
	MapPanel mapPanel;

	int generation = 0;
	
	public ArrayList<Espece> especesOpen;
	public ArrayList<Espece> especesClosed;
	
	//FrameManager frame;
	
	boolean running = true;
	boolean pausing = false;
	boolean REALTIMEMODE = false;
	boolean MANUALMODE = false;
	boolean RANDOMMAP = false;
	
	
	//temporaire
	boolean D, G;
	
	double dt;
	public double time;

	public Simulation() {
		super();
		//init
		map = new Map("TODO", this);
		especesOpen = new ArrayList<Espece>();
		this.resetAndAddEspeces();
		
		running = true;
		
		time = 0;

	}
	
	public void run() {
		dt = 8;
		long currTime = System.currentTimeMillis();
		long timePassed;
		while(true) {
			if(!pausing) {
				if(especesOpen.size() != 0 && time < CONSTANTS.TIME_LIMIT) {
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
									try {
										especesClosed.add(e);
										//e.update(dt,0,0);
										e.kill();
										map.setFitnessToEspece(e);
										
										especesOpen.remove(i);
									}catch(Exception e1) {
										System.err.println("FATAL");
										e1.printStackTrace();
									}
								}
							}
						}
					}
					
					
				}
				else {
					nextGeneration();
				}
			}
			
			if(REALTIMEMODE) {
				try {
					timePassed = System.currentTimeMillis() - currTime;
					if(timePassed > dt) {
					}
					//System.out.println(timePassed);
					
					if(especesOpen.size() == 0) {
						
					}
					timePassed = System.currentTimeMillis() - currTime;
					//System.out.println(timePassed);
					Simulation.sleep((long) ((dt - timePassed) > 0 ?dt - timePassed: 0));
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
			//  dt = pausing?0:1000/60;
			
			
		}
		
	}

	private void nextGeneration(){
		this.generation++;

        for(int i = 0; i < especesOpen.size(); i++) {
            Espece espece = especesOpen.get(i);

			map.setFitnessToEspece(espece);

            espece.kill();
            especesClosed.add(espece);
            especesOpen.remove(i);
        }

        this.mutate();

        //Check if should save best NN
        if(NaturalSelection.best != null){
            //Load le currentBest
            NeuralNetwork currentBest = null;
            NetworkStructure bestNS = NetworkStructure.load("best_nn.dat");

            if(bestNS != null) {
                currentBest = new NeuralNetwork(bestNS);
            }

            if(currentBest != null){
                System.out.println("Saved best: " + currentBest.getFitness() + " Now: " + NaturalSelection.best.getFitness());

                //Check si le nouveau est meilleur
                if(NaturalSelection.best.getFitness() > currentBest.getFitness()){
                    System.out.println("BETTER!!");
                    saveBest();
                }

            }else{
                saveBest();
            }
        }


        time = 0;

        //Change la map a chaque 10 generations
        if(RANDOMMAP) {
            if (this.generation % 10 == 0) {
                map.createRandomMap();
            }
        }

    }

    /**
     * Sauvegarde dans un fichier le meilleur neuralNetwork
     */
    private void saveBest(){
        Espece espece = NaturalSelection.best;

        if(espece != null) {
            NetworkStructure.save("best_nn.dat", espece.neuralNetwork);
        }
    }
	
	public void resetAndAddEspeces() {
		especesClosed = new ArrayList<>();
		
		while(especesOpen.size() < CONSTANTS.NUMBERCARS) {
			especesOpen.add(new Espece(map.depart, map.orientation, this.map));
		}
		
	}
	
	public void mutate() {
		NaturalSelection selection = new NaturalSelection(especesClosed);
		this.especesOpen = selection.getMutatedList(map);
		this.especesClosed = new ArrayList<Espece>();
		
	}

	private Espece getSelectedEspece(){
        for(Espece espece : this.especesOpen){
            if(espece.selected){
                return espece;
            }
        }

        return null;
    }

	@Override
	public void keyPressed(KeyEvent e) {
	    //Next generation
	    if(e.getKeyCode() == KeyEvent.VK_Q){
            nextGeneration();
        }
		if(e.getKeyCode() == KeyEvent.VK_X){
	        REALTIMEMODE = true;
	        MANUALMODE = true;
        }
		if(e.getKeyCode() == KeyEvent.VK_R) {
			REALTIMEMODE = !REALTIMEMODE;
		}

		if(MANUALMODE){
            Espece selected = getSelectedEspece();

	        if(e.getKeyCode() == KeyEvent.VK_A){
	            selected.shouldListenToNN = false;
	            selected.manual_left = 1.0;
                System.out.println("gauche");
            }
            else if(e.getKeyCode() == KeyEvent.VK_D){
                selected.shouldListenToNN = false;
                selected.manual_right = 1.0;
                System.out.println("droite");
            }
        }
		
		if(e.getKeyCode() == KeyEvent.VK_SPACE) {
			pausing=!pausing;
		}
		
		
	}
	@Override
	public void keyReleased(KeyEvent e) {
        if(MANUALMODE) {
            Espece selected = getSelectedEspece();
            if (e.getKeyCode() == KeyEvent.VK_D) {
                selected.shouldListenToNN = true;
                selected.manual_right = 0.0;

            }
            if (e.getKeyCode() == KeyEvent.VK_A) {
                selected.shouldListenToNN = true;
                selected.manual_left = 0.0;
            }
        }
		
	}

	@Override
	public void keyTyped(KeyEvent e) {}

	public ArrayList<Espece> getEspeces() {
		ArrayList<Espece> temp = new ArrayList<>();
		temp.addAll(especesClosed);
		temp.addAll(especesOpen);
		
		return temp;
	}

}
