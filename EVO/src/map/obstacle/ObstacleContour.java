package map.obstacle;

import java.awt.Graphics;
import java.awt.geom.Point2D;

import espece.Espece;
import espece.capteur.Capteur;

public class ObstacleContour extends Obstacle{
	
	public int w, h;

	public ObstacleContour(int x, int y, int[] infos) {
		super(x, y, null);
		w = infos[0];
		h = infos[1];
	}
	
	public ObstacleContour(int x, int y, int w, int h) {
		super(x, y, null);
		this.w = w;
		this.h = h;
	}
	
	

	@Override
	public boolean collision(Espece e) {
		Point2D.Double[] points = this.getCoins(e);
		
		for(Point2D.Double p : points) {
			if(p.x < x || p.y < this.y || p.x > this.x + this.w || p.y > this.y + this.h) 
				return true;
		}
		
		return false;
	}

	@Override
	public boolean fastCollision(Espece e) {
		return true;
	}

	@Override
	public void draw(Graphics g) {}

	@Override
	public double getCapteurValue(Capteur c) {
		return this.getCapteurValueForRect(c, w, h);
	}
	
	

}
