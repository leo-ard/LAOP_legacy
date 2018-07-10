package org.lrima.map.Studio.Drawables;


import org.omg.CORBA.Bounds;

import javax.swing.*;
import java.awt.*;

public class Start extends Square implements Drawable {


    public Start(){
        super(Obstacle.TYPE_START);
        isSquare = true;
        icon = new ImageIcon(getClass().getResource("/images/icons/Map_Studio/start.gif"));
        this.rect = new Rectangle();
    }

    @Override
    public void draw(Graphics2D g) {
        g.drawImage(icon.getImage(), position.x, position.y, null);
    }
}
