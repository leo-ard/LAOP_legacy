package org.lrima.map.obstacle;

import java.awt.Graphics;

import org.lrima.espece.Espece;
import org.lrima.espece.capteur.Capteur;

public interface IObstacle {

	public boolean collision(Espece e);
	
	public boolean fastCollision(Espece e);
	
	
	public void draw(Graphics g);
	
	public double getCapteurValue(Capteur c) ;
	
	
}
