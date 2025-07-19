package game;

import java.awt.*;

/**
 * Projectile fired by player or enemies.
 */
public class Projectile extends GameObject {

    private final int speed;
    private final Color color;

    public Projectile(int x, int y, int width, int height, int speed, Color color) {
        super(x, y, width, height);
        this.speed = speed;
        this.color = color;
    }

    @Override
    public void update() {
        y -= speed; // Move upward for player projectile
    }

    @Override
    public void update(boolean left, boolean right, boolean up, boolean down) {
        // no implementation needed
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(color);
        g.fillOval(x, y, width, height);
    }
}
