package org.lrima.simulation;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;

import org.lrima.core.UserPrefs;
import org.lrima.espece.Espece;
import org.lrima.espece.capteur.Capteur;
import org.lrima.espece.network.NetworkStructure;
import org.lrima.espece.network.NeuralNetwork;
import org.lrima.map.Map;
import org.lrima.map.MapPanel;
import org.lrima.map.Studio.Drawables.Line;
import org.lrima.map.Studio.Drawables.Obstacle;
import org.lrima.simulation.selection.NaturalSelection;

public class Simulation extends Thread implements KeyListener{
	
	private Map map;
	private MapPanel mapPanel;

	int generation = 0;
	
	public ArrayList<Espece> especesOpen;
	public ArrayList<Espece> especesClosed;
	
	//FrameManager frame;
	
	public boolean running = true;
	public boolean pausing = false;

    public boolean shouldGetNewMap = false;
    public boolean shouldResetAndAddEspece = false;

    public NeuralNetwork neuralNetworkToUse = null;

    //temporaire
	boolean D, G;
	
	double dt;
	public double time;

	public Simulation() {
		super();
		//init
		map = new Map("TODO", this);
		this.resetAndAddEspeces(null);
		
		running = true;
		
		time = 0;

	}
	
	public void run() {
		dt = 8;
		long currTime = System.currentTimeMillis();
		long timePassed;
		while(running) {
			if(!pausing) {
				if(especesOpen.size() != 0 && time < UserPrefs.TIME_LIMIT) {
					currTime = System.currentTimeMillis();
					boolean d = D, g = G;
					for(Espece e : especesOpen) {
					    e.update(dt, d ? 1 : 0, g ? 1 : 0);
					}

					time += dt;
					Iterator<Espece> iterator = especesOpen.iterator();

					while (((Iterator) iterator).hasNext()){
						Espece e = iterator.next();
						e.resetCapteur();
						for(Capteur c : e.getCapteursList()){
						    double minCapteurValue = 100.0; // Pour avoir le capteur le plus proche possible
						    for(Obstacle o : map.obstacles){
						        if(o.type.equals(Obstacle.TYPE_LINE)){
						            Line line = (Line) o;
									double capteurValue = line.getCapteurValue(c);
						            if(capteurValue < minCapteurValue && capteurValue != -1.0) {
						            	//Si il y a eu une collision
										c.setValue(capteurValue);
										c.lastObstacleCollided = o;
										minCapteurValue = capteurValue;
									}
                                }

                            }
                        }

                        //Check si l'auto meurt
						boolean died = false;
                        for(Obstacle o : map.obstacles) {
                        	if(!died) {
								//Si l'espece touche un mur
								if (o.collisionWithRect(e)) {
									try {
										especesClosed.add(e);
										//e.update(dt,0,0);
										e.kill();
										map.setFitnessToEspece(e);

										iterator.remove();
										died = true;
									} catch (Exception e1) {
										e1.printStackTrace();
										running = false;
									}
								}
							}
						}
					}
                    //Quand le user clique sur le bouton new map dans le menu
                    if(shouldGetNewMap){
                        //TODO: map.createRandomMap();
                        shouldGetNewMap = false;
                    }
                    if(shouldResetAndAddEspece){
                        if(neuralNetworkToUse != null){
                            resetAndAddEspeces(neuralNetworkToUse);
                        }
                        else{
                            resetAndAddEspeces(neuralNetworkToUse);
                        }
                        shouldResetAndAddEspece = false;
                    }

				}
				else {
					nextGeneration();
					//resetEspeceImage();
				}
			}

			if(UserPrefs.REAL_TIME) {
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

	public void changeNeuralNetwork(NeuralNetwork nn){
	    generation = 0;
        neuralNetworkToUse = nn;
        shouldResetAndAddEspece = true;
    }

	public void nextGeneration(){
	    //Reload les préférences du user
	    UserPrefs.load();

		this.generation++;

        for(int i = 0; i < especesOpen.size(); i++) {
            Espece espece = especesOpen.get(i);

			map.setFitnessToEspece(espece);
			espece.maxDistanceFromStart = 0.0;

            espece.kill();
            especesClosed.add(espece);
            especesOpen.remove(i);
        }

        this.mutate();

        time = 0;

        //Change la org.lrima.map a chaque 10 generations
        if(UserPrefs.RANDOM_MAP) {
            if (this.generation % 10 == 0) {
                //TODO: map.createRandomMap();
            }
        }

    }

	public void resetAndAddEspeces(NeuralNetwork neuralNetworkToUse) {
        especesOpen = new ArrayList<>();
		especesClosed = new ArrayList<>();

		int numberOfCar = UserPrefs.preferences.getInt(UserPrefs.KEY_NUMBER_OF_CAR, UserPrefs.DEFAULT_NUMBER_OF_CAR);

		while(especesOpen.size() < numberOfCar) {
		    if(neuralNetworkToUse == null) {
                especesOpen.add(new Espece(this.map));
            }
            else{
		        //Load le neuralNetwork qui a été demmandé d'être loadé
		        especesOpen.add(new Espece(this.map, neuralNetworkToUse));
            }
		}
		
	}
	
	public void mutate() {
		NaturalSelection selection = new NaturalSelection(especesClosed);
		this.especesOpen = selection.getMutatedList(map);
		this.especesClosed = new ArrayList<Espece>();
	}

	/**
	 * Trouve le meilleur fitness dans toutes les voitures
	 * @return le fitness de la meilleure voiture
	 */
	public double getBestFitness(){
		return getBest().getFitness();
	}

	public Espece getBest(){
		ArrayList<Espece> sortedEspece = new ArrayList<>(especesOpen);
		sortedEspece.addAll(especesClosed);
		sortedEspece.sort(new Comparator<Espece>() {
			@Override
			public int compare(Espece e1, Espece e2) {
				if(e1.getFitness() == e2.getFitness())
					return 0;
				return e1.getFitness()>e2.getFitness()?-1:1;
			}
		});

		return sortedEspece.get(0);
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
		
		
	}
	@Override
	public void keyReleased(KeyEvent e) {
		
	}

	@Override
	public void keyTyped(KeyEvent e) {}

	public ArrayList<Espece> getEspeces() {
		ArrayList<Espece> temp = new ArrayList<>();
		temp.addAll(especesClosed);
		temp.addAll(especesOpen);
		
		return temp;
	}

    public Map getMap() {
        return map;
    }

    public MapPanel getMapPanel() {
        return mapPanel;
    }
}
