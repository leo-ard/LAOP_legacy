package org.lrima.map;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import org.lrima.core.EVO;
import org.lrima.core.UserPrefs;
import org.lrima.espece.Espece;
import org.lrima.map.Studio.Drawables.Line;
import org.lrima.map.Studio.Drawables.Obstacle;
import org.lrima.Interface.EspeceInfoPanel;
import org.lrima.simulation.Simulation;

/**
 * Panel that displays a map
 */
public class MapPanel extends JPanel implements MouseMotionListener, MouseListener, MouseWheelListener{

	//The map to display
	protected Map map;

	//View range
	//TODO: Trouver noms plus descriptifs
	protected int viewX, viewY;
	protected int offX, offY;
	protected float zoom;

	//Used for the drag motion on the map
	protected Point lastMousePosition;

	//Stores the logo of LRIMA;
	private BufferedImage LRIMA_image;

	//The simulation to use
	private Simulation simulation;

	//Colors used on the map
	private final Color COLOR_BACKGROUND = new Color(238, 238, 238);
	private final Color COLOR_BACKGROUND_LINES = new Color(136, 136, 136);
	private final Color COLOR_OBSTACLE = new Color(176, 176, 176);
	private final Color COLOR_TEXT = new Color(0, 0, 0);
	private final Color COLOR_START = new Color(51,255, 107);
	private final int START_POINT_SIZE = 50;

	//Used for the text
	private final int TEXT_MARGIN = 20;
	private final int FONT_SIZE = 32;


	/**
	 * Creates an panel to display the map in it's current state
	 *
	 * @param map the map to be displayed
	 */
	protected MapPanel(Map map){
		//Setup variables
		this.map = map;
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
	 * Creates a panel to display the map's simulation and especes
	 *
	 * @param simulation the map's simulation to be displayed
	 */
	public MapPanel(Simulation simulation) {
		this(simulation.getMap());
		//Setup variables
		this.simulation = simulation;
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

		doTransforms(graphics);
		setupRelativeGraphics(graphics);

		//Reset the zoom and translations
		graphics.setTransform(new AffineTransform());
		setupStaticGraphics(graphics);
	}

	/**
	 * Draws the graphics that are not moving relative to the map
	 *
	 * @param graphics to be drawn on
	 */
	protected void setupStaticGraphics(Graphics2D graphics){
		//Write the informations
		drawInformation(graphics);

		//Put the LRIMA image on the top left
		graphics.drawImage(this.LRIMA_image,0, 0,null);
	}

	/**
	 * Draws the graphics that are moving relative to the map
	 *
	 * @param graphics to be drawn on
	 */
	protected void setupRelativeGraphics(Graphics2D graphics){
		drawBackground(graphics);
		drawObstacles(graphics);
		drawStart(graphics);
		drawCars(graphics);
		drawPoints(graphics);

	}

	protected void drawPoints(Graphics2D graphics){
		//Draw the start
		graphics.setColor(Color.GREEN);
		graphics.fillOval(map.getDepart().x, map.getDepart().y, 50, 50);
	}

	/**
	 * Applies the zoom and translations to graphics
	 * @param graphics the graphics to modify
	 */
	protected void doTransforms(Graphics2D graphics){
		double translateX, translateY;

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
	}

	/**
	 * Draws the background of the map and the lines
	 * @param graphics the graphics to put a map into
	 */
	protected void drawBackground(Graphics2D graphics){
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

	protected void drawStart(Graphics2D graphics){
		int x = (int) map.getDepart().getX();
		int y = (int) map.getDepart().getY();

		graphics.setColor(this.COLOR_START);
		graphics.fillOval(x-this.START_POINT_SIZE/2, y-this.START_POINT_SIZE/2, this.START_POINT_SIZE, this.START_POINT_SIZE);
	}

	/**
	 * Draws all the obstacles
	 * @param graphics the graphics to put the obstacles into
	 */
	protected void drawObstacles(Graphics2D graphics){
		graphics.setColor(Color.RED);
		graphics.setStroke(new BasicStroke(3));
		for(Obstacle o : map.getObstacles()){
			ArrayList<Line> lines = o.getLines();
			for(Line line : lines){
				line.draw(graphics);
			}
		}
	}

	/**
	 * Draw the cars
	 * @param graphics the graphics to put the cars into
	 */
	protected void drawCars(Graphics2D graphics){
		graphics.setColor(Color.blue);
		for(Espece e : this.simulation.getAllEspeces()) {
			e.draw(graphics);
		}
	}

	/**
	 * Draw additional information such as the current generation, the time
	 * @param graphics the graphics to put the information into
	 */
	protected void drawInformation(Graphics2D graphics){
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

		Point pointClickedOnMap = mapPointFromScreenPoint(e.getPoint());

		Espece selected = this.simulation.getAllEspeces().get(0);
		int proche = selected.distanceFrom(pointClickedOnMap);
		for(Espece espece : this.simulation.getAllEspeces()) {
			//Reset selected
			espece.selected = false;

			if(proche >= espece.distanceFrom(pointClickedOnMap)) {
				proche = espece.distanceFrom(pointClickedOnMap);
				selected = espece;
			}

		}
		selected.setSelected(true);

		//TODO: Meilleure facon de faire ca?
		EVO.frame.changeNetworkFocus(selected);
		//Get the information of the car in the EspeceInfoPanel if it is selected
		EspeceInfoPanel.update(selected);
	}

	public Point mapPointFromScreenPoint(Point screenPoint){
		int x = (int) ((screenPoint.getX()-viewX-offX)*(1.0/(double)zoom));
		int y = (int) ((screenPoint.getY()-viewY-offY)*(1.0/(double)zoom));


		return new Point(x, y);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		lastMousePosition = new Point(e.getX()-viewX, e.getY()-viewY);
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

		this.repaint();
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		viewX = (int) (e.getX()-lastMousePosition.x);
		viewY = (int) (e.getY()-lastMousePosition.y);

		this.repaint();
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {}
	@Override
	public void mouseEntered(MouseEvent arg0) {}
	@Override
	public void mouseExited(MouseEvent arg0) {}

	public void setMap(Map map){
		this.map = map;
		this.repaint();
	}

	public Map getMap() {
		return map;
	}
}
