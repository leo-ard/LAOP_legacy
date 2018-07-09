package org.lrima.simulation.Interface.options;

import org.lrima.core.UserPrefs;

import javax.swing.*;
import java.awt.*;

public class SimulationPanel extends JPanel implements OptionPanel {

    JLabel timeLimitLabel, loadLastSavedLabel;
    JTextField timeLimitField;
    JCheckBox loadLastSavedCheck;

    public SimulationPanel(){

        FlowLayout sectionLayout = new FlowLayout(FlowLayout.LEFT, 15, 5);
        Box box = Box.createVerticalBox();
        add(box);

        JPanel timeLimitPanel = new JPanel();
        timeLimitPanel.setLayout(sectionLayout);
        timeLimitLabel = new JLabel("Time limit (seconds) :");
        timeLimitField = new JTextField(5);
        timeLimitPanel.add(timeLimitLabel);
        timeLimitPanel.add(timeLimitField);
        box.add(timeLimitPanel);

        JPanel loadBestPanel = new JPanel();
        loadBestPanel.setLayout(sectionLayout);
        loadLastSavedLabel = new JLabel("Automaticaly load last saved NN:");
        loadLastSavedCheck = new JCheckBox();
        loadBestPanel.add(loadLastSavedLabel);
        loadBestPanel.add(loadLastSavedCheck);
        box.add(loadBestPanel);

        loadPrefs();

    }

    private void loadPrefs(){
        UserPrefs.load();

        int timeLimit = (int)(UserPrefs.TIME_LIMIT / 1000);
        boolean loadBest = UserPrefs.USE_BEST;

        timeLimitField.setText("" + timeLimit);
        loadLastSavedCheck.setSelected(loadBest);
    }

    @Override
    public boolean save(){
        int timeLimit;

        try{
            timeLimit = Integer.parseInt(timeLimitField.getText());
        }catch(NumberFormatException e){
            System.out.println("Erreur");
            return false;
        }

        UserPrefs.preferences.putInt(UserPrefs.KEY_TIME_LIMIT, timeLimit * 1000);
        UserPrefs.preferences.putBoolean(UserPrefs.KEY_USE_LAST_SAVED, loadLastSavedCheck.isSelected());

        return true;
    }
}
