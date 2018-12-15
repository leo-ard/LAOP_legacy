package org.lrima.Interface.home.pages;

import org.lrima.Interface.FrameManager;
import org.lrima.Interface.home.HomeFrameManager;
import org.lrima.Interface.utils.ImagePanel;
import org.lrima.core.UserPrefs;
import org.lrima.map.Studio.Studio;
import org.lrima.network.interfaces.NeuralNetworkModel;
import org.lrima.simulation.SimulationManager;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

public class HomePanel extends org.lrima.Interface.home.PagePanel {

    private JEditorPane descriptionEVO;
    private ImagePanel lrimaImagePanel;
    private JButton configureButton;
    private JButton runLastButton;
    private JButton mapStudioButton;

    private HomeFrameManager homeFrameManager;

    public HomePanel(HomeFrameManager homeFrameManager){
        super(homeFrameManager);
        this.homeFrameManager = homeFrameManager;
        GroupLayout layout = new GroupLayout(this);

        this.setupComponents();


        layout.setHorizontalGroup(
                layout.createParallelGroup(Alignment.TRAILING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(Alignment.TRAILING)
                                        .addComponent(descriptionEVO)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(runLastButton)
                                                .addPreferredGap(ComponentPlacement.RELATED, 142, Short.MAX_VALUE)
                                                .addGroup(layout.createParallelGroup(Alignment.LEADING, false)
                                                        .addComponent(mapStudioButton, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(configureButton, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                                .addContainerGap())
                        .addGroup(layout.createSequentialGroup()
                                .addGap(126)
                                .addComponent(lrimaImagePanel, GroupLayout.DEFAULT_SIZE, 132, Short.MAX_VALUE)
                                .addGap(116))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(15)
                                .addComponent(lrimaImagePanel, GroupLayout.DEFAULT_SIZE, 106, Short.MAX_VALUE)
                                .addGap(18)
                                .addComponent(descriptionEVO, GroupLayout.DEFAULT_SIZE, 97, Short.MAX_VALUE)
                                .addGap(90)
                                .addComponent(mapStudioButton)
                                .addPreferredGap(ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(Alignment.TRAILING, false)
                                        .addComponent(runLastButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(configureButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap())
        );

        this.setLayout(layout);
    }

    private void setupComponents(){
        this.setupDescription();
        this.setupLrimaImage();
        this.setupButtons();
    }

    private void setupDescription(){
        descriptionEVO = new JTextPane();
        descriptionEVO.addHyperlinkListener(e -> {
            if(e.getEventType() == HyperlinkEvent.EventType.ACTIVATED){
                if(Desktop.isDesktopSupported()){
                    try{
                        Desktop.getDesktop().browse(e.getURL().toURI());
                    }catch (Exception error){
                        error.printStackTrace();
                    }

                }
            }
        });
        descriptionEVO.setEditorKit(JEditorPane.createEditorKitForContentType("text/html"));
        String text =   "Welcome to EVO, a program that lets you test different artificial intelligence";
        text +=         " algorithms with the task of making a car learn to drive by itself without driving into obstacles.";
        text +=         " This program was developed for LRIMa, <a href=\"http://www-etud.iro.umontreal.ca/~rezguiji/lrima.php\">click here to learn more</a>";
        descriptionEVO.setText(text);
        descriptionEVO.setEditable(false);
    }

    private void setupLrimaImage(){
        this.lrimaImagePanel = new ImagePanel("/images/LRIMA.png");
    }

    private void setupButtons(){
        this.configureButton = new JButton("Configure Simulation");
        configureButton.addActionListener(e -> {
            ConfigureSimulationPanel configureSimulationPanel = new ConfigureSimulationPanel(homeFrameManager);
            homeFrameManager.next(configureSimulationPanel);
        });

        this.runLastButton = new JButton("Run last simulation");
        runLastButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<NeuralNetworkModel> savedModels = homeFrameManager.getSavedModels();
                if(savedModels != null){
                    homeFrameManager.close();

                    SimulationManager simulationManager = new SimulationManager();
                    for(NeuralNetworkModel model : savedModels){
                        int numberOfSimulationPerBatches = (int) UserPrefs.getOption(UserPrefs.KEY_NUMBER_SIMULATION).getValue();
                        simulationManager.addBatch(model, numberOfSimulationPerBatches);
                    }

                    simulationManager.start();

                    FrameManager frameManager = new FrameManager(simulationManager);
                    frameManager.setVisible(true);
                }
                else{
                    JOptionPane.showMessageDialog(HomePanel.this, "The file containing the last simulation doesn't exist. Try to run a simulation before.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        this.mapStudioButton = new JButton("Map Studio");
        this.mapStudioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Studio studio = new Studio();
                studio.setVisible(true);
            }
        });
    }

    @Override
    public String getName() {
        return "Welcome to EVO";
    }
}

