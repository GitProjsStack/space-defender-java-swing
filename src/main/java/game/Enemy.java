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

    private final int maxHealth;
    private int currentHealth;

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
        this.level = Math.max(1, Math.min(level, 7)) - 1;
        this.maxHealth = 20 + this.level * 10;
        this.currentHealth = maxHealth;
    }

    @Override
    public void update() {
        y += SPEEDS[level];
        if (shootCooldown > 0) shootCooldown--;
    }

    @Override
    public void update(boolean left, boolean right, boolean up, boolean down) {
        // Enemy movement not controlled by input
    }

    @Override
    public void draw(Graphics g) {
        if (IMAGES[level] != null) {
            g.drawImage(IMAGES[level], x, y, width, height, null);
        } else {
            g.setColor(Color.RED);
            g.fillOval(x, y, width, height);
        }

        drawHealthBar(g);
    }

    private void drawHealthBar(Graphics g) {
        int barWidth = 40;
        int barHeight = 5;
        int barX = x;
        int barY = y - 8;

        HealthBarRenderer.drawHealthBar(g, barX, barY, barWidth, barHeight, currentHealth, maxHealth);
    }

    public boolean canShoot() {
        return shootCooldown <= 0;
    }

    public Projectile shoot() {
        shootCooldown = 60 + (int)(Math.random() * 60);
        int projX = x + width / 2 - 4;
        int projY = y + height;
        return new Projectile(projX, projY, 5, Color.RED);
    }

    public void takeDamage(int damage) {
        currentHealth -= damage;
    }

    public boolean isDead() {
        return currentHealth <= 0;
    }
}
