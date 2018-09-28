package org.lrima.Interface.home;

import org.lrima.Interface.FrameManager;
import org.lrima.Interface.options.OptionsDialog;
import org.lrima.network.algorithms.AlgorithmManager;
import org.lrima.network.annotations.AlgorithmInformation;
import org.lrima.network.interfaces.NeuralNetwork;
import org.lrima.network.interfaces.NeuralNetworkModel;
import org.lrima.simulation.Simulation;
import org.lrima.simulation.SimulationBatch;

import javax.swing.*;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.GroupLayout.Alignment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;

public class OneAlgorithmFrame extends JFrame {

    private JTextPane descriptionPane = new JTextPane();
    private JComboBox algorithmCombo;
    private JButton configureButton = new JButton("Configure");
    private JButton simulateButton = new JButton("Simulate");
    private JButton backButton = new JButton("Back");

    private JPanel content = new JPanel();
    private JFrame lastFrame;

    private NeuralNetworkModel currentModel;
    {
        try {
            currentModel = AlgorithmManager.algorithms.get(0).getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public OneAlgorithmFrame(JFrame lastFrame){
        this.lastFrame = lastFrame;

        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setBounds(lastFrame.getBounds().x, lastFrame.getBounds().y, Constant.FRAME_WIDTH, Constant.FRAME_HEIGHT);
        this.setTitle("Algorithm Options");
        this.setContentPane(content);
        this.setResizable(false);

        this.setupComponents();

        GroupLayout layout = new GroupLayout(content);

        layout.setHorizontalGroup(
                layout.createParallelGroup(Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(Alignment.LEADING)
                                        .addGroup(Alignment.TRAILING, layout.createSequentialGroup()
                                                .addComponent(algorithmCombo, 0, 238, Short.MAX_VALUE)
                                                .addPreferredGap(ComponentPlacement.UNRELATED)
                                                .addComponent(configureButton))
                                        .addComponent(descriptionPane, GroupLayout.DEFAULT_SIZE, 382, Short.MAX_VALUE)
                                        .addGroup(Alignment.TRAILING, layout.createSequentialGroup()
                                                .addComponent(backButton)
                                                .addPreferredGap(ComponentPlacement.RELATED, 128, Short.MAX_VALUE)
                                                .addComponent(simulateButton)))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(descriptionPane, GroupLayout.PREFERRED_SIZE, 105, GroupLayout.PREFERRED_SIZE)
                                .addGap(47)
                                .addGroup(layout.createParallelGroup(Alignment.BASELINE)
                                        .addComponent(algorithmCombo, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(configureButton))
                                .addPreferredGap(ComponentPlacement.RELATED, 265, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(Alignment.BASELINE)
                                        .addComponent(simulateButton)
                                        .addComponent(backButton))
                                .addContainerGap())
        );

        this.getContentPane().setLayout(layout);

    }

    private void setupComponents(){
        //Description
        this.descriptionPane.setText("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce ac lobortis nulla. Donec at turpis sed purus commodo fringilla vel nec ex. Vivamus placerat luctus malesuada. Ut odio dui, lobortis vitae finibus eget, fermentum eu sem. Etiam suscipit augue ut nunc hendrerit, consequat tincidunt lacus porttitor.");
        this.descriptionPane.setEditable(false);


        this.algorithmCombo = new JComboBox(AlgorithmManager.algorithmsName.toArray());

        //Bottom buttons
        this.backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lastFrame.setVisible(true);
                setVisible(false);
            }
        });

        //Configure button
        this.configureButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentModel.displayOptions();
            }
        });

        //
        this.algorithmCombo.addActionListener(e -> {
            try {
                currentModel = AlgorithmManager.algorithms.get(this.algorithmCombo.getSelectedIndex()).getConstructor().newInstance();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e1) {
                e1.printStackTrace();
            }
        });

        //Simulate button
        this.simulateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    dispose();

                    FrameManager frameManager = new FrameManager();
                    frameManager.addBatch(currentModel, 2);
                    frameManager.setVisible(true);
                    frameManager.startBatches();
                }catch (Exception error){
                    error.printStackTrace();
                }
            }
        });
    }
}
