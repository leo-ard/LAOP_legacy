package map;

import java.awt.Point;
import java.util.ArrayList;

import core.CONSTANTS;
import espece.Espece;
import map.obstacle.Obstacle;
import map.obstacle.ObstacleArrivee;
import map.obstacle.ObstacleContour;
import map.obstacle.ObstcaleRect;
import simulation.Simulation;

public class Map {
	
	public ArrayList<Obstacle> obstacles;
	public Point destination;
	public Point depart;
	public Simulation simulation;
	
	public int w, h;
	public int orientation;

	int[][] mapping = {
	        {0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, -2},
            {0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 0, 0, 0, 1, 0, 0, -2},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 1, -2},
            {0, 0, 0, 0, 1, 1, 1, 0, 0, 1, 0, 0, 0, 1, 0, 0, -2},
            {0, 9, 0, 0, 1, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 1, -2},
            {0, 0, 0, 0, 1, 1, 1, 0, 0, 1, 0, 0, 0, 1, 0, 0, -2},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 1, -2},
            {0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 0, 0, 0, 1, 0, 0, -2},
            {0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, -2}

	};
	int blocSize = 200;

	
	public Map(String s, Simulation sim) {
		this.obstacles = new ArrayList<Obstacle>();
		
		this.simulation = sim;
		this.w = mapping[0].length * blocSize;//11000;
		this.h = mapping.length * blocSize;//2000;

        for(int i = 0 ; i < mapping.length ; i++){
            for(int j = 0 ; j < mapping[0].length ; j++){
                switch(mapping[i][j]){
                    case 1:
                        this.obstacles.add(new ObstcaleRect(j * blocSize, i * blocSize, blocSize, blocSize));
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

        //Setup la destination sur toute la derniere colonne
        //this.destination = new Point(mapping[0].length * blocSize, mapping.length * blocSize);

		this.obstacles.add(new ObstacleContour(0, 0, w, h));
		this.orientation = 0;
	}
	
	public void update(ArrayList<Espece> especes) {
		for(Espece e : especes) {
			for(Obstacle o : obstacles) {
				if(o.collision(e)) {
					e.kill();
				}
			}
		}
	}

	public void setFitnessToEspece(Espece e) {
		double x = e.getX();
		double scale = 0.01;

		e.setFitness(scale * ((x * 10)+5000*(CONSTANTS.TIME_LIMIT-simulation.time)/ CONSTANTS.TIME_LIMIT));
	}
}
