package org.lrima.Interface.home.pages;

import org.lrima.Interface.FrameManager;
import org.lrima.Interface.home.HomeFrameManager;
import org.lrima.Interface.home.PagePanel;
import org.lrima.core.UserPrefs;
import org.lrima.network.algorithms.AlgorithmManager;
import org.lrima.network.interfaces.NeuralNetworkModel;
import org.lrima.simulation.SimulationManager;

import javax.swing.*;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.GroupLayout.Alignment;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public class OneAlgorithmPanel extends PagePanel {

    private JTextPane descriptionPane = new JTextPane();
    private JComboBox algorithmCombo;
    private JButton configureButton = new JButton("Configure");
    private JButton simulateButton = new JButton("Simulate");
    private JButton backButton = new JButton("Back");

    private NeuralNetworkModel currentModel;
    {
        try {
            //TODO: Crash si il oublie de mettre l'annotation. Ça devrait prendre des valeurs par defaut
            currentModel = AlgorithmManager.algorithms.get(0).getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public OneAlgorithmPanel(HomeFrameManager homeFrameManager){
        super(homeFrameManager);

        this.setupComponents();

        GroupLayout layout = new GroupLayout(this);

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

        setLayout(layout);

    }

    private void setupComponents(){
        //Description
        this.descriptionPane.setText("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce ac lobortis nulla. Donec at turpis sed purus commodo fringilla vel nec ex. Vivamus placerat luctus malesuada. Ut odio dui, lobortis vitae finibus eget, fermentum eu sem. Etiam suscipit augue ut nunc hendrerit, consequat tincidunt lacus porttitor.");
        this.descriptionPane.setEditable(false);


        this.algorithmCombo = new JComboBox(AlgorithmManager.algorithmsName.toArray());

        //Bottom buttons
        this.backButton.addActionListener(e -> homeFrameManager.back());

        //Configure button
        this.configureButton.addActionListener(e -> currentModel.displayOptions());

        //
        this.algorithmCombo.addActionListener(e -> {
            try {
                currentModel = AlgorithmManager.algorithms.get(this.algorithmCombo.getSelectedIndex()).getConstructor().newInstance();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e1) {
                e1.printStackTrace();
            }
        });

        //Simulate button
        this.simulateButton.addActionListener(e -> {
            try {
                homeFrameManager.close();

                ArrayList<NeuralNetworkModel> models = new ArrayList<>();
                models.add(currentModel);

                homeFrameManager.addModelsToSaved(models);

                SimulationManager simulationManager = new SimulationManager();
                int numberOfSimulationPerBatches = (int) UserPrefs.getOption(UserPrefs.KEY_NUMBER_SIMULATION).getValue();
                simulationManager.addBatch(currentModel, numberOfSimulationPerBatches);
                simulationManager.start();

                FrameManager frameManager = new FrameManager(simulationManager);
                frameManager.setVisible(true);
            }catch (Exception error){
                error.printStackTrace();
            }
        });
    }

    @Override
    public String getName() {
        return "Configure algorithm";
    }
}
