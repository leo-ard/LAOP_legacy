package org.lrima.Interface.home;

import org.lrima.Interface.options.OptionsDialog;

import javax.swing.*;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.GroupLayout.Alignment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ConfigureSimulationFrame extends JFrame {

    private JLabel header = new JLabel("Configure the simulation");
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
        this.setBounds(Constant.bounds);
        this.setTitle("Configure the simulation");
        this.setContentPane(content);

        this.configureComponents();

        GroupLayout layout = new GroupLayout(this.getContentPane());
        layout.setHorizontalGroup(
                layout.createParallelGroup(Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(backButton)
                                .addPreferredGap(ComponentPlacement.RELATED, 247, Short.MAX_VALUE)
                                .addComponent(nextButton, GroupLayout.DEFAULT_SIZE, 75, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(simulationOptionButton, GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
                                .addContainerGap())
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(Alignment.LEADING)
                                        .addComponent(descriptionTextPane)
                                        .addComponent(compareAlgorithmRadio)
                                        .addComponent(testOneAlgorithmRadio))
                                .addContainerGap())
                        .addGroup(layout.createSequentialGroup()
                                .addGap(145)
                                .addComponent(header, GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
                                .addGap(145))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(Alignment.TRAILING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(13)
                                .addComponent(header)
                                .addGap(21)
                                .addComponent(descriptionTextPane, GroupLayout.PREFERRED_SIZE, 165, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(ComponentPlacement.UNRELATED)
                                .addComponent(testOneAlgorithmRadio)
                                .addPreferredGap(ComponentPlacement.RELATED)
                                .addComponent(compareAlgorithmRadio)
                                .addGap(29)
                                .addComponent(simulationOptionButton)
                                .addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(Alignment.BASELINE)
                                        .addComponent(nextButton)
                                        .addComponent(backButton))
                                .addContainerGap())
        );

        this.getContentPane().setLayout(layout);
        //this.pack();
    }

    private void configureComponents(){
        this.header.setHorizontalTextPosition(SwingConstants.CENTER);

        //Description pane
        this.descriptionTextPane = new JTextPane();
        descriptionTextPane.setEditable(false);
        descriptionTextPane.setText("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce ac lobortis nulla. Donec at turpis sed purus commodo fringilla vel nec ex. Vivamus placerat luctus malesuada. Ut odio dui, lobortis vitae finibus eget, fermentum eu sem. Etiam suscipit augue ut nunc hendrerit, consequat tincidunt lacus porttitor.");


        //Radio buttons
        this.radioGroup = new ButtonGroup();
        this.testOneAlgorithmRadio = new JRadioButton("I want to test one algorithm.");
        testOneAlgorithmRadio.setSelected(true);
        this.compareAlgorithmRadio = new JRadioButton("I want to compare multiple algorithms.");
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
