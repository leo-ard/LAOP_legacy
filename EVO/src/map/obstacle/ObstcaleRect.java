package map.obstacle;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

import espece.Espece;
import espece.capteur.Capteur;

public class ObstcaleRect extends Obstacle {
	
	int w;
	int h;
	
	
	public ObstcaleRect(int x, int y, int[] infos) {
		super(x, y, null);
		this.w = infos[0];
		this.h = infos[1];
	}

	public ObstcaleRect(int x, int y, int w, int h) {
		super(x, y, null);
		this.w = w;
		this.h = h;
	}
	
	@Override
	public boolean fastCollision(Espece e) {
		int ew =w+h/2 + Capteur.CAPTEUR_LENGHT*2;
		int eh =w+h/2 + Capteur.CAPTEUR_LENGHT*2;
		
		double ex = e.getX()-ew/2;
		double ey = e.getY()-eh/2;
		
		
		if(ex < x + w && ex + ew > this.x && ey < y + h && ey + eh > this.y) {
			return true;
		}
		return false;
	}

	@Override
	public boolean collision(Espece e) {
		Point2D.Double[] points = this.getCoins(e);
		
		for(Point2D p : points) {
			if(p.getX() < this.x + this.w && p.getX() > this.x && p.getY() < this.y + this.h && p.getY() > this.y) {
				return true;
			}
		}
		
		
		return false;
	}
	
	@Override
	public double getCapteurValue(Capteur c) {
		return this.getCapteurValueForRect(c, w, h);
	}
	
	
	
	public double x(Point2D.Double p1, Point2D.Double p2) {
		return p1.getX()*p2.getY()-p1.getY()*p2.getX();
	}
	
	public Point2D.Double substract(Point2D.Double p1, Point2D.Double p2){
		return new Point2D.Double (p1.getX()-p2.getX(), p1.getY() - p2.getY());
	}
	
	public Point2D.Double substract(double x1, double y1, double x2, double y2){
		return new Point2D.Double (x1-x2, y1 - y2);
	}
	
	public Line2D.Double[] toLines() {
		Line2D.Double[] lines = {
				new Line2D.Double(x, y, x+w, y), 
				new Line2D.Double(x, y+h, x+w, y+h),
				new Line2D.Double(x+w, y, x+w, y+h),
				new Line2D.Double(x, y, x, y+h)
		};
		return lines;
	}
	
	public Rectangle toRect() {
		return new Rectangle(this.x, this.y, this.w, this.h);
	}

	@Override
	public void draw(Graphics g) {
		g.fillRect(x, y, w, h);
		g.drawRect(x, y, w, h);
	}

	
	

	
	
}
