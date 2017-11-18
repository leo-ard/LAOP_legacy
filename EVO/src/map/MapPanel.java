package map;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JLabel;
import javax.swing.JPanel;

import espece.Espece;
import map.obstacle.Obstacle;

public class MapPanel extends JPanel implements MouseMotionListener, MouseListener, MouseWheelListener{
	
	final int ZOOM_MINIMUM = 0;
	final int ZOOM_MAXIMUM = 100;
	
	Map map;
	
	//Width and height of the JPanel
	int w, h;
	
	//Width and height of the map
	int mw, mh;
	
	//View range
	int viewX, viewY;
	int offX, offY;
	float zoom;
	int scale = 1;
	
	
	//drag n drop
	int mx, my;
	boolean dragging;
	
	//temp
	boolean D, G;
	
	public MapPanel(Map map, int w, int h) {
		this.setPreferredSize(new Dimension(w, h));
		this.w = w;
		this.h = h;
		this.map = map;
		
		this.viewX =0;
		this.viewY = 0;
		this.zoom = 1;
		this.addMouseMotionListener(this);
		this.addMouseWheelListener(this);
		this.addMouseListener(this);
		
		Timer uploadCheckerTimer = new Timer(true);
		uploadCheckerTimer.scheduleAtFixedRate(
		    new TimerTask() {
		      public void run() { repaint(); }
		    }, 0, 16);
	}
	
	@Override
	public void paintComponent(Graphics g1d) {
		Graphics2D g = (Graphics2D) g1d;
		super.paintComponent(g);
		
		g.scale(zoom, zoom);
		g.translate((viewX+offX)*(1.0/(double)zoom), (viewY+offY)*(1.0/(double)zoom));
		
		//background
		g.setColor(new Color(255, 200, 120));
		g.fillRect(0, 0, map.mx, map.my);
		g.setColor(Color.black);
		g.drawRect(0, 0, map.mx, map.my);
		
		//draws map obstacle
		g.setColor(new Color(255, 50, 50));
		for(Obstacle o : map.obstacles) {
			o.draw(g);
		}
		
		g.setColor(Color.blue);
		for(Espece e : map.simulation.especes) {
			e.draw(g);
		}
		
		//g.rotate(0);
		
		try {
			//System.out.println(this.getMousePosition());
			//System.out.println((viewX+offX)*(1.0/(double)zoom) - this.getMousePosition().getX()*(1.0/(double)zoom));
		}catch(Exception e) {
			
		}
		
		
		g.translate(0, 0);
	}
	
	public int addZoom(int x) {
		return (int)(x *zoom);
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		//requestFocus();
	}

	@Override
	public void mousePressed(MouseEvent e) {
		mx = e.getX()-viewX;
		my = e.getY()-viewY;
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		viewX = (int) (e.getX()-mx);
		viewY = (int) (e.getY()-my);
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		double delta = e.getWheelRotation()/10.0;
		double zoomOld = zoom;
		zoom /= Math.exp(delta);	
		
		double mouseXOnMap = (viewX+offX)*(1.0/(double)zoomOld) - e.getX()*(1.0/(double)zoomOld);
		double mouseYOnMap = (viewY+offY)*(1.0/(double)zoomOld) - e.getY()*(1.0/(double)zoomOld);
		
		offX += (int) (-mouseXOnMap  * -(zoom-zoomOld));
		offY += (int) (-mouseYOnMap * -(zoom-zoomOld));
		
		//repaint();	
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		viewX = (int) (e.getX()-mx);
		viewY = (int) (e.getY()-my);
		//repaint();
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {}
	@Override
	public void mouseEntered(MouseEvent arg0) {}
	@Override
	public void mouseExited(MouseEvent arg0) {}
}
