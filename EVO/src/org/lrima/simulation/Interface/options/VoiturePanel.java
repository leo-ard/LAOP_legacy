package org.lrima.simulation.Interface.options;

import org.lrima.core.UserPrefs;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.prefs.Preferences;

public class VoiturePanel extends JPanel implements OptionPanel, ActionListener {



    private JLabel carImageLabel;
    private JLabel numberOfCarLabel;
    private JLabel carSpeedLabel;
    private JLabel turnrateLabel;

    private JTextField carImageURL;
    private JTextField numberOfCarTextField;
    private JTextField carSpeedTextField;
    private JTextField turnrateTextField;

    private JFileChooser fileChooser;
    JButton carImageOpenButton;

    public VoiturePanel(){
        Box box = Box.createVerticalBox();
        add(box);

        FlowLayout sectionLayout = new FlowLayout(FlowLayout.LEFT, 15, 5);
        //Car image
        fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File("."));

        JPanel carImagePanel = new JPanel(sectionLayout);
        carImageLabel = new JLabel("Car image:");
        carImageURL = new JTextField(20);
        carImageOpenButton = new JButton("Open");
        carImageOpenButton.addActionListener(this);

        carImagePanel.add(carImageLabel);
        carImagePanel.add(carImageURL);
        carImagePanel.add(carImageOpenButton);
        box.add(carImagePanel);

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
     * Mets les options sauvegardés dans les textfields
     */
    private void load_prefs(){
        UserPrefs.load();

        numberOfCarTextField.setText("" + UserPrefs.NUMBERCARS);
        carSpeedTextField.setText("" + UserPrefs.VITESSE_VOITURE);
        turnrateTextField.setText("" + UserPrefs.TURNRATE);
        carImageURL.setText(UserPrefs.SRC_VOITURE);
    }

    @Override
    public boolean save(){
        int numberOfCar, carSpeed;
        double turnRate;

        try {
            numberOfCar = Integer.parseInt(numberOfCarTextField.getText());
            carSpeed = Integer.parseInt(carSpeedTextField.getText());
            turnRate = Double.parseDouble(turnrateTextField.getText());
        }catch (NumberFormatException e){
            System.out.println("EREUR D'entré");
            return false; //Si le user entre des lettres ou quelque chose d'imprévue
        }

        UserPrefs.preferences.putInt(UserPrefs.KEY_NUMBER_OF_CAR, numberOfCar);
        UserPrefs.preferences.putInt(UserPrefs.KEY_CAR_SPEED, carSpeed);
        UserPrefs.preferences.putDouble(UserPrefs.KEY_TURN_RATE, turnRate);
        UserPrefs.preferences.put(UserPrefs.KEY_CAR_IMAGE_URL, carImageURL.getText());

        System.out.println(UserPrefs.preferences.getInt(UserPrefs.KEY_NUMBER_OF_CAR, 10));

        return true;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == carImageOpenButton){
            int returnVal = fileChooser.showOpenDialog(this);

            if(returnVal == JFileChooser.APPROVE_OPTION){
                File selectedFile = fileChooser.getSelectedFile();
                String absolutePath = selectedFile.getAbsolutePath();
                this.carImageURL.setText(absolutePath);
            }
        }
    }
}
