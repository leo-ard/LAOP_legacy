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
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Set;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.EditorKit;

/**
 * @author unknown
 */
public class AccueilDialog extends JDialog implements ActionListener, ItemListener {

    private JButton okButton = new JButton("Simulate");
    JTextPane algorithmDescriptionPane;
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
        this.setTitle("Welcome to EVO");
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
        this.neuralNetworkComboBox.addItemListener(this);
    }


    /**
     * Inits the components of the dialog and adds them to the panel
     */
    private void initComponents() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension panelSize = new Dimension((int)(screenSize.width / 2.5), screenSize.height / 2);

        JPanel panel = new JPanel();
        this.setResizable(false);
        this.setPreferredSize(new Dimension(panelSize.width, panelSize.height));
        this.setBounds(screenSize.width / 2 - panelSize.width / 2, screenSize.height / 2 - panelSize.height / 2, panelSize.width, panelSize.height);

        this.okButton.addActionListener(this);

        loadAlgorithms();

        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.rowWeights = new double[]{1.0, 0.0};
        gridBagLayout.columnWeights = new double[]{1.0, 0.0};

        panel.setLayout(gridBagLayout);

        //INTRODUCTION TEXT
        JEditorPane introductionText = new JTextPane();
        introductionText.addHyperlinkListener(new HyperlinkListener() {
            @Override
            public void hyperlinkUpdate(HyperlinkEvent e) {
                if(e.getEventType() == HyperlinkEvent.EventType.ACTIVATED){
                    if(Desktop.isDesktopSupported()){
                        try{
                            Desktop.getDesktop().browse(e.getURL().toURI());
                        }catch (Exception error){
                            error.printStackTrace();
                        }

                    }
                }
            }
        });

        introductionText.setEditorKit(JEditorPane.createEditorKitForContentType("text/html"));
        String text =   "Welcome to EVO, a program that lets you test different artificial intelligence";
        text +=         " algorithms with the task of making a car learn to drive by itself without driving into obstacles.";
        text +=         " This program was developed for LRIMa, <a href=\"http://www-etud.iro.umontreal.ca/~rezguiji/lrima.php\">click here to learn more</a>";
        introductionText.setText(text);
        introductionText.setEditable(false);

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

        //Algorithm description
        algorithmDescriptionPane = new JTextPane();
        String descriptionText = this.algorithms[neuralNetworkComboBox.getSelectedIndex()].getAnnotation(MainAlgorithmClass.class).description();
        this.algorithmDescriptionPane.setText(descriptionText);
        algorithmDescriptionPane.setEditable(false);

        GridBagConstraints algorithmDescriotionGBC = new GridBagConstraints();
        algorithmDescriotionGBC.gridx = 0;
        algorithmDescriotionGBC.gridy = 2;
        algorithmDescriotionGBC.gridwidth = 3;
        algorithmDescriotionGBC.insets = new Insets(MARGIN_BETWEEN_COMPONENTS, FRAME_MARGIN, MARGIN_BETWEEN_COMPONENTS, FRAME_MARGIN);
        algorithmDescriotionGBC.fill = GridBagConstraints.BOTH;

        panel.add(algorithmDescriptionPane, algorithmDescriotionGBC);

        //SIMULATE BUTTON
        GridBagConstraints okButtonGBC = new GridBagConstraints();

        okButtonGBC.gridy = 3;
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

    @Override
    public void itemStateChanged(ItemEvent e) {
        if(e.getStateChange() == ItemEvent.SELECTED){
            String descriptionText = this.algorithms[neuralNetworkComboBox.getSelectedIndex()].getAnnotation(MainAlgorithmClass.class).description();
            this.algorithmDescriptionPane.setText(descriptionText);
        }
    }
}
