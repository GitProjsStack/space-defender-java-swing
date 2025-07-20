package game;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

/**
 * Explosion animation used when an enemy or the player is destroyed.
 */
public class Explosion {

    private static final int FRAME_COUNT = 7;
    private static final BufferedImage[] FRAMES = new BufferedImage[FRAME_COUNT];

    private final int x;
    private final int y;
    private int currentFrame = 0;
    private static final int frameDelay = 5; // Adjust for slower/faster animation
    private int frameTimer = 0;
    private boolean finished = false;

    static {
        for (int i = 0; i < FRAME_COUNT; i++) {
            try {
                FRAMES[i] = ImageIO.read(Objects.requireNonNull(
                        Explosion.class.getResourceAsStream("/images/explosionImgs/explosion" + (i + 1) + ".png")
                ));
            } catch (IOException | IllegalArgumentException e) {
                System.err.println("Failed to load explosion" + (i + 1) + ".png");
            }
        }
    }

    /**
     * Constructs an explosion at the given coordinates.
     * @param x The X coordinate of the explosion center.
     * @param y The Y coordinate of the explosion center.
     */
    public Explosion(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Updates the explosion animation frame.
     */
    public void update() {
        frameTimer++;
        if (frameTimer >= frameDelay) {
            frameTimer = 0;
            currentFrame++;
            if (currentFrame >= FRAME_COUNT) {
                finished = true;
            }
        }
    }

    /**
     * Draws the current frame of the explosion animation.
     * @param g The graphics context.
     */
    public void draw(Graphics g) {
        if (!finished && currentFrame < FRAME_COUNT && FRAMES[currentFrame] != null) {
            g.drawImage(FRAMES[currentFrame], x, y, 40, 40, null); // 40x40 explosion size
        }
    }

    /**
     * @return true if the explosion has completed all frames.
     */
    public boolean isFinished() {
        return finished;
    }
}
