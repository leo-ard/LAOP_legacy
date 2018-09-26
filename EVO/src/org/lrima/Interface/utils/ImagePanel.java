package org.lrima.Interface.utils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class ImagePanel extends JPanel {

    private BufferedImage image;

    public ImagePanel(String path){
        try{
            this.image = ImageIO.read(ImagePanel.class.getResource(path));
            this.setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(this.image, 0, 0, null);
    }
}
