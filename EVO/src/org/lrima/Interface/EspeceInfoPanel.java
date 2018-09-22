package org.lrima.Interface;

import org.lrima.annotations.DisplayInfo;
import org.lrima.espece.Espece;
import org.lrima.espece.network.NetworkPanel;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.util.HashMap;

//TODO: Improve the way this class works
public class EspeceInfoPanel extends JPanel {

    private Espece espece = null;
    private HashMap<String, Box> information;
    private NetworkPanel networkPanel;

    private Box boxLayout;

    private Dimension screenSize;

    //TODO: Mettre les infos dans une table
    public EspeceInfoPanel(){

        boxLayout = Box.createVerticalBox();

        screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBackground(Color.WHITE);
        //setSize(600, screenSize.height);
        setPreferredSize(new Dimension(screenSize.width / 5, screenSize.height));

        add(boxLayout, BorderLayout.NORTH);

        load();

        networkPanel = new NetworkPanel(null);
        this.add(networkPanel, BorderLayout.SOUTH);
    }

    private void load(){
        information = new HashMap<>();
        try {
            getEspeceFields();
        }catch (Exception e){}
        displayEspeceFields();
    }

    /**
     * Va chercher toutes les informations de l'espece
     */
    private void getEspeceFields() throws IllegalAccessException{
        Class<?> especeClass = Espece.class;

        for(Field field : especeClass.getDeclaredFields()){
            if(field.isAnnotationPresent(DisplayInfo.class)){
                String fieldName = null, fieldValue = null;

                fieldValue = "Select a car";
                fieldName = field.getName();
                field.setAccessible(true);

                if(this.espece != null) {
                    String value = field.get(this.espece).toString();

                    if(field.get(this.espece) instanceof Double || field.get(this.espece) instanceof Float){
                        value = new DecimalFormat("#.##").format(field.get(this.espece));
                    }

                    fieldValue = value;
                }

                if(!information.containsKey(fieldName)) {

                    //Cree la boite avec les deux informations
                    Box horizontalBox = Box.createHorizontalBox();
                    //horizontalBox.setPreferredSize(new Dimension(EspeceInfoPanel.instance.getWidth(), 200));
                    horizontalBox.setPreferredSize(new Dimension(screenSize.width / 5, screenSize.height));
                    //horizontalBox.setPreferredSize(new Dimension(screenSize.width / 8, screenSize.height));

                    JLabel fieldNameLabel = new JLabel(fieldName);
                    JLabel fieldValueLabel = new JLabel(fieldValue);

                    fieldNameLabel.setSize(new Dimension(horizontalBox.getWidth() / 2, 50));
                    fieldValueLabel.setSize(new Dimension(horizontalBox.getWidth() / 2, 50));

                    horizontalBox.add(new JLabel(fieldNameLabel.getText()));
                    horizontalBox.add(Box.createHorizontalGlue());
                    horizontalBox.add(fieldValueLabel);

                    information.put(fieldName, horizontalBox);
                }
                else{
                    //Fait juste modifier le texte
                    Box horizontalBox = information.get(fieldName);
                    JLabel valueLabel = (JLabel) horizontalBox.getComponents()[2];
                    valueLabel.setText(fieldValue);
                }
            }
        }
    }

    private void displayEspeceFields(){

        for(String fieldName : information.keySet()){
            Box informationBox = information.get(fieldName);
            if(informationBox.getParent() != this.boxLayout){
                //L'ajoute a this
                boxLayout.add(informationBox);
            }
        }
    }

    public void setEspece(Espece e){
        this.espece = e;
        try {
            getEspeceFields();
        }catch (Exception error){};

        this.networkPanel.setEspece(espece);

        this.revalidate();
    }
}
