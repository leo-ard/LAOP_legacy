package map.obstacle;

import java.awt.Graphics;

import espece.Espece;
import espece.capteur.Capteur;

public abstract class Obstacle implements ObstacleInterface{
	
	int x;
	int y;
	
	public Obstacle(int x, int y, int[] infos) {
		this.x = x;
		this.y = y;
	}

	
	
	
	

}
