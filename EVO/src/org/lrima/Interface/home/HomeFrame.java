package org.lrima.Interface.home;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import java.awt.*;

public class HomeFrame extends JFrame {

    private final Dimension screenDimension = Toolkit.getDefaultToolkit().getScreenSize();
    private JPanel content;
    private JEditorPane descriptionEVO;

    private final int FRAME_WIDTH = screenDimension.width / 3;
    private final int FRAME_HEIGHT = (int)(screenDimension.height / 1.5);

    public HomeFrame(){
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setBounds(screenDimension.width / 2 - FRAME_WIDTH / 2, screenDimension.height / 2 - FRAME_HEIGHT / 2, FRAME_WIDTH, FRAME_HEIGHT);

        this.content = new JPanel();
        this.setContentPane(content);

        GroupLayout layout = new GroupLayout(content);

        this.setupComponents();


        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(descriptionEVO, GroupLayout.DEFAULT_SIZE, 422, Short.MAX_VALUE)
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(134)
                                .addComponent(descriptionEVO, GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE)
                                .addGap(175))
        );

        this.setLayout(layout);
    }

    private void setupComponents(){
        this.setupDescription();
    }

    private void setupDescription(){
        descriptionEVO = new JTextPane();
        descriptionEVO.addHyperlinkListener(new HyperlinkListener() {
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
        descriptionEVO.setEditorKit(JEditorPane.createEditorKitForContentType("text/html"));
        String text =   "Welcome to EVO, a program that lets you test different artificial intelligence";
        text +=         " algorithms with the task of making a car learn to drive by itself without driving into obstacles.";
        text +=         " This program was developed for LRIMa, <a href=\"http://www-etud.iro.umontreal.ca/~rezguiji/lrima.php\">click here to learn more</a>";
        descriptionEVO.setText(text);
        descriptionEVO.setEditable(false);
    }
}

