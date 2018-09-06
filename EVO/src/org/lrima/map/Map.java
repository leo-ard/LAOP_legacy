package org.lrima.map;

import java.awt.Point;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

import org.lrima.core.UserPrefs;
import org.lrima.espece.Espece;
import org.lrima.map.Studio.Drawables.Line;
import org.lrima.map.Studio.Drawables.Obstacle;
import org.lrima.simulation.Simulation;

public class Map implements Serializable {

    //All the obstacles in the map
	private ArrayList<Obstacle> obstacles = new ArrayList<>();
	//Where the cars should span
	private Point depart = null;

	//The size of the map
	private int mapWidth, mapHeight;


    /**
     * Initialize a map
     */
	public Map(int width, int height) {
        this.mapWidth = width;
        this.mapHeight = height;

	}

    /**
     * Load the map information and obstacle from a file
     * @param filePath the path to the map file
     */
    static public Map loadMapFromFile(String filePath){
        try {
            //Open the file
            FileInputStream fis = new FileInputStream(filePath);
            ObjectInputStream ois = new ObjectInputStream(fis);

            Map map = (Map) ois.readObject();

            map.setDepart(new Point(map.getMapWidth() / 2, map.getMapHeight() / 2));

            return map;

        }catch (Exception e){
            System.out.println("Error while loading the map file");
            e.printStackTrace();
        }

        return null;
    }

    //*******===========================================================================
    //* ACCESSORS AND MUTATORS
    //* ACCESSORS AND MUTATORS
    //********============================================================================/

    public Point getDepart() {
        return depart;
    }

    public ArrayList<Obstacle> getObstacles() {
        return obstacles;
    }

    public int getMapHeight() {
        return mapHeight;
    }

    public int getMapWidth() {
        return mapWidth;
    }

    public void setDepart(Point depart) {
        this.depart = depart;
    }
}
