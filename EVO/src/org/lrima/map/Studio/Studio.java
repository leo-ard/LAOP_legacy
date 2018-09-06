package org.lrima.map.Studio;

import org.lrima.map.Map;
import org.lrima.map.Studio.Drawables.*;
import org.lrima.simulation.Simulation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;


/**
 * Todo: Refaire cette classe, en sauvegardant un Map dans le fichier au lieu d'un arraylist
 */
public class Studio extends JFrame implements ActionListener {

    private Map map;
    DrawingPanel drawingPanel;

    JButton startButton;
    JButton endButton;
    JButton lineButton;

    JFileChooser fileChooser;
    JMenuItem save;
    JMenuItem load;
    JMenuItem newMap;
    private JButton multipleLineButton;

    /**
     * Enter directly into the studio
     */
    public static void main(String[] args){
        Studio s = new Studio();
        s.setVisible(true);
        s.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public Studio(){
        this.map = new Map(10000, 10000);
        map.setDepart(new Point(map.getMapWidth() / 2, map.getMapHeight() /2));

        setTitle("Map Studio");
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        setupMenu();
        setupToolBar();

        drawingPanel = new DrawingPanel(this.map);
        add(drawingPanel);

        drawingPanel.requestFocus();

        fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File("."));
    }

    /**
     * Add all the buttons to the menu
     */
    private void setupMenu(){
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu file = new JMenu("File");
        menuBar.add(file);

        newMap = new JMenuItem("New");
        newMap.addActionListener(this);
        file.add(newMap);

        save = new JMenuItem("Save");
        save.addActionListener(this);
        file.add(save);

        load = new JMenuItem("load");
        load.addActionListener(this);
        file.add(load);
    }

    /**
     * Add buttons on the toolbar to add the starting point and add obstacles
     */
    private void setupToolBar(){
        JToolBar toolBar = new JToolBar();

        startButton = new JButton();
        startButton.setIcon(new ImageIcon(this.getClass().getResource("/images/icons/Map_Studio/start.gif")));
        startButton.addActionListener(this);
        toolBar.add(startButton);

        endButton = new JButton();
        endButton.setIcon(new ImageIcon(this.getClass().getResource("/images/icons/Map_Studio/finish.gif")));
        endButton.addActionListener(this);
        toolBar.add(endButton);

        lineButton = new JButton();
        lineButton.setIcon(LineObstacle.OBSTACLE_ICON);
        lineButton.addActionListener(this);
        toolBar.add(lineButton);

        multipleLineButton = new JButton();
        multipleLineButton.setIcon(MultipleLineObstacle.OBSTACLE_ICON);
        multipleLineButton.addActionListener(this);
        toolBar.add(multipleLineButton);

        add(toolBar, "South");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Obstacle obstacle = null;

        if(e.getSource() == startButton){

        }

        if(e.getSource() == multipleLineButton){
            this.drawingPanel.setSelectedObstacle(new MultipleLineObstacle());
        }

        if(e.getSource() == lineButton){
            this.drawingPanel.setSelectedObstacle(new LineObstacle());
        }

        if(obstacle != null) {
            drawingPanel.setSelectedObstacle(obstacle);
        }

        //Menu
        if(e.getSource() == newMap){
            this.map = new Map(10000, 10000);
            this.drawingPanel = new DrawingPanel(map);
        }
        if(e.getSource() == save){
            fileChooser.setApproveButtonText("Save");

            int returnVal = fileChooser.showOpenDialog(this);
            if(returnVal == JFileChooser.APPROVE_OPTION){
                save(fileChooser.getSelectedFile());
            }
        }
        if(e.getSource() == load){
            fileChooser.setApproveButtonText("Load");
            int returnVal = fileChooser.showOpenDialog(this);

            if(returnVal == JFileChooser.APPROVE_OPTION){
                load(fileChooser.getSelectedFile());
            }
        }
    }

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
