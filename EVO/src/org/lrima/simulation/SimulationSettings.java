package org.lrima.simulation;

import org.lrima.Interface.options.Option;
import org.lrima.Interface.options.OptionsDialog;
import org.lrima.Interface.options.types.OptionInt;
import org.lrima.core.UserPrefs;

import javax.swing.*;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class SimulationSettings {

    private HashMap<String, Option> settings;
    private OptionsDialog dialog;

    public SimulationSettings(){
        this.settings = UserPrefs.getVariableSettings();
        System.out.println(settings.get(UserPrefs.KEY_NUMBER_OF_CAR));
    }

    public void setSettings(HashMap<String, Option> settings){
        this.settings = settings;
    }

    public HashMap<String, Option> getSettings() {
        return settings;
    }

    public void applySettings(){
        for(String key : this.settings.keySet()){
            UserPrefs.set(key, settings.get(key));
        }
    }

    public void displayOptions(){
        if(dialog == null) {
            dialog = new OptionsDialog("Simulation settings", new LinkedHashMap<>(settings));
        }
        dialog.setVisible(true);
    }
}
