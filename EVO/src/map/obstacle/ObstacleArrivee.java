package map.obstacle;

import java.awt.Color;
import java.awt.Graphics;

import espece.capteur.Capteur;

public class ObstacleArrivee extends ObstcaleRect{

	public ObstacleArrivee(int x, int y, int w, int h) {
		super(x, y, w, h);
	}
	public double getCapteurValue(Capteur c) {
		return 1;
		
	}
	
	public void draw(Graphics g) {
		g.setColor(new Color(255,255,102));
		super.draw(g);
	}

}
