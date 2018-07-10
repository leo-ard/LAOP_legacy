package org.lrima.map.Studio.Drawables;

import javax.swing.*;
import java.awt.*;

public class End extends Square {
    public End(){
        super(Obstacle.TYPE_END);
        isSquare = true;
        icon = new ImageIcon(getClass().getResource("/images/icons/Map_Studio/finish.gif"));
        this.rect = new Rectangle();
    }

    @Override
    public void draw(Graphics2D g) {
        g.drawImage(icon.getImage(), position.x, position.y, null);
    }
}
