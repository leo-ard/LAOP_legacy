package map;

import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

import espece.Espece;
import map.obstacle.Obstacle;
import map.obstacle.ObstcaleRect;
import simulation.Simulation;

public class Map {
	
	public ArrayList<Obstacle> obstacles;
	public Point destination;
	public Point depart;
	public Simulation simulation;
	
	public int mx, my;
	
	public Map(String s, Simulation sim) {
		this.obstacles = new ArrayList<Obstacle>();
		
		this.simulation = sim;
		this.mx = 2500;
		this.my = 2500;
		
		this.obstacles.add(new ObstcaleRect(100, 100, 300,100));
		this.obstacles.add(new ObstcaleRect(400, 100, 100,300));
		
		this.depart = new Point(100,400);
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
