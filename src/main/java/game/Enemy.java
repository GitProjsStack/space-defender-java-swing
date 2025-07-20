package game;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.Objects;

/**
 * Enemy class representing falling enemies in the game.
 */
public class Enemy extends GameObject {

    private static final int[] SPEEDS = {2, 2, 3, 3, 4, 4, 5};
    private static final BufferedImage[] IMAGES = new BufferedImage[7];

    private final int level;
    private int shootCooldown = 0;

    static {
        for (int i = 0; i < IMAGES.length; i++) {
            try {
                IMAGES[i] = ImageIO.read(
                        Objects.requireNonNull(
                                Enemy.class.getResourceAsStream("/images/enemyImgs/enemy" + (i + 1) + ".png")
                        )
                );
            } catch (IOException | IllegalArgumentException e) {
                System.err.println("Failed to load enemy" + (i + 1) + ".png");
            }
        }
    }

    public Enemy(int x, int y, int level) {
        super(x, y, 40, 40);
        this.level = Math.max(1, Math.min(level, 7)) - 1; // clamp level 0-6 for array index
    }

    @Override
    public void update() {
        y += SPEEDS[level];
        if (shootCooldown > 0) shootCooldown--;
    }

    @Override
    public void update(boolean left, boolean right, boolean up, boolean down) {
        // no implementation required for Enemy
    }

    @Override
    public void draw(Graphics g) {
        if (IMAGES[level] != null) {
            g.drawImage(IMAGES[level], x, y, width, height, null);
        } else {
            g.setColor(Color.RED);
            g.fillOval(x, y, width, height);
        }
    }

    /**
     * Returns true if enemy can shoot (cooldown elapsed).
     */
    public boolean canShoot() {
        return shootCooldown <= 0;
    }

    /**
     * Enemy shoots a downward projectile.
     *
     * @return a new Projectile moving downwards from enemy's center bottom.
     */
    public Projectile shoot() {
        shootCooldown = 60 + (int)(Math.random() * 60); // Random cooldown 60-120 frames
        int projX = x + width / 2 - 4;  // Adjust projectile x-position
        int projY = y + height;
        return new Projectile(projX, projY, 8, 12, 5, Color.RED); // Enemy projectile: red, size 8x12, speed 5 downward
    }
}
