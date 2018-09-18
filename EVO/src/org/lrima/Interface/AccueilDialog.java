/*
 * Created by JFormDesigner on Sun Sep 16 19:44:39 EDT 2018
 */

package org.lrima.Interface;

import org.lrima.espece.network.annotations.MainAlgorithmClass;
import org.lrima.espece.network.interfaces.NeuralNetwork;
import org.lrima.espece.network.interfaces.NeuralNetworkReceiver;
import org.lrima.simulation.Simulation;
import org.reflections.Reflections;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Set;
import javax.swing.*;
import javax.swing.border.*;

/**
 * @author unknown
 */
public class AccueilDialog extends JDialog implements ActionListener {

    private JButton okButton = new JButton("Simulate");
    private JComboBox<String> neuralNetworkComboBox = new JComboBox<>();

    private String[] algorithmsString;
    private Class<?>[] algorithms;
    private FrameManager frameManager;

    private final int FRAME_MARGIN = 20;
    private final int MARGIN_BETWEEN_COMPONENTS = 10;

    /**
     * A dialog that describe the EVO program and lets the user choose which algorithm to use.
     * @param frameManager the frame manager
     */
    public AccueilDialog(FrameManager frameManager) {
        super(frameManager);
        this.frameManager = frameManager;
        initComponents();

        //When the user closes the dialog
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                Class<?extends NeuralNetwork> algorithmChosen = (Class<NeuralNetwork>) algorithms[neuralNetworkComboBox.getSelectedIndex()];
                frameManager.setAlgorithmToUse(algorithmChosen);
            }
        });
    }


    /**
     * Inits the components of the dialog and adds them to the panel
     */
    private void initComponents() {
        JPanel panel = new JPanel();
        this.setResizable(false);
        this.setPreferredSize(new Dimension(500, 300));

        this.okButton.addActionListener(this);

        loadAlgorithms();

        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.rowWeights = new double[]{1.0, 0.0};
        gridBagLayout.columnWeights = new double[]{1.0, 0.0};

        panel.setLayout(gridBagLayout);

        //INTRODUCTION TEXT
        JTextPane introductionText = new JTextPane();
        String text =   "Welcome to EVO, a program that lets you test different artificial intelligence";
        text +=         " algorithms with the task of making a car learn to drive by itself without driving into obstacles.";
        text +=         " This program was developed for LRIMa, click here to learn more";
        introductionText.setText(text);
        introductionText.setEditable(false);
        introductionText.setSize(200, 300);

        GridBagConstraints textPaneGBC = new GridBagConstraints();
        textPaneGBC.gridx = 0;
        textPaneGBC.gridy = 0;
        textPaneGBC.fill = GridBagConstraints.BOTH;
        textPaneGBC.gridwidth = 3;
        textPaneGBC.insets = new Insets(FRAME_MARGIN, FRAME_MARGIN, MARGIN_BETWEEN_COMPONENTS, FRAME_MARGIN);

        panel.add(introductionText, textPaneGBC);

        //ALGORITHM TO USE COMBOBOX AND LABEL
        JLabel algorithmToUseLabel = new JLabel("Algorithm to use:");

        GridBagConstraints algorithmToUseLabelGBC = new GridBagConstraints();
        algorithmToUseLabelGBC.gridx = 0;
        algorithmToUseLabelGBC.gridy = 1;
        algorithmToUseLabelGBC.insets = new Insets(MARGIN_BETWEEN_COMPONENTS, FRAME_MARGIN, MARGIN_BETWEEN_COMPONENTS, MARGIN_BETWEEN_COMPONENTS);

        panel.add(algorithmToUseLabel, algorithmToUseLabelGBC);

        GridBagConstraints algorithmToUseComboBoxGBC = new GridBagConstraints();
        algorithmToUseComboBoxGBC.gridx = 1;
        algorithmToUseComboBoxGBC.gridy = 1;
        algorithmToUseComboBoxGBC.fill = GridBagConstraints.HORIZONTAL;
        algorithmToUseComboBoxGBC.insets = new Insets(MARGIN_BETWEEN_COMPONENTS, MARGIN_BETWEEN_COMPONENTS, MARGIN_BETWEEN_COMPONENTS, FRAME_MARGIN);

        panel.add(this.neuralNetworkComboBox, algorithmToUseComboBoxGBC);

        //SIMULATE BUTTON
        GridBagConstraints okButtonGBC = new GridBagConstraints();

        okButtonGBC.gridy = 2;
        okButtonGBC.gridx = 1;
        okButtonGBC.insets = new Insets(MARGIN_BETWEEN_COMPONENTS, MARGIN_BETWEEN_COMPONENTS, FRAME_MARGIN, FRAME_MARGIN);

        panel.add(okButton, okButtonGBC);

        this.pack();

        this.getContentPane().add(panel);
    }

    /**
     * Search in the algorithms package and find classes with the @MainAlgorithmClass annotation
     * It then add the classes in the JComboBox so that the user can choose which algorithm to use.
     */
    private void loadAlgorithms(){

        //TODO: Fonctionne si on compile dans IDE, mais si on export en jar, le directory n'existe pas !
        File algorithmRoot = new File("EVO/src/org/lrima/espece/network/algorithms");
        File[] algorithms = algorithmRoot.listFiles();
        ArrayList<String> algorithmPackages = new ArrayList<>();

        //Transform all algorithms directories into package notation
        for(File file : algorithms){
            if(file.isDirectory()){
                algorithmPackages.add(file.getAbsolutePath().split("src")[1].replaceAll("[^A-Za-z0-9]", ".").substring(1));
            }
        }

        this.algorithms = new Class<?>[algorithmPackages.size()];
        this.algorithmsString = new String[algorithmPackages.size()];

        int i = 0 ;
        //Tries to find the main class in the packages
        for(String algorithmPackage : algorithmPackages) {
            Reflections reflections = new Reflections(algorithmPackage);
            Set<Class<?>> algorithmClasses = reflections.getTypesAnnotatedWith(MainAlgorithmClass.class);

            if(algorithmClasses.size() == 1) {
                for(Class<?> algorithmClass : algorithmClasses) {
                    this.algorithms[i] = algorithmClass;
                    this.algorithmsString[i] = algorithmClass.getAnnotation(MainAlgorithmClass.class).name();
                }
            }
            else if(algorithmClasses.size() == 0){
                System.err.println("You don't have a class with the annotation @MainAlgorithmClass in the package " + algorithmPackage + " !");
            }
            else{
                System.err.println("You need only one class with the annotation @MainAlgorithmClass in the package " + algorithmPackage + " !");
            }
            i++;
        }

        //resets the combo box with the new information
        this.neuralNetworkComboBox = new JComboBox<>(this.algorithmsString);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == this.okButton){
            Class<?extends NeuralNetwork> algorithmChosen = (Class<NeuralNetwork>) this.algorithms[neuralNetworkComboBox.getSelectedIndex()];
            this.frameManager.setAlgorithmToUse(algorithmChosen);
            this.dispose();
        }
    }
}
