package org.lrima.map.Studio;

import org.lrima.core.UserPrefs;
import org.lrima.map.Map;
import org.lrima.map.Studio.Drawables.*;
import org.lrima.map.Studio.tools.CreateObstacleTool;
import org.lrima.map.Studio.tools.CreateStartTool;
import org.lrima.map.Studio.tools.EditObstacleTool;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;


/**
 * Class that manage the edition of maps
 */
public class Studio extends JFrame implements ActionListener {
    private Map map;
    private DrawingPanel drawingPanel;
    private JButton rectangleButton, selectionButton, multipleLineButton, lineButton, startButton;

    private JFileChooser fileChooser;
    private JMenuItem save, load, newMap;


    /**
     * Entry to launch the studio
     */
    public static void main(String[] args){
        Studio s = new Studio();
        s.setVisible(true);
        s.setDefaultCloseOperation(EXIT_ON_CLOSE);
        s.setSize(new Dimension(1080, 800));
    }

    /**
     * Creates the frame that displays everything to edit a map
     */
    public Studio(){
        this.map = new Map(10000, 10000);
        map.setDepart(new Point(0, 0));

        setTitle("Map Studio");
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        setupMenu();
        setupToolBar();

        drawingPanel = new DrawingPanel(this.map);
        drawingPanel.setFocusable(true);
        drawingPanel.requestFocusInWindow();
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
        startButton.setIcon(new ImageIcon(this.getClass().getResource(UserPrefs.SRC_TOOLS_START)));
        startButton.addActionListener(this);
        toolBar.add(startButton);

        /*endButton = new JButton();
        endButton.setIcon(new ImageIcon(this.getClass().getResource("/images/icons/Map_Studio/finish.gif")));
        endButton.addActionListener(this);
        toolBar.add(endButton);*/

        lineButton = new JButton();
        lineButton.setIcon(LineObstacle.OBSTACLE_ICON);
        lineButton.addActionListener(this);
        toolBar.add(lineButton);

        multipleLineButton = new JButton();
        multipleLineButton.setIcon(MultipleLineObstacle.OBSTACLE_ICON);
        multipleLineButton.addActionListener(this);
        toolBar.add(multipleLineButton);

        rectangleButton = new JButton();
        rectangleButton.setIcon(RectangleObstacle.OBSTACLE_ICON);
        rectangleButton.addActionListener(this);
        toolBar.add(rectangleButton);

        selectionButton = new JButton();
        selectionButton.setIcon(new ImageIcon(new ImageIcon((this.getClass().getResource(UserPrefs.SRC_TOOLS_SELECTION))).getImage().getScaledInstance(40,40,Image.SCALE_DEFAULT)));
        selectionButton.addActionListener(this);
        toolBar.add(selectionButton);


        add(toolBar, "South");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Obstacle obstacle = null;

        //tools
        if(e.getSource() == lineButton){
            this.drawingPanel.setTool(new CreateObstacleTool(new LineObstacle(), drawingPanel.getMap().getObstacles()));
        }
        else if(e.getSource() == multipleLineButton){
            this.drawingPanel.setTool(new CreateObstacleTool(new MultipleLineObstacle(), drawingPanel.getMap().getObstacles()));
        }
        else if(e.getSource() == rectangleButton){
            this.drawingPanel.setTool(new CreateObstacleTool(new RectangleObstacle(), drawingPanel.getMap().getObstacles()));
        }
        if(e.getSource() == selectionButton){
            this.drawingPanel.setTool(new EditObstacleTool(this.drawingPanel.selectedObstacles));
        }

        if(e.getSource() == startButton){
            this.drawingPanel.setTool(new CreateStartTool());
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
