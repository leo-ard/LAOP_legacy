package map.obstacle;

import java.awt.Graphics;

import espece.Espece;
import espece.capteur.Capteur;

public interface IObstacle {

	public boolean collision(Espece e);
	
	public boolean fastCollision(Espece e);
	
	
	public void draw(Graphics g);
	
	public double getCapteurValue(Capteur c) ;
	
	
}
