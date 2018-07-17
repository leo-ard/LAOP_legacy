package org.lrima.map;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
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

import org.lrima.core.EVO;
import org.lrima.core.UserPrefs;
import org.lrima.espece.Espece;
import org.lrima.espece.capteur.Capteur;
import org.lrima.map.Studio.Drawables.Obstacle;

public class MapPanel extends JPanel implements MouseMotionListener, MouseListener, MouseWheelListener{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	final int ZOOM_MINIMUM = 0;
	final int ZOOM_MAXIMUM = 100;
	
	private Map map;
	
	//View range
	private int viewX, viewY;
	private int offX, offY;
	private float zoom;
	
	//drag n drop
	private int lastMousePositionX, lastMousePositionY;
	private boolean dragging;
	
	//temp
	boolean D, G;
	
	//images;
	public static Image IMG_VOITURE;
	public static Image IMG_LRIMA;

	public boolean followBest = UserPrefs.FOLLOW_BEST;


	public MapPanel(Map map, int w, int h) {
		this.setPreferredSize(new Dimension(w, h));
		this.map = map;
		
		this.zoom = 1.50f;
		this.viewX =(int) ((-map.depart.x+ w/2)*zoom);
		this.viewY =(int) ((-map.depart.y+ h/2)*zoom);
		
		this.loadImages();
		this.addMouseMotionListener(this);
		this.addMouseWheelListener(this);
		this.addMouseListener(this);

		//this.mouseWheelMoved(new MouseWheelEvent(this, 0, 0, 0, 0, 0, 0, true, 0, 0, 0));
		
	}
	
	public void start() {
		Timer uploadCheckerTimer = new Timer(false);
		uploadCheckerTimer.scheduleAtFixedRate(
		    new TimerTask() {
		      public void run() { repaint(); }
		    }, 0, 16);
	}
	
	public void loadImages() {
		UserPrefs.load();

		try {
			BufferedImage temp  = ImageIO.read(new File(UserPrefs.SRC_VOITURE));
			IMG_VOITURE = temp.getScaledInstance(Espece.ESPECES_WIDTH, Espece.ESPECES_HEIGHT, Image.SCALE_DEFAULT);
			IMG_LRIMA = ImageIO.read(MapPanel.class.getResource(UserPrefs.SRC_LRIMA)).getScaledInstance(130,95, Image.SCALE_DEFAULT);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void paintComponent(Graphics g1d) {
		Graphics2D g = (Graphics2D) g1d;
		super.paintComponent(g);


		if(followBest){
			Espece bestEspece = map.simulation.getBest();

			double anchorx = (getWidth() - (getWidth() * zoom)) / 2;
			double anchory = (getHeight() - (getHeight() * zoom)) / 2;

			g.translate(anchorx, anchory);
			g.scale(zoom, zoom);
			g.translate((-bestEspece.getX() + getWidth() / 2), (-bestEspece.getY() + getHeight() / 2));
		}
		else{
			double translateX = (viewX+offX)*(1.0/(double)zoom);
			double translateY = (viewY+offY)*(1.0/(double)zoom);
			g.scale(zoom, zoom);
			g.translate(translateX, translateY);
		}
		
		//background
		g.setColor(new Color(238, 238, 238));
		g.fillRect(0, 0, map.w, map.h);

		//Cadriller
		g.setColor(new Color(136, 136, 136));
		for(int i = 0 ; i < map.w ; i += map.w / 40){
			g.drawLine(i, 0, i, map.h);
		}
		for(int i = 0 ; i < map.h ; i += map.h / 40){
			g.drawLine(0, i, map.w, i);
		}
		
		
		//ellipse size
		int s = 100;
		g.setColor(Color.green);
		g.fillOval(map.depart.x-s/2, map.depart.y-s/2, s, s);
		
		g.setColor(Color.YELLOW);
		//g.fillOval(org.lrima.map.destination.x-s/2, org.lrima.map.destination.y-s/2, s, s);
		
		
		
		//draws org.lrima.map obstacle

		for(Obstacle o : map.obstacles) {
            g.setColor(new Color(255, 51, 51));
			o.draw(g);
		}
		
		//contour
		g.setColor(Color.black);
		g.drawRect(0, 0, map.w, map.h);
		
		g.setColor(Color.blue);
		for(Espece e : map.simulation.getEspeces()) {
			if(e!= null)
				e.draw(g);
		}
		
		g.setTransform(new AffineTransform());
		g.setColor(Color.black);
		g.drawString("Version alpha 1.1 - Laboratoire de recherche LRIMA", 10, 20);
		g.drawString(""+map.simulation.time/1000, 10,40);
		g.drawImage(IMG_LRIMA,this.getWidth()-150,this.getHeight()-105,null);


		//Draw capteurs lines
		/*for(Espece e : org.lrima.map.org.lrima.simulation.especesOpen){
			for(Capteur c : e.getCapteursList()){
				c.draw(g);
			}
		}*/
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		int ex = (int) ((e.getX()-viewX-offX)*(1.0/(double)zoom));
		int ey = (int) ((e.getY()-viewY-offY)*(1.0/(double)zoom));

		Espece selected = map.simulation.getEspeces().get(0);
		int proche = selected.distanceFrom(new Point(ex,ey));
		for(Espece espece : map.simulation.getEspeces()) {
			//Reset selected
			espece.selected = false;

			if(proche >= espece.distanceFrom(new Point(ex, ey))) {
				proche = espece.distanceFrom(new Point(ex, ey));
				selected = espece;
			}

		}
		selected.selected = true;

		System.out.println("Clic: " + getPointOnMap(e.getPoint()));
		/*System.out.println("Selected position: " + new Point.Double(selected.getX(), selected.getY()));*/


		EVO.frame.changeNetworkFocus(selected);
	}

	public Point.Double getPointOnMap(Point point){
		double mouseXOnMap = -((viewX+offX)*(1.0/(double)zoom) - point.x*(1.0/(double)zoom));
		double mouseYOnMap = -((viewY+offY)*(1.0/(double)zoom) - point.y*(1.0/(double)zoom));

		return new Point.Double(mouseXOnMap, mouseYOnMap);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		lastMousePositionX = e.getX()-viewX;
		lastMousePositionY = e.getY()-viewY;
		
		
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		viewX = (int) (e.getX()-lastMousePositionX);
		viewY = (int) (e.getY()-lastMousePositionY);
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {

			double delta = e.getWheelRotation() / 10.0;
			double zoomOld = zoom;
			zoom /= Math.exp(delta);

			if (zoom <= 0.5) {
				zoom = 0.5f;
			}
			if (zoom >= 2.5) {
				zoom = 2.5f;
			}

			double mouseXOnMap = (viewX + offX) * (1.0 / (double) zoomOld) - e.getX() * (1.0 / (double) zoomOld);
			double mouseYOnMap = (viewY + offY) * (1.0 / (double) zoomOld) - e.getY() * (1.0 / (double) zoomOld);

			offX += (int) (-mouseXOnMap * -(zoom - zoomOld));
			offY += (int) (-mouseYOnMap * -(zoom - zoomOld));

		//repaint();
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		viewX = (int) (e.getX()-lastMousePositionX);
		viewY = (int) (e.getY()-lastMousePositionY);
		//repaint();
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {}
	@Override
	public void mouseEntered(MouseEvent arg0) {}
	@Override
	public void mouseExited(MouseEvent arg0) {}
}
