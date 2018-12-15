package org.lrima.Interface.options;

import org.lrima.Interface.options.types.OptionDialogListener;
import org.lrima.core.UserPrefs;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class OptionsDialog extends JDialog implements ActionListener {
    private LinkedHashMap<String, OptionsDisplayPanel> optionPanel;
    private ArrayList<OptionDialogListener> listeners = new ArrayList<>();
    JTabbedPane tabbedPane = new JTabbedPane();



    private JButton okButton;

    private LinkedHashMap<String, ArrayList<String>> allTabs;

    public OptionsDialog(){
        setTitle("Options");
        tabbedPane.setTabPlacement(JTabbedPane.LEFT);
        this.setupSize();
        this.setupButtons();
        this.setupDefault();
        this.setUpOptionPanel();
        this.setupTabs();
    }

    public OptionsDialog(String name, LinkedHashMap<String, Option> options) {
        setTitle(name);
        this.optionPanel = new LinkedHashMap<>();
        this.setupSize();
        this.setupButtons();
        this.addTab(name, options);
        this.setupTabs();

    }

    public void addTab(String name, LinkedHashMap<String, Option> options){
        if(!optionPanel.containsKey(name)) {
            this.optionPanel.put(name, new OptionsDisplayPanel(options));
            this.setupTabs();
        }
    }

    private void setUpOptionPanel() {
        optionPanel = new LinkedHashMap<>();
        allTabs.forEach((String name, ArrayList<String> keys) -> {
           LinkedHashMap<String, Option> options = new LinkedHashMap<>();
           keys.stream().forEach(key -> {
               options.put(key, UserPrefs.getOption(key));
           });

           optionPanel.put(name, new OptionsDisplayPanel(options));
        });
    }

    private void setupDefault() {
        allTabs = new LinkedHashMap<>();

        //Simulation tabs
        ArrayList<String> simulationTab = new ArrayList<>();
        //TODO : ADD ALL THE SETTINGS
        simulationTab.add(UserPrefs.KEY_TIME_LIMIT);
        simulationTab.add(UserPrefs.KEY_NUMBER_SIMULATION);
        simulationTab.add(UserPrefs.KEY_NUMBER_GENERATION_PER_SIMULATION);
        simulationTab.add(UserPrefs.KEY_MAP_TO_USE);

        allTabs.put("Simulation", simulationTab);

        //Car tab
        ArrayList<String> carTab = new ArrayList<>();
        carTab.add(UserPrefs.KEY_NUMBER_OF_CAR);
        carTab.add(UserPrefs.KEY_TURN_RATE);
        allTabs.put("Car", carTab);

    }

    /**
     * Sets the size and position of the dialog
     */
    private void setupSize(){
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        //Set the site of the dialog and centers it on the screen
        int width =  screenSize.width / 3;
        int heigth = screenSize.height / 3;
        int x = (screenSize.width / 2) - (width / 2);
        int y = (screenSize.height / 2) - (heigth);

        setBounds(x, y, width, heigth);
        setResizable(false);
    }

    /**
     * Creates a button to save the options and one to cancel
     */
    private void setupButtons(){
        //The save button
        okButton = new JButton("Ok");
        okButton.addActionListener(this);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(okButton);

        //Add the two button to the bottom of the dialog box
        add(buttonPanel, "South");
    }

    /**
     * Adds the options tabs to the dialog
     */
    private void setupTabs(){
        if(this.optionPanel.size() > 1){

            optionPanel.forEach((name, optionPanel)->{
                tabbedPane.addTab(name, optionPanel);
            });

            add(tabbedPane);
        }
        else{
            String key = optionPanel.keySet().iterator().next();

            this.setTitle(fromKeyToNormalText(key));
            this.add(this.optionPanel.get(key));
        }
    }

    static String fromKeyToNormalText(String key){
        return key.charAt(0) + key.substring(1).toLowerCase().replaceAll("_", " ");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == okButton){
            setVisible(false);

            this.listeners.forEach(OptionDialogListener::onOkPress);
        }
    }

    public void addOptionDialogListener(OptionDialogListener listener){
        this.listeners.add(listener);
    }
}
