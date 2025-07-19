package game;

import java.awt.*;

/**
 * Abstract base class for all game objects.
 * Holds position and size information.
 */
public abstract class GameObject {
    protected int x;
    protected int y;
    protected int width;
    protected int height;

    /**
     * Constructs a game object with given position and size.
     *
     * @param x      The x-coordinate of the object.
     * @param y      The y-coordinate of the object.
     * @param width  The width of the object.
     * @param height The height of the object.
     */
    protected GameObject(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /**
     * Updates the game object's state.
     */
    public abstract void update();

    /**
     * Draws the game object using the provided graphics context.
     *
     * @param g The graphics context to draw on.
     */
    public abstract void draw(Graphics g);

    /**
     * Returns the bounding rectangle of the object, used for collision detection.
     *
     * @return The bounding rectangle.
     */
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    /**
     * Returns the current x-coordinate.
     *
     * @return The x-coordinate.
     */
    public int getX() {
        return x;
    }

    /**
     * Returns the current y-coordinate.
     *
     * @return The y-coordinate.
     */
    public int getY() {
        return y;
    }
}
