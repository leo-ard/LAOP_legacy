package org.lrima.Interface.home;


import org.lrima.Interface.home.pages.HomePanel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class HomeFrameManager {
    public static final Dimension screenDimension = Toolkit.getDefaultToolkit().getScreenSize();
    public static final int FRAME_WIDTH = screenDimension.width / 3;
    public static final int FRAME_HEIGHT = (int)(screenDimension.height / 1.5);


    private ArrayList<PagePanel> history = new ArrayList<>();
    private int currentIndex;
    private JFrame mainFrame;

    public HomeFrameManager(){
        mainFrame = new JFrame("Welcome to EVO");
        mainFrame.setLayout(new BorderLayout());
        mainFrame.setResizable(true);
        mainFrame.setBounds(new Rectangle(0, 0, FRAME_WIDTH, FRAME_HEIGHT));
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);

        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        currentIndex = -1;
        next(new HomePanel(this));
    }

    public void next(PagePanel page){
        currentIndex++;

        //Remove all pages that came after the one that we are now going to
        while(history.size() > this.currentIndex){
            history.remove(history.size()-1);
        }

        history.add(page);
        setPanelByIndex();
    }

    private void setPanelByIndex(){
        mainFrame.setContentPane(history.get(currentIndex));
        mainFrame.setTitle(history.get(currentIndex).getName());
    }

    public void back() {
        currentIndex--;
        setPanelByIndex();
    }

    public void close(){
        mainFrame.dispose();
    }


}
