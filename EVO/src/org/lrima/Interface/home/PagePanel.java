package org.lrima.Interface.home;

import javax.swing.*;

public abstract class PagePanel extends JPanel{
    protected HomeFrameManager homeFrameManager;

    public PagePanel(HomeFrameManager homeFrameManager){
        this.homeFrameManager = homeFrameManager;
    }

    public abstract String getName();
}
