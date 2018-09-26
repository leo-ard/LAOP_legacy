package org.lrima.Interface.utils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImagePanel extends JPanel {

    private BufferedImage image;

    public ImagePanel(String path){
        try{
            this.image = ImageIO.read(new File(path));
            //this.
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void paintComponents(Graphics g) {
        super.paintComponents(g);
        g.drawImage(this.image, 0, 0, null);
    }
}
