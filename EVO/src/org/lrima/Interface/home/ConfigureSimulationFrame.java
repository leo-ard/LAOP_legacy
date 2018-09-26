package org.lrima.Interface.home;

import org.lrima.Interface.options.OptionsDialog;

import javax.swing.*;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.GroupLayout.Alignment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ConfigureSimulationFrame extends JFrame {

    private JTextPane descriptionTextPane;
    private JRadioButton testOneAlgorithmRadio;
    private JRadioButton compareAlgorithmRadio;
    private ButtonGroup radioGroup;
    private JButton simulationOptionButton;
    private JButton nextButton;
    private JButton backButton;

    private JPanel content = new JPanel();
    private JFrame lastFrame;

    public ConfigureSimulationFrame(JFrame lastFrame){
        this.lastFrame = lastFrame;

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setBounds(lastFrame.getBounds().x, lastFrame.getBounds().y, Constant.FRAME_WIDTH, Constant.FRAME_HEIGHT);
        this.setTitle("Configure the simulation");
        this.setContentPane(content);
        this.setResizable(false);


        this.configureComponents();

        GroupLayout layout = new GroupLayout(this.getContentPane());
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

        this.getContentPane().setLayout(layout);
        //this.pack();
    }

    private void configureComponents(){
        //Description pane
        this.descriptionTextPane = new JTextPane();
        descriptionTextPane.setEditable(false);
        descriptionTextPane.setText("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce ac lobortis nulla. Donec at turpis sed purus commodo fringilla vel nec ex. Vivamus placerat luctus malesuada. Ut odio dui, lobortis vitae finibus eget, fermentum eu sem. Etiam suscipit augue ut nunc hendrerit, consequat tincidunt lacus porttitor.");


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
                    OneAlgorithmFrame oneAlgorithmFrame = new OneAlgorithmFrame(ConfigureSimulationFrame.this);
                    oneAlgorithmFrame.setVisible(true);
                    setVisible(false);
                }
                else if(compareAlgorithmRadio.isSelected()){
                    CompareAlgorithmsFrame compareAlgorithmsFrame = new CompareAlgorithmsFrame(ConfigureSimulationFrame.this);
                    compareAlgorithmsFrame.setVisible(true);
                    setVisible(false);
                }
            }
        });

        this.backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lastFrame.setVisible(true);
                setVisible(false);
            }
        });
    }
}
