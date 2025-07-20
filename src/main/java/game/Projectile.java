package game;

import java.awt.*;

/**
 * Represents a projectile shot by either the player or enemies.
 */
public class Projectile {

    private final int x;
    private int y;
    private final int speed;
    private static final int width = 8;
    private static final int height = 12;
    private final Color color;

    public Projectile(int x, int y, int speed, Color color) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.color = color;
    }

    /**
     * Moves the projectile.
     */
    public void update() {
        y += speed;
    }

    /**
     * Draws the projectile.
     */
    public void draw(Graphics g) {
        g.setColor(color);
        g.fillRect(x, y, width, height);
    }

    /**
     * Returns the bounding box of the projectile.
     */
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public int getY() {
        return y;
    }
}
