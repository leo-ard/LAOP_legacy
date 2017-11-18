package map.obstacle;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.Vector;

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
		
		double ex = e.x-ew/2;
		double ey = e.y-eh/2;
		
		
		if(ex < x + w && ex + ew > this.x && ey < y + h && ey + eh > this.y) {
			return true;
		}
		return false;
	}

	@Override
	public boolean collision(Espece e) {
		
		/*
		 *   0     1
		 * 
		 *   2     3
		 */
		
		
		
		double angle = e.tetha;
		double cos = Math.cos(angle);
		double sin = Math.sin(angle);
		
		Point2D.Double[] points = new Point2D.Double[4];
		
		double tx = -e.w/2;
		double ty = -e.h/2;
		points[0] = new Point2D.Double(e.x + tx * cos - ty * sin,  e.y + tx * sin + ty * cos);
		
		tx = e.w/2;
		ty = -e.h/2;
		points[1] = new Point2D.Double(e.x + tx * cos - ty * sin,  e.y + tx * sin + ty * cos);
		
		tx = -e.w/2;
		ty = e.h/2;
		points[2] = new Point2D.Double(e.x + tx * cos - ty * sin,  e.y + tx * sin + ty * cos);
		
		tx = e.w/2;
		ty = e.h/2;
		points[3] = new Point2D.Double(e.x + tx * cos - ty * sin,  e.y + tx * sin + ty * cos);
		
		for(Point2D p : points) {
			if(p.getX() < this.x + this.w && p.getX() > this.x && p.getY() < this.y + this.h && p.getY() > this.y) {
				return true;
			}
		}
		
		
		return false;
	}
	
	/**
	 * 
	 * @author Léonard Oest O'Leary
	 */
	@Override
	public double getCapteurValue(Capteur c) {
		//System.out.println(c.getX1()+" "+ c.getY1()+" "+ c.getX1()+c.getXRealtive()+" "+ c.getY1()+c.getYRealtive());
		double xRel = c.getXRealtive();
		double yRel = c.getYRealtive();
		
		Line2D.Double line = new Line2D.Double(c.getX1(), c.getY1(), c.getX1()+xRel, c.getY1()+yRel);
		
		//message to me awake : replace sysout by a variable that push the smalest value value from the capteur 
		//and be proud, u didz it 
		//alone, kinda
		
		double sx = (x-line.getX1())/xRel;
		double y3 = line.getY1() + (sx*yRel);
		if(y3 <=y+h && y3 >= y && sx <= 1 && sx >=0) {
			System.out.println("x " + sx);
		}
		
		double sxw = (x+w-line.getX1())/xRel;
		double y4 = line.getY1() + (sxw*yRel);
		if(y4 <=y+h && y4 >= y && sxw <= 1 && sxw >=0) {
			System.out.println("xw"+sxw);
		}
		
		double sy = (y-line.getY1())/yRel;
		double x3 = line.getX1() + (sy*xRel);
		if(x3 <=x+w && x3 >= x && sy <= 1 && sy >=0) {
			System.out.println("y "+sy);
		}
		
		double syh = (y+h-line.getY1())/yRel;
		double x4 = line.getX1() + (syh*xRel);
		if(x4 <=x+w && x4 >= x && syh <= 1 && syh >=0) {
			System.out.println("yh"+syh);
		}
		
		return 0;
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
		//System.out.println(this.x+" "+ this.y+" "+ this.w+" "+ this.h);
		return new Rectangle(this.x, this.y, this.w, this.h);
	}

	@Override
	public void draw(Graphics g) {
		g.fillRect(x, y, w, h);
		
	}

	
	

	
	
}
