package org.lrima.Interface;

import org.lrima.annotations.DisplayInfo;
import org.lrima.espece.Espece;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.util.HashMap;

//TODO: Improve the way this class works
public class EspeceInfoPanel extends JPanel {

    public static EspeceInfoPanel instance = null;
    private static HashMap<String, Box> information;

    private static Box boxLayout;

    private static Dimension screenSize;

    public EspeceInfoPanel(){
        if(instance == null){
            instance = this;
        }
        else{
            return;
        }

        boxLayout = Box.createVerticalBox();

        screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBackground(Color.WHITE);
        //setSize(600, screenSize.height);
        setPreferredSize(new Dimension(screenSize.width / 5, screenSize.height));

        add(boxLayout, BorderLayout.NORTH);

        load();
    }

    static private void load(){
        information = new HashMap<>();
        try {
            getEspeceFields(null);
        }catch (Exception e){}
        displayEspeceFields();
    }

    static public void update(Espece selected){
        //EspeceInfoPanel.boxLayout.removeAll();

        try {
            getEspeceFields(selected);
        }catch (Exception e){}
        EspeceInfoPanel.instance.revalidate();
    }

    /**
     * Va chercher toutes les informations de l'espece
     * @param e l'espece
     */
    static private void getEspeceFields(Espece e) throws IllegalAccessException{
        Class<?> especeClass = Espece.class;

        for(Field field : especeClass.getDeclaredFields()){
            if(field.isAnnotationPresent(DisplayInfo.class)){
                String fieldName = null, fieldValue = null;

                fieldValue = "Select a car";
                fieldName = field.getName();
                field.setAccessible(true);

                if(e != null) {
                    String value = field.get(e).toString();

                    if(field.get(e) instanceof Double || field.get(e) instanceof Float){
                        value = new DecimalFormat("#.##").format(field.get(e));
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

    static private void displayEspeceFields(){

        for(String fieldName : information.keySet()){
            Box informationBox = information.get(fieldName);
            if(informationBox.getParent() != EspeceInfoPanel.boxLayout){
                //L'ajoute a this
                boxLayout.add(informationBox);
            }
        }
    }
}
