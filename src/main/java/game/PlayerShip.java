package game;

import java.awt.*;

/**
 * PlayerShip controlled by the user.
 */
public class PlayerShip extends GameObject {

    private static final int SPEED = 5;

    /**
     * Constructs the player ship at the given position with default size.
     *
     * @param x The initial x-coordinate.
     * @param y The initial y-coordinate.
     */
    public PlayerShip(int x, int y) {
        super(x, y, 40, 40);
    }

    /**
     * Updates player position based on input flags.
     *
     * @param left  True if moving left.
     * @param right True if moving right.
     * @param up    True if moving up.
     * @param down  True if moving down.
     */
    public void update(boolean left, boolean right, boolean up, boolean down) {
        if (left && x > 0) x -= SPEED;
        if (right && x < 800 - width) x += SPEED;
        if (up && y > 0) y -= SPEED;
        if (down && y < 600 - height) y += SPEED;
    }

    /**
     * Default update (no-op) to satisfy abstract method.
     */
    @Override
    public void update() {
        // No-op: use update with input parameters for player movement
    }

    /**
     * Draws the player ship as a cyan rectangle.
     *
     * @param g Graphics context.
     */
    @Override
    public void draw(Graphics g) {
        g.setColor(Color.CYAN);
        g.fillRect(x, y, width, height);
    }
}
