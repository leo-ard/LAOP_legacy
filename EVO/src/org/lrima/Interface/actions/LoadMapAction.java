package org.lrima.Interface.actions;

import org.lrima.core.UserPrefs;
import org.lrima.map.Map;
import org.lrima.map.MapPanel;
import org.lrima.simulation.Simulation;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

public class LoadMapAction extends AbstractAction {

    private MapPanel mapPanel;
    private Simulation simulation;
    JFileChooser fileChooser = new JFileChooser();

    public LoadMapAction(String name, MapPanel panel, Simulation simulation){
        super(name);
        this.mapPanel = panel;
        this.simulation = simulation;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        fileChooser.setCurrentDirectory(new File("."));
        fileChooser.setApproveButtonText("Load Map");

        int returnVal = fileChooser.showOpenDialog(null);
        if(returnVal == JFileChooser.APPROVE_OPTION){
            this.loadMap(fileChooser.getSelectedFile());
        }
    }

    /**
     * Loads a map from a file and restart the simulation.
     * It also sets the file path in the preferences.
     * @param file the file to load the map from
     */
    private void loadMap(File file){
        try{
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);

            UserPrefs.preferences.put(UserPrefs.KEY_MAP_TO_USE, file.getPath());

            this.mapPanel.setMap((Map) ois.readObject());
            simulation.setShouldRestart(true);
            simulation.setPausing(false);

        }catch (Exception e){
            System.err.println(e.getMessage());
        }
    }
}
