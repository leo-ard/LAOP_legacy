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
     * Load the map information and obstacle from the preferences of the user
     * If it is not set in the preferences, the map default.map is used
     */
    static public Map loadMapFromPreferences(){
        try {
            String filePath = UserPrefs.getFile(UserPrefs.KEY_MAP_TO_USE).getPath();

            //Open the file
            FileInputStream fis = new FileInputStream(filePath);
            ObjectInputStream ois = new ObjectInputStream(fis);

            Map map = (Map) ois.readObject();

            return map;

        }catch (Exception e){
            e.printStackTrace();
            System.err.println("Could not load the file stored in the preferences. Trying to load default.map ...");

            //If the path of the map to use in the preferences doesn't exist, Use default.map instead
            try {
                String filePath = "./default.map";

                //Open the file
                FileInputStream fis = new FileInputStream(filePath);
                ObjectInputStream ois = new ObjectInputStream(fis);

                Map map = (Map) ois.readObject();

                return map;

            }catch (Exception e2){
                System.err.println("The file 'default.map' doesn't exist !");

            }
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
