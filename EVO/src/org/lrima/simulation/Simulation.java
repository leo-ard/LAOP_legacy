package org.lrima.simulation;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import org.lrima.core.UserPrefs;
import org.lrima.espece.Espece;
import org.lrima.espece.capteur.Capteur;
import org.lrima.espece.network.NetworkStructure;
import org.lrima.espece.network.NeuralNetwork;
import org.lrima.map.Map;
import org.lrima.map.MapPanel;
import org.lrima.map.obstacle.Obstacle;
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
										running = false;
									}
								}
							}
						}
					}
                    //Quand le user clique sur le bouton new map dans le menu
                    if(shouldGetNewMap){
                        map.createRandomMap();
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

            espece.kill();
            especesClosed.add(espece);
            especesOpen.remove(i);
        }

        this.mutate();

        time = 0;

        //Change la org.lrima.map a chaque 10 generations
        if(UserPrefs.RANDOM_MAP) {
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
	
	public void resetAndAddEspeces(NeuralNetwork neuralNetworkToUse) {
        especesOpen = new ArrayList<>();
		especesClosed = new ArrayList<>();

		int numberOfCar = UserPrefs.preferences.getInt(UserPrefs.KEY_NUMBER_OF_CAR, UserPrefs.DEFAULT_NUMBER_OF_CAR);

		while(especesOpen.size() < numberOfCar) {
		    if(neuralNetworkToUse == null) {
                especesOpen.add(new Espece(map.depart, map.orientation, this.map));
            }
            else{
		        //Load le neuralNetwork qui a été demmandé d'être loadé
		        especesOpen.add(new Espece(map.depart, map.orientation, this.map, neuralNetworkToUse));
            }
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
}
