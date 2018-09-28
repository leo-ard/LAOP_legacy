package org.lrima.Interface.home.pages;

import org.lrima.Interface.home.HomeFrameManager;
import org.lrima.Interface.utils.ImagePanel;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import java.awt.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

public class HomePanel extends org.lrima.Interface.home.PagePanel {

    private JEditorPane descriptionEVO;
    private ImagePanel lrimaImagePanel;
    private JButton configureButton;
    private JButton runLastButton;

    public HomePanel(HomeFrameManager homeFrameManager){
        super(homeFrameManager);
        GroupLayout layout = new GroupLayout(this);

        this.setupComponents();


        layout.setHorizontalGroup(
                layout.createParallelGroup(Alignment.TRAILING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(Alignment.LEADING)
                                        .addComponent(descriptionEVO, GroupLayout.DEFAULT_SIZE, 422, Short.MAX_VALUE)
                                        .addGroup(Alignment.TRAILING, layout.createSequentialGroup()
                                                .addComponent(runLastButton)
                                                .addPreferredGap(ComponentPlacement.RELATED, 88, Short.MAX_VALUE)
                                                .addComponent(configureButton)))
                                .addContainerGap())
                        .addGroup(layout.createSequentialGroup()
                                .addGap(126)
                                .addComponent(lrimaImagePanel, GroupLayout.DEFAULT_SIZE, 222, Short.MAX_VALUE)
                                .addGap(116))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(15)
                                .addComponent(lrimaImagePanel, GroupLayout.DEFAULT_SIZE, 101, Short.MAX_VALUE)
                                .addGap(18)
                                .addComponent(descriptionEVO, GroupLayout.DEFAULT_SIZE, 92, Short.MAX_VALUE)
                                .addGap(137)
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
        this.configureButton = new JButton("Configure");
        configureButton.addActionListener(e -> {
            ConfigureSimulationPanel configureSimulationPanel = new ConfigureSimulationPanel(homeFrameManager);
            homeFrameManager.next(configureSimulationPanel);
        });

        this.runLastButton = new JButton("Run last simulation");
    }

    @Override
    public String getName() {
        return "Welcome to EVO";
    }
}

