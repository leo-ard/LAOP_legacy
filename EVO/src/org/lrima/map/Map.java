package org.lrima.map;

import java.awt.Point;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Iterator;

import org.lrima.core.UserPrefs;
import org.lrima.espece.Espece;
import org.lrima.map.Studio.Drawables.Line;
import org.lrima.map.Studio.Drawables.Obstacle;
import org.lrima.simulation.Simulation;

public class Map {

    //All the obstacles in the map
	private ArrayList<Line> obstacles;
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
        this.loadMapFromFile("test.map");
	}

    /**
     * Load the map information and obstacle from a file
     * @param filePath the path to the map file
     */
    private void loadMapFromFile(String filePath){
        this.obstacles = new ArrayList<Line>();

        try {
            //Open the file
            FileInputStream fis = new FileInputStream(filePath);
            ObjectInputStream ois = new ObjectInputStream(fis);

            //Get all the obstacles stored in the file
            this.depart = (Point) ois.readObject();
            this.obstacles = (ArrayList<Line>) ois.readObject();

            //Find the start position
            Iterator<Line> iterator = obstacles.iterator();
            while (iterator.hasNext()){
                Obstacle obstacle = iterator.next();
                if(obstacle.type.equals(Obstacle.TYPE_START)){
                    this.depart = obstacle.getPosition();
                }
            }

        }catch (Exception e){
            System.out.println("Error while loading the map file");
            e.printStackTrace();
        }
    }

    //*******===========================================================================
    //* ACCESSORS AND MUTATORS
    //* ACCESSORS AND MUTATORS
    //********============================================================================/

    public Point getDepart() {
        return depart;
    }

    public ArrayList<Line> getObstacles() {
        return obstacles;
    }

    public int getMapHeight() {
        return mapHeight;
    }

    public int getMapWidth() {
        return mapWidth;
    }
}
