package org.lrima.Interface.options;

import org.lrima.core.UserPrefs;

import javax.swing.*;
import java.awt.*;

public class OptionSimulationPanel extends JPanel implements OptionPanel {

    private JLabel timeLimitLabel, loadLastSavedLabel;
    private JTextField timeLimitTextField;
    private JCheckBox loadLastSavedCheck;

    /**
     * Creates a JPanel with all the options for the simulation
     */
    public OptionSimulationPanel(){

        FlowLayout sectionLayout = new FlowLayout(FlowLayout.LEFT, 15, 5);
        Box box = Box.createVerticalBox();
        add(box);

        JPanel timeLimitPanel = new JPanel();
        timeLimitPanel.setLayout(sectionLayout);
        timeLimitLabel = new JLabel("Time limit (seconds) :");
        timeLimitTextField = new JTextField(5);
        timeLimitPanel.add(timeLimitLabel);
        timeLimitPanel.add(timeLimitTextField);
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

    /**
     * Load the options saved in the preferences and puts it into the things
     */
    private void loadPrefs(){
        UserPrefs.load();

        int timeLimit = (int)(UserPrefs.TIME_LIMIT / 1000);
        boolean loadBest = UserPrefs.USE_BEST;

        timeLimitTextField.setText("" + timeLimit);
        loadLastSavedCheck.setSelected(loadBest);
    }

    @Override
    public boolean save(){
        int timeLimit;

        try{
            timeLimit = Integer.parseInt(timeLimitTextField.getText());
        }catch(NumberFormatException e){
            System.out.println("Erreur");
            return false;
        }

        //Put the new value into the preferences
        UserPrefs.preferences.putInt(UserPrefs.KEY_TIME_LIMIT, timeLimit * 1000);
        UserPrefs.preferences.putBoolean(UserPrefs.KEY_USE_LAST_SAVED, loadLastSavedCheck.isSelected());

        return true;
    }
}
