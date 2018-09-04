package org.lrima.map.Studio;

import org.lrima.map.Map;
import org.lrima.map.Studio.Drawables.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;


/**
 * Todo: Refaire cette classe, en sauvegardant un Map dans le fichier au lieu d'un arraylist
 */
public class Studio extends JFrame implements ActionListener {

    private Map map;
    private DrawingPanel drawingPanel;

    private JButton startButton;
    private JButton endButton;
    private JButton lineButton;

    private JFileChooser fileChooser;
    private JMenuItem saveMenuItem;
    private JMenuItem loadMenuItem;
    private JMenuItem newMapMenuItem;

    /**
     * Enter directly into the studio
     */
    public static void main(String[] args){
        Studio s = new Studio();
        s.setVisible(true);
        s.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public Studio(){
        setTitle("Map Studio");
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        setupMenu();
        setupToolBar();

        initializeMap(10000, 10000);

        drawingPanel = new DrawingPanel(this.map);
        add(drawingPanel);

        //Setup the file chooser for the menu
        fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File("."));
    }

    /**
     * Initialize the map variable with a width and height
     * @param width the width the map has to be
     * @param height the height the map has to be
     */
    private void initializeMap(int width, int height){
        this.map = new Map(10000, 10000);
        map.setDepart(new Point(0, 0));
    }

    /**
     * Add all the buttons to the menu
     */
    private void setupMenu(){
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        //The file menu
        JMenu file = new JMenu("File");
        menuBar.add(file);

        //Reset the map button
        newMapMenuItem = new JMenuItem("New");
        newMapMenuItem.addActionListener(this);
        file.add(newMapMenuItem);

        //Save the map button
        saveMenuItem = new JMenuItem("Save");
        saveMenuItem.addActionListener(this);
        file.add(saveMenuItem);

        //Load map from file button
        loadMenuItem = new JMenuItem("load");
        loadMenuItem.addActionListener(this);
        file.add(loadMenuItem);
    }

    /**
     * Add buttons on the toolbar to add the starting point and add obstacles
     */
    private void setupToolBar(){
        JToolBar toolBar = new JToolBar();

        //Button to add a start to the map
        startButton = new JButton();
        startButton.setIcon(new ImageIcon(this.getClass().getResource("/images/icons/Map_Studio/start.gif")));
        startButton.addActionListener(this);
        toolBar.add(startButton);

        //Button to add a single line
        lineButton = new JButton();
        lineButton.setIcon(LineObstacle.OBSTACLE_ICON);
        lineButton.addActionListener(this);
        toolBar.add(lineButton);

        //Adds the toolbar to the bottom of the screen
        add(toolBar, "South");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //When you click on the line button in the tool bar
        if(e.getSource() == lineButton){
            this.drawingPanel.setSelectedObstacle(new LineObstacle());
        }

        //Menu buttons
        if(e.getSource() == newMapMenuItem){
            this.map = new Map(10000, 10000);
            this.drawingPanel = new DrawingPanel(map);
        }
        if(e.getSource() == saveMenuItem){
            fileChooser.setApproveButtonText("Save");

            int returnVal = fileChooser.showOpenDialog(this);
            if(returnVal == JFileChooser.APPROVE_OPTION){
                save(fileChooser.getSelectedFile());
            }
        }
        if(e.getSource() == loadMenuItem){
            fileChooser.setApproveButtonText("Load");
            int returnVal = fileChooser.showOpenDialog(this);

            if(returnVal == JFileChooser.APPROVE_OPTION){
                load(fileChooser.getSelectedFile());
            }
        }
    }

    /**
     * Save the map in a file
     * @param file the file to save the map into
     */
    public void save(File file){
        try {
            FileOutputStream fis = new FileOutputStream(file, false);
            ObjectOutputStream oos = new ObjectOutputStream(fis);

            //Todo: Popup si getStart == null
            if(drawingPanel.getMap().getDepart() != null) {
                oos.writeObject(drawingPanel.getMap());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //TODO: doesn't work

    /**
     * Load a map from a file
     * @param file the file to load the map from
     */
    public void load(File file){
        try{
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream oos = new ObjectInputStream(fis);

            drawingPanel = new DrawingPanel((Map)oos.readObject());
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
