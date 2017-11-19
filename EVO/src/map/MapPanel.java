package map;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import core.CONSTANTS;
import espece.Espece;
import map.obstacle.Obstacle;

public class MapPanel extends JPanel implements MouseMotionListener, MouseListener, MouseWheelListener{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
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
	
	//images;
	public static Image IMG_VOITURE;
	
	public MapPanel(Map map, int w, int h) {
		this.setPreferredSize(new Dimension(w, h));
		this.w = w;
		this.h = h;
		this.map = map;
		
		
		this.zoom = 0.40f;
		this.viewX =(int) ((-map.depart.x+ w/2)*zoom);
		this.viewY =(int) ((-map.depart.y+ h/2)*zoom);
		
		this.loadImages();
		this.addMouseMotionListener(this);
		this.addMouseWheelListener(this);
		this.addMouseListener(this);
		
		//this.mouseWheelMoved(new MouseWheelEvent(this, 0, 0, 0, 0, 0, 0, true, 0, 0, 0));
		
		Timer uploadCheckerTimer = new Timer(true);
		uploadCheckerTimer.scheduleAtFixedRate(
		    new TimerTask() {
		      public void run() { repaint(); }
		    }, 0, 16);
	}
	
	public void loadImages() {
		try {
			BufferedImage temp  = ImageIO.read(new File(CONSTANTS.SRC_VOITURE));
			IMG_VOITURE = temp.getScaledInstance(Espece.ESPECES_WIDTH, Espece.ESPECES_HEIGHT, Image.SCALE_DEFAULT);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void paintComponent(Graphics g1d) {
		Graphics2D g = (Graphics2D) g1d;
		super.paintComponent(g);
		
		g.scale(zoom, zoom);
		g.translate((viewX+offX)*(1.0/(double)zoom), (viewY+offY)*(1.0/(double)zoom));
		
		//background
		g.setColor(new Color(255, 178, 102));
		g.fillRect(0, 0, map.mx, map.my);
		
		
		//ellipse size
		int s = 100;
		g.setColor(Color.green);
		g.fillOval(map.depart.x-s/2, map.depart.y-s/2, s, s);
		
		g.setColor(Color.YELLOW);
		//g.fillOval(map.destination.x-s/2, map.destination.y-s/2, s, s);
		
		
		
		//draws map obstacle
		g.setColor(new Color(255, 51, 51));
		for(Obstacle o : map.obstacles) {
			o.draw(g);
		}
		
		//contour
		g.setColor(Color.black);
		g.drawRect(0, 0, map.mx, map.my);
		
		g.setColor(Color.blue);
		for(Espece e : map.simulation.especes) {
			e.draw(g);
		}
		
		
		
		
		g.setTransform(new AffineTransform());
		g.setColor(Color.black);
		g.drawString("Version alpha 1.1 - Laboratoire de recherche LRIMA", 10, 20);
		g.drawString(""+map.simulation.time/1000, 10,40);
		
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
