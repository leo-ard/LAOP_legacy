package org.lrima.map.Studio.Drawables;

import org.lrima.espece.Espece;

import java.awt.*;

public abstract class Square extends Obstacle {

    public Rectangle rect;

    public Square(String type){
        super(type);
    }

    public Square(Obstacle obstacle) {
        super(obstacle);
        rect = ((Square)obstacle).rect;
    }

    @Override
    public void move(Point newPos) {
        super.move(newPos);
        this.rect = new Rectangle(newPos.x, newPos.y, icon.getIconWidth(), icon.getIconHeight());
    }

    @Override
    public boolean collisionWithRect(Espece e) {
        return false;
    }
}
