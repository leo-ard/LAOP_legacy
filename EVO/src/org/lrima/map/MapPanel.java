package org.lrima.map;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import org.lrima.core.EVO;
import org.lrima.core.UserPrefs;
import org.lrima.espece.Espece;
import org.lrima.map.Studio.Drawables.Obstacle;
import org.lrima.simulation.Simulation;

public class MapPanel extends JPanel implements MouseMotionListener, MouseListener, MouseWheelListener{

	//The map to display
	private Map map;

	//View range
	//TODO: Trouver noms plus descriptifs
	private int viewX, viewY;
	private int offX, offY;
	private float zoom;

	//Used for the drag motion on the map
	private int lastMousePositionX, lastMousePositionY;

	//Stores the logo of LRIMA;
	private BufferedImage LRIMA_image;

	//The simulation to use
	private Simulation simulation;

	//Colors used on the map
	private final Color COLOR_BACKGROUND = new Color(238, 238, 238);
	private final Color COLOR_BACKGROUND_LINES = new Color(136, 136, 136);
	private final Color COLOR_OBSTACLE = new Color(176, 176, 176);
	private final Color COLOR_TEXT = new Color(0, 0, 0);

	//Used for the text
	private final int TEXT_MARGIN = 20;
	private final int FONT_SIZE = 32;



	public MapPanel(Simulation simulation) {
		//Setup variables
		this.simulation = simulation;
		this.map = simulation.getMap();
		this.zoom = 0.5f;

		//Try to load te LRIMA logo image
		try {
			URL imagePath = getClass().getResource("/images/LRIMA.png");
			this.LRIMA_image = ImageIO.read(new File(imagePath.getPath()));
		}catch (IOException e){
			System.out.println("Could load LRIMA logo image");
		}

		//TODO: getSize retourne 0 dans le constructeur
		//Centers the view on the start point
		this.viewX =(int) ((-map.getDepart().x+ this.getSize().getWidth()/2)*zoom);
		this.viewY =(int) ((-map.getDepart().y+ this.getSize().getHeight()/2)*zoom);

		//Add all the listeners
		this.addMouseMotionListener(this);
		this.addMouseWheelListener(this);
		this.addMouseListener(this);
	}

	/**
	 * Used to constantly repaint the map
	 */
	public void start() {
		Timer uploadCheckerTimer = new Timer(false);
		uploadCheckerTimer.scheduleAtFixedRate(
				new TimerTask() {
					public void run() { repaint(); }
				}, 0, 16);
	}

	@Override
	public void paintComponent(Graphics g) {
		Graphics2D graphics = (Graphics2D) g;
		super.paintComponent(graphics);

		double translateX, translateY;

		UserPrefs.load();
		if(UserPrefs.FOLLOW_BEST){
			Espece bestEspece = simulation.getBest();
			translateX = (int) -bestEspece.getX() + getWidth() / 2;
			translateY = (int) -bestEspece.getY() + getHeight() / 2;

			//TODO: faire qu'on puisse modifier le zoom
			zoom = 1.0f;

			bestEspece.setSelected(true);
		}
		else{
			translateX = (viewX+offX)*(1.0/(double)zoom);
			translateY = (viewY+offY)*(1.0/(double)zoom);
		}

		//Zoom based on mouse wheel
		graphics.scale(zoom, zoom);
		graphics.translate(translateX, translateY);

		drawBackground(graphics);
		drawObstacles(graphics);
		drawCars(graphics);

		//Reset the zoom and translations
		graphics.setTransform(new AffineTransform());

		//Write the informations
		drawInformation(graphics);

		//Put the LRIMA image on the top left
		graphics.drawImage(this.LRIMA_image,0, 0,null);
	}

	/**
	 * Draws the background of the map and the lines
	 * @param graphics the graphics to put a map into
	 */
	private void drawBackground(Graphics2D graphics){
		//Set the background color
		graphics.setColor(COLOR_BACKGROUND);
		graphics.fillRect(0, 0, map.getMapWidth(), map.getMapHeight());

		//Draw the lines
		graphics.setColor(COLOR_BACKGROUND_LINES);
		for(int i = 0 ; i < map.getMapWidth() ; i += map.getMapWidth() / 40){
			//Vertical lines
			graphics.drawLine(i, 0, i, map.getMapHeight());
		}
		for(int i = 0 ; i < map.getMapHeight() ; i += map.getMapHeight() / 40){
			//Horizontal lines
			graphics.drawLine(0, i, map.getMapWidth(), i);
		}
	}

