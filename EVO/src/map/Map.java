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
	
	public Map(String s, Simulation sim) {
		this.obstacles = new ArrayList<Obstacle>();
		
		this.simulation = sim;
		this.w = 11000;
		this.h = 2000;
		
		this.obstacles.add(new ObstcaleRect(2000, 0, 400,1700));
		
		this.obstacles.add(new ObstcaleRect(4000, 300, 400,1700));
		
		/*int x = 1000;
		this.obstacles.add(new ObstcaleRect(4000+x, 100, 200, 200));
		this.obstacles.add(new ObstcaleRect(4000+x, 500, 200, 200));
		this.obstacles.add(new ObstcaleRect(4000+x, 900, 200, 200));
		this.obstacles.add(new ObstcaleRect(4000+x, 1300, 200, 200));
		this.obstacles.add(new ObstcaleRect(4000+x, 1700, 200, 200));
		
		x = 1600;
		this.obstacles.add(new ObstcaleRect(4000+x, 100, 200, 200));
		this.obstacles.add(new ObstcaleRect(4000+x, 500, 200, 200));
		this.obstacles.add(new ObstcaleRect(4000+x, 900, 200, 200));
		this.obstacles.add(new ObstcaleRect(4000+x, 1300, 200, 200));
		this.obstacles.add(new ObstcaleRect(4000+x, 1700, 200, 200));
		
		x = 2200;
		this.obstacles.add(new ObstcaleRect(4000+x, 100, 200, 200));
		this.obstacles.add(new ObstcaleRect(4000+x, 500, 200, 200));
		this.obstacles.add(new ObstcaleRect(4000+x, 900, 200, 200));
		this.obstacles.add(new ObstcaleRect(4000+x, 1300, 200, 200));
		this.obstacles.add(new ObstcaleRect(4000+x, 1700, 200, 200));
		
		x = 2800;
		this.obstacles.add(new ObstcaleRect(4000+x, 100, 200, 200));
		this.obstacles.add(new ObstcaleRect(4000+x, 500, 200, 200));
		this.obstacles.add(new ObstcaleRect(4000+x, 900, 200, 600));
		this.obstacles.add(new ObstcaleRect(4000+x, 1300, 200, 200));
		this.obstacles.add(new ObstcaleRect(4000+x, 1700, 200, 200));
		
		x = 3400;
		this.obstacles.add(new ObstcaleRect(4000+x, 100, 200, 200));
		this.obstacles.add(new ObstcaleRect(4000+x, 500, 600, 200));
		this.obstacles.add(new ObstcaleRect(4000+x, 900, 200, 200));
		this.obstacles.add(new ObstcaleRect(4000+x, 1300, 200, 200));
		this.obstacles.add(new ObstcaleRect(4000+x, 1700, 200, 200));/**/
		
		
		this.obstacles.add(new ObstcaleRect(6000, 0, 400,1700));
		
		this.obstacles.add(new ObstcaleRect(8000, 0, 400, 900));
		this.obstacles.add(new ObstcaleRect(8000, 1100, 400, 900));
		

		
		
		this.obstacles.add(new ObstacleContour(0, 0, w, h));
		this.obstacles.add(new ObstacleArrivee(10000, 0, 1000, h));
		
		this.depart = new Point(200,1000);
		this.destination = new Point(7600, 1600);
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
		e.setFitness(scale * (x+5000*(CONSTANTS.TIME_LIMIT-simulation.time)/ CONSTANTS.TIME_LIMIT));
	}
}
