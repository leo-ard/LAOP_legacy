package org.lrima.map.Studio;

import org.lrima.map.Studio.Drawables.*;
import org.lrima.simulation.Simulation;

import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;

public class Studio extends JFrame implements ActionListener {

    Simulation simulation;
    DrawingPanel drawingPanel;

    JButton startButton;
    JButton endButton;
    JButton lineButton;

    JFileChooser fileChooser;
    JMenuItem save;
    JMenuItem load;
    JMenuItem newMap;

    public Studio(Simulation simulation){
        this.simulation = simulation;

        setTitle("Map Studio");
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        setupMenu();
        setupToolBar();

        drawingPanel = new DrawingPanel(this);
        add(drawingPanel);

        //Continue la simulation quand on ferme le Studio
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                simulation.setPausing(false);
            }
        });

        fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File("."));
    }

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
        lineButton.setIcon(new ImageIcon(this.getClass().getResource("/images/icons/Map_Studio/line.gif")));
        lineButton.addActionListener(this);
        toolBar.add(lineButton);

        add(toolBar, "South");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Obstacle obstacle = null;
        drawingPanel.resetClic();

        if(e.getSource() == startButton){
            obstacle = new Start();
        }
        if(e.getSource() == endButton){
            obstacle = new End();
        }
        if(e.getSource() == lineButton){
            obstacle = new Line();
        }

        if(obstacle != null) {
            drawingPanel.setSelectedObstacle(obstacle);
        }

        //Menu
        if(e.getSource() == newMap){
            drawingPanel.newMap();
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

            System.out.println("Saving: " + drawingPanel.placedObstacles.size());

            oos.writeObject(drawingPanel.placedObstacles);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void load(File file){
        try{
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream oos = new ObjectInputStream(fis);

            System.out.println("Loading: " + drawingPanel.placedObstacles.size());

            drawingPanel.placedObstacles = (ArrayList<Obstacle>)oos.readObject();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
