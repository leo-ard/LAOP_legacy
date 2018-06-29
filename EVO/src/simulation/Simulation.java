package simulation;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;

import core.CONSTANTS;
import espece.Espece;
import espece.capteur.Capteur;
import espece.network.NeuralNetwork;
import map.Map;
import map.MapPanel;
import map.obstacle.Obstacle;
import simulation.selection.NaturalSelection;

public class Simulation extends Thread implements KeyListener{
	
	Map map;
	MapPanel mapPanel;
	
	public ArrayList<Espece> especesOpen;
	public ArrayList<Espece> especesClosed;
	
	//FrameManager frame;
	
	boolean running = true;
	boolean pausing = false;
	boolean REALTIMEMODE = false;
	
	
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
						System.out.println("ERROR - NOT ENOUGHT TIME");
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
        for(int i = 0; i < especesOpen.size(); i++) {
            Espece espece = especesOpen.get(i);

            espece.kill();
            map.setFitnessToEspece(espece);
            especesClosed.add(espece);
            especesOpen.remove(i);
        }

        this.mutate();

        //Check if should save best NN
        if(NaturalSelection.best != null){
            //Load le currentBest
            NeuralNetwork currentBest = null;
            try{
                FileInputStream fis = new FileInputStream("best_nn.dat");
                ObjectInputStream ois = new ObjectInputStream(fis);

                currentBest = (NeuralNetwork) ois.readObject();


            } catch (FileNotFoundException e) {
                currentBest = null;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }


            if(currentBest != null){
                System.out.println("Current: " + currentBest.getFitness() + " Now: " + NaturalSelection.best.getFitness());

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

        //Change la map
		//map.createRandomMap();

    }

    /**
     * Sauvegarde dans un fichier le meilleur neuralNetwork
     */
    private void saveBest(){
        Espece espece = NaturalSelection.best;

        if(espece != null) {
            try {
                FileOutputStream fos = new FileOutputStream("best_nn.dat");
                ObjectOutputStream oos = new ObjectOutputStream(fos);

                oos.writeObject(espece.neuralNetwork);

            } catch (IOException e) {
                e.printStackTrace();
            }
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

	@Override
	public void keyPressed(KeyEvent e) {
	    //Next generation
	    if(e.getKeyCode() == KeyEvent.VK_Q){
            nextGeneration();
        }
		if(e.getKeyCode() == KeyEvent.VK_D) {
			D = true;
		}
		if(e.getKeyCode() == KeyEvent.VK_A) {
			G = true;
		}
		if(e.getKeyCode() == KeyEvent.VK_R) {
			REALTIMEMODE = !REALTIMEMODE;
		}
		
		if(e.getKeyCode() == KeyEvent.VK_SPACE) {
			pausing=!pausing;
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

	public ArrayList<Espece> getEspeces() {
		ArrayList<Espece> temp = new ArrayList<Espece>();
		temp.addAll(especesClosed);
		temp.addAll(especesOpen);
		
		return temp;
	}

}
