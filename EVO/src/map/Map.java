package map;

import java.awt.Point;
import java.util.ArrayList;

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
	
	public int mx, my;
	public int orientation;
	
	public Map(String s, Simulation sim) {
		this.obstacles = new ArrayList<Obstacle>();
		
		this.simulation = sim;
		this.mx = 8800;
		this.my = 2000;
		
		this.obstacles.add(new ObstcaleRect(1800, 0, 400,1100));
		this.obstacles.add(new ObstcaleRect(3800, 900, 400,1100));
		this.obstacles.add(new ObstcaleRect(5800, 0, 400,1100));
		this.obstacles.add(new ObstacleContour(0, 0, mx, my));
		this.obstacles.add(new ObstacleArrivee(8000, 0, 800, my));
		
		this.depart = new Point(400,400);
		this.destination = new Point(7600, 1600);
		this.orientation = 45;
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
	
	

}