	/**
	 * Draws all the obstacles
	 * @param graphics the graphics to put the obstacles into
	 */
	private void drawObstacles(Graphics2D graphics){
		for(Obstacle o : map.getObstacles()) {
			graphics.setColor(COLOR_OBSTACLE);
			o.draw(graphics);
		}
	}

	/**
	 * Draw the cars
	 * @param graphics the graphics to put the cars into
	 */
	private void drawCars(Graphics2D graphics){
		graphics.setColor(Color.blue);
		for(Espece e : this.simulation.getAllEspeces()) {
			e.draw(graphics);
		}
	}

	/**
	 * Draw additional information such as the current generation, the time
	 * @param graphics the graphics to put the information into
	 */
	private void drawInformation(Graphics2D graphics){
		graphics.setColor(COLOR_TEXT);
		graphics.setFont(new Font("Helvetica", Font.PLAIN, FONT_SIZE));

		String text = 	"Generation: " + this.simulation.getGeneration();
		text += 		"\nTime: " + Simulation.currentTime / 1000;

		drawText(graphics,text, new Point(TEXT_MARGIN, getHeight() * 2 - TEXT_MARGIN), "up");
	}

	/**
	 * Draws text on the screen. Works with line breaks.
	 * @param graphics the graphic context to put the text onto
	 * @param text the text to display
	 * @param startPosition the starting position of the text
	 * @param direction the direction the text should go if there are new lines (up or down)
	 */
	private void drawText(Graphics2D graphics, String text, Point startPosition, String direction){

		for(String line : text.split("\n")){
			graphics.drawString(line, startPosition.x, startPosition.y);

			switch (direction.toUpperCase()){
				case("UP"):
					//The next line is on top of the last one
					startPosition.y -= graphics.getFontMetrics().getHeight();
					break;
				case ("DOWN"):
					//The next line is on the bottom of the last one
					startPosition.y += graphics.getFontMetrics().getHeight();
					break;
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		int ex = (int) ((e.getX()-viewX-offX)*(1.0/(double)zoom));
		int ey = (int) ((e.getY()-viewY-offY)*(1.0/(double)zoom));

		Espece selected = this.simulation.getAllEspeces().get(0);
		int proche = selected.distanceFrom(new Point(ex,ey));
		for(Espece espece : this.simulation.getAllEspeces()) {
			//Reset selected
			espece.selected = false;

			if(proche >= espece.distanceFrom(new Point(ex, ey))) {
				proche = espece.distanceFrom(new Point(ex, ey));
				selected = espece;
			}

		}
		selected.setSelected(true);

		//TODO: Meilleure facon de faire ca?
		EVO.frame.changeNetworkFocus(selected);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		lastMousePositionX = e.getX()-viewX;
		lastMousePositionY = e.getY()-viewY;
	}

	@Override
	public void mouseReleased(MouseEvent e) { }

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {

		double delta = e.getWheelRotation() / 10.0;
		double zoomOld = zoom;
		zoom /= Math.exp(delta);

		//Allow a maximum ammount of zoom
		if (zoom <= 0.2) {
			zoom = 0.2f;
		}
		if (zoom >= 2.5) {
			zoom = 2.5f;
		}

		//Get the position of the mouse when the user is scrolling
		double mouseXOnMap = (viewX + offX) * (1.0 / (double) zoomOld) - e.getX() * (1.0 / (double) zoomOld);
		double mouseYOnMap = (viewY + offY) * (1.0 / (double) zoomOld) - e.getY() * (1.0 / (double) zoomOld);

		//Make it looks like it is zooming where the mouse is
		offX += (int) (-mouseXOnMap * -(zoom - zoomOld));
		offY += (int) (-mouseYOnMap * -(zoom - zoomOld));
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		viewX = (int) (e.getX()-lastMousePositionX);
		viewY = (int) (e.getY()-lastMousePositionY);
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {}
	@Override
	public void mouseEntered(MouseEvent arg0) {}
	@Override
	public void mouseExited(MouseEvent arg0) {}
}
