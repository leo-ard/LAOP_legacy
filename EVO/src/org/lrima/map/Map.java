package org.lrima.map;

import java.awt.Point;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Iterator;

import org.lrima.core.UserPrefs;
import org.lrima.espece.Espece;
import org.lrima.map.Studio.Drawables.Line;
import org.lrima.map.Studio.Drawables.Obstacle;
import org.lrima.simulation.Simulation;

public class Map {
	
	public ArrayList<Obstacle> obstacles;
	public Point destination = null;
	public Point depart = null;
	public Simulation simulation;
	
	public int w, h;
	public int orientation;

	//int[][] mapping;

	int[][] mapping = {
	        {0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 1, 1, 0, 1, 1, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, -2},
            {0, 1, 1, 1, 1, 1, 0, 1, 0, 1, 0, 1, 1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 1, 1, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 1, 0, 1, 0, 0, 0, 1, 0, 1, 0, 1, 1, 0, 0, 0, 0, 1, 0, 0, 1, 1, 0, 1, 0, 1, 0, 0, 0, 1, 0, 1, 0, 1, 0, 1, -2},
            {0, 1, 0, 0, 0, 1, 1, 1, 0, 1, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 1, 0, 0, 0, 1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 1, 0, 0, 1, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 1, 0, 1, 1, 0, 1, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, -2},
            {0, 1, 0, 9, 0, 0, 0, 0, 0, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 1, 1, 0, 0, 1, 0, 1, 0, 1, 0, 0, 0, 0, 1, 1, 0, 1, 0, 0, 1, 0, 1, 1, 0, 1, 0, 0, 0, 1, 0, 1, 0, 1, 0, 1, -2},
            {0, 1, 0, 0, 0, 1, 1, 1, 1, 1, 1, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 1, 0, 1, 0, 0, 1, 0, 1, 1, 1, 0, 1, 0, 1, 0, 0, 1, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 1, 0, 1, 0, 1, 0, 1, 0, 0, 0, 1, 0, 1, 0, 1, 1, 0, 0, 1, 0, 0, 0, 1, 1, 0, 0, 1, 1, 1, 1, 0, -2},
            {0, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 0, 1, 1, 0, 1, 0, 1, 0, 1, 0, 0, 1, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 1, 1, 1, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 1, 1, 1, 0, 1, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 1, 0, 0, 1, 0, 1, 0, 0, 0, 1, 0, 1, 0, 1, 0, 0, -2},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 1, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 1, 0, 1, 1, 0, 0, 0, 0, 1, 0, 1, 0, 1, -2}

	};


	/*int[][] mapping = {
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 1, 1, 1, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {0, 0, 0, 1, 0, 1, 0, 1, 0, 0, 0, 1, 1, 1, 1, 1, 1},
            {0, 9, 0, 0, 0, 1, 0, 1, 0, 1, 0, 0, 0, 0, 0, 1, 1},
            {0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 4, 4, 4, 4, 4, 1, 1}
    };*/

    /*int[][] mapping = {
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {1, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0},
            {1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0},
            {1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0},
            {1, 0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1},
            {1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0},
            {1, 0, 1, 0, 1, 0, 1, 1, 1, 1, 1, 1, 0, 1, 0, 1, 0},
            {1, 0, 1, 0, 1, 0, 1, 0, 0, 0, 1, 1, 0, 1, 0, 1, 0},
            {1, 0, 1, 0, 0, 0, 1, 0, 9, 0, 0, 1, 0, 1, 0, 1, 0},
            {1, 0, 1, 1, 1, 1, 1, 0, 0, 0, 1, 1, 0, 0, 0, 1, 0},
            {1, 1, 1, 0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0},
            {1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0},
            {1, 1, 1, 1, 0, 0, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1},
            {0, 0, 0, 1, 0, 1, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0},
            {0, 9, 0, 0, 0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0},
            {0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0}
    };*/

	int blocSize = 200;

	
	public Map(String s, Simulation sim) {
		
		this.simulation = sim;
        orientation = 0;
		//createRandomMap();
        reloadMap();

		this.orientation = 0;
	}

	public void createRandomMap(){
        int width = org.lrima.utils.Random.getRandomIntegerValue(30, 50);
        int height = org.lrima.utils.Random.getRandomIntegerValue(30, 50);

        mapping = new int[height][width];

        int blocChance = 30;

        for(int i = 0 ; i < height ; i++){
            for(int j = 0 ; j < width ; j++){
                if(i == 1 && j == 1){
                    mapping[i][j] = 9; //DEPART
                    this.depart = new Point(j * blocSize, i * blocSize);
                }
                else if(i <= 2 && j <= 2){
                    mapping[i][j] = 0; //Rien autour du spawn
                }
                else{
                    mapping[i][j] = 0;

                    int ran = org.lrima.utils.Random.getRandomIntegerValue(100);
                    if(ran < blocChance){
                        mapping[i][j] = 1;
                    }
                }
            }
        }
        //Ajoute la fin aleatoirement
        int endX = org.lrima.utils.Random.getRandomIntegerValue(10, width - 1);
        int endY = org.lrima.utils.Random.getRandomIntegerValue(10, height -1 );

        destination = new Point(endX, endY);
        mapping[endY][endX] = -2; //Fin


        if(!mapSolvable()){
            createRandomMap();
        }
        else{
            reloadMap();
        }
    }

    private boolean mapSolvable(){
        return true;
    }

    /**
     * Reload la org.lrima.map, apres l'avoir generer
     */
    private void reloadMap(){
        this.obstacles = new ArrayList<Obstacle>();

        try {
            FileInputStream fis = new FileInputStream("test.map");
            ObjectInputStream ois = new ObjectInputStream(fis);
            this.obstacles = (ArrayList<Obstacle>) ois.readObject();
        }catch (Exception e){
            e.printStackTrace();
        }

        //depart = new Point(500, 500);

        this.w = 10000;
        this.h = 10000;

        //Trouve le depart
        Iterator<Obstacle> iterator = obstacles.iterator();
        while (iterator.hasNext()){
            Obstacle obstacle = iterator.next();
            if(obstacle.type.equals(Obstacle.TYPE_START)){
                this.depart = obstacle.getPosition();
            }
        }

        /*for(int i = 0 ; i < mapping.length ; i++){
            for(int j = 0 ; j < mapping[0].length ; j++){
                switch(mapping[i][j]){
                    case 1:
                        this.obstacles.add(new ObstcaleRect(j * blocSize, i * blocSize, blocSize, blocSize));
                        break;
                    case 2:
                        this.obstacles.add(new ObstcaleRect(j * blocSize, (i * blocSize) + blocSize / 2, blocSize, blocSize / 2));
                        break;
                    case 3:
                        this.obstacles.add(new ObstcaleRect(j * blocSize, ((i + 1) * blocSize) - (int)(blocSize / 1.5), blocSize, (int)(blocSize / 1.5)));
                        break;
                    case 4:
                        this.obstacles.add(new ObstcaleRect(j * blocSize, ((i + 1) * blocSize) - (int)(blocSize / 1.1), blocSize, (int)(blocSize / 1.1)));
                        break;
                    case 9:
                        this.depart = new Point(j * blocSize,i * blocSize);
                        break;
                    case -2:
                        this.obstacles.add(new ObstacleArrivee(j * blocSize, i * blocSize, blocSize, blocSize));
                        break;
                }
            }
        }

        this.obstacles.add(new ObstacleContour(0, 0, w, h));*/
    }
	
	public void update(ArrayList<Espece> especes) {
        //TODO
		/*for(Espece e : especes) {
			for(Obstacle o : obstacles) {
				if(o.collision(e)) {
					e.kill();
				}
			}
		}*/
	}

	public void setFitnessToEspece(Espece e) {
		double x = e.getX();
		double y = e.getY();
		double scale = 0.001;

        double averageSpeed = e.totalSpeed / simulation.time;
        int timeLimit = UserPrefs.TIME_LIMIT;

        double fitness = ((1 * ((simulation.time - 0.00001) / timeLimit)) *( scale * e.maxDistanceFromStart)) ;// * averageSpeed);

        //double fitness = (scale * e.maxDistanceFromStart);

        //if(e.isDead()){
        //   fitness = 0.0;
        //}

        //Quand il se rend a destination
        if(fitness == Double.POSITIVE_INFINITY){
            fitness = 9999999;
		}

		if(e.maxDistanceFromStart < 800){
		    fitness = 0.0;
        }

		e.setFitness(fitness);
	}
}
