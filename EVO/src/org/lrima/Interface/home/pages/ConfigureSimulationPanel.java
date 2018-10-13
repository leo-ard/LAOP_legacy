package org.lrima.Interface.home.pages;

import org.lrima.Interface.home.HomeFrameManager;
import org.lrima.Interface.home.PagePanel;
import org.lrima.Interface.options.OptionsDialog;

import javax.swing.*;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.GroupLayout.Alignment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ConfigureSimulationPanel extends PagePanel {

    private JTextPane descriptionTextPane;
    private JRadioButton testOneAlgorithmRadio;
    private JRadioButton compareAlgorithmRadio;
    private ButtonGroup radioGroup;
    private JButton simulationOptionButton;
    private JButton nextButton;
    private JButton backButton;

    public ConfigureSimulationPanel(HomeFrameManager homeFrameManager) {
        super(homeFrameManager);
        this.configureComponents();

        GroupLayout layout = new GroupLayout(this);
        layout.setHorizontalGroup(
                layout.createParallelGroup(Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(Alignment.LEADING)
                                        .addComponent(compareAlgorithmRadio)
                                        .addComponent(testOneAlgorithmRadio)
                                        .addComponent(descriptionTextPane, GroupLayout.DEFAULT_SIZE, 382, Short.MAX_VALUE)
                                        .addComponent(simulationOptionButton, GroupLayout.DEFAULT_SIZE, 382, Short.MAX_VALUE)
                                        .addGroup(Alignment.TRAILING, layout.createSequentialGroup()
                                                .addComponent(backButton)
                                                .addPreferredGap(ComponentPlacement.RELATED, 166, Short.MAX_VALUE)
                                                .addComponent(nextButton)))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(descriptionTextPane, GroupLayout.PREFERRED_SIZE, 118, GroupLayout.PREFERRED_SIZE)
                                .addGap(18)
                                .addComponent(testOneAlgorithmRadio)
                                .addPreferredGap(ComponentPlacement.RELATED)
                                .addComponent(compareAlgorithmRadio)
                                .addGap(18)
                                .addComponent(simulationOptionButton)
                                .addPreferredGap(ComponentPlacement.RELATED, 189, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(Alignment.BASELINE)
                                        .addComponent(nextButton)
                                        .addComponent(backButton))
                                .addContainerGap())
        );

        setLayout(layout);
        //this.pack();
    }

    private void configureComponents(){
        //Description pane
        this.descriptionTextPane = new JTextPane();
        descriptionTextPane.setEditable(false);
        descriptionTextPane.setText("To configure a simulation we have to know if you want to only test one algorithm or if you want to compare multiple algorithms together.");


        //Radio buttons
        this.radioGroup = new ButtonGroup();
        this.testOneAlgorithmRadio = new JRadioButton("I want to test one algorithm.");
        //testOneAlgorithmRadio.setSelected(true);
        this.compareAlgorithmRadio = new JRadioButton("I want to compare multiple algorithms.");
        compareAlgorithmRadio.setSelected(true);
        this.radioGroup.add(testOneAlgorithmRadio);
        this.radioGroup.add(compareAlgorithmRadio);

        //Simulation options button
        this.simulationOptionButton = new JButton("Simulation Settings");
        simulationOptionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                OptionsDialog optionsDialog = new OptionsDialog();
                optionsDialog.setVisible(true);
            }
        });

        //Bottom buttons
        this.nextButton = new JButton("Next");
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(testOneAlgorithmRadio.isSelected()){
                    OneAlgorithmPanel oneAlgorithmPanel = new OneAlgorithmPanel(homeFrameManager);
                    homeFrameManager.next(oneAlgorithmPanel);
                }
                else if(compareAlgorithmRadio.isSelected()){
                    CompareAlgorithmsPanel compareAlgorithmsPanel = new CompareAlgorithmsPanel(homeFrameManager);
                    homeFrameManager.next(compareAlgorithmsPanel);
                }
            }
        });

        this.backButton = new JButton("Back");
        backButton.addActionListener(e -> homeFrameManager.back());
    }

    @Override
    public String getName() {
        return "Configure the simulation";
    }
}
