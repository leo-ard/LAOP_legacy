package org.lrima.simulation.Interface.options;

import org.lrima.core.UserPrefs;

import javax.swing.*;
import java.awt.*;

public class OptionVoiturePanel extends JPanel implements OptionPanel {



    private JLabel numberOfCarLabel;
    private JLabel carSpeedLabel;
    private JLabel turnrateLabel;

    private JTextField numberOfCarTextField;
    private JTextField carSpeedTextField;
    private JTextField turnrateTextField;

    /**
     * Creates a JPanel with all the options fields for the car
     */
    public OptionVoiturePanel(){
        Box box = Box.createVerticalBox();
        add(box);

        FlowLayout sectionLayout = new FlowLayout(FlowLayout.LEFT, 15, 5);

        //Number of car
        JPanel numberOfCarPanel = new JPanel(sectionLayout);
        numberOfCarLabel = new JLabel("Number of car:");
        numberOfCarTextField = new JTextField(4);

        numberOfCarPanel.add(numberOfCarLabel);
        numberOfCarPanel.add(numberOfCarTextField);
        box.add(numberOfCarPanel);

        //Car speed
        JPanel carSpeedPanel = new JPanel(sectionLayout);
        carSpeedLabel = new JLabel("Car speed:");
        carSpeedTextField = new JTextField(4);

        carSpeedPanel.add(carSpeedLabel);
        carSpeedPanel.add(carSpeedTextField);
        box.add(carSpeedPanel);

        //Turn rate
        JPanel turnratePanel = new JPanel(sectionLayout);
        turnrateLabel = new JLabel("Turn Rate:");
        turnrateTextField = new JTextField(4);

        turnratePanel.add(turnrateLabel);
        turnratePanel.add(turnrateTextField);
        box.add(turnratePanel);


        load_prefs();
    }

    /**
     * Loads the user preferences and put the values into the form
     */
    private void load_prefs(){
        UserPrefs.load();

        numberOfCarTextField.setText("" + UserPrefs.NUMBERCARS);
        carSpeedTextField.setText("" + UserPrefs.VITESSE_VOITURE);
        turnrateTextField.setText("" + UserPrefs.TURNRATE);
    }

    @Override
    public boolean save(){
        int numberOfCar, carSpeed;
        double turnRate;

        //Check if the user entered numbers
        try {
            numberOfCar = Integer.parseInt(numberOfCarTextField.getText());
            carSpeed = Integer.parseInt(carSpeedTextField.getText());
            turnRate = Double.parseDouble(turnrateTextField.getText());
        }catch (NumberFormatException e){
            System.out.println("EREUR D'entrée");
            return false;
        }

        //Save the new preferences
        UserPrefs.preferences.putInt(UserPrefs.KEY_NUMBER_OF_CAR, numberOfCar);
        UserPrefs.preferences.putInt(UserPrefs.KEY_CAR_SPEED, carSpeed);
        UserPrefs.preferences.putDouble(UserPrefs.KEY_TURN_RATE, turnRate);

        System.out.println(UserPrefs.preferences.getInt(UserPrefs.KEY_NUMBER_OF_CAR, 10));

        return true;
    }
}
