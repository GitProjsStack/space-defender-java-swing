package game;

import java.awt.*;

/**
 * Represents a projectile (bullet) fired by an enemy.
 */
public class Projectile extends GameObject {

    private static final int SPEED = 5;

    public Projectile(int x, int y) {
        super(x, y, 5, 10); // Thin vertical bullet
    }

    @Override
    public void update() {
        y += SPEED;
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.YELLOW);
        g.fillRect(x, y, width, height);
    }
}
