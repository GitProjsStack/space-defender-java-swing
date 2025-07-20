package game;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;
import javax.imageio.ImageIO;

/**
 * Represents the player's ship with movement, shooting, and health behavior.
 */
public class PlayerShip {

    private int x;
    private int y;
    private static final int width = 40;
    private static final int height = 40;
    private static final int speed = 5;
    private BufferedImage image;
    private static final Color NEON_GREEN = new Color(57, 255, 20);

    private long lastShotTime = 0;
    private static final int SHOT_COOLDOWN = 300; // milliseconds

    private final int maxHealth = 100;
    private int currentHealth = maxHealth;

    /**
     * Initializes the player's ship at the given position.
     */
    public PlayerShip(int x, int y) {
        this.x = x;
        this.y = y;
        loadImage();
    }

    private void loadImage() {
        try {
            image = ImageIO.read(
                    Objects.requireNonNull(
                            getClass().getResourceAsStream("/images/playerImgs/playership.png")
                    )
            );
        } catch (IOException e) {
            System.err.println("Failed to load player ship image.");
        }
    }

    /**
     * Updates the ship's position based on input.
     */
    public void update(boolean left, boolean right, boolean up, boolean down) {
        if (left) x -= speed;
        if (right) x += speed;
        if (up) y -= speed;
        if (down) y += speed;

        // Boundaries
        x = Math.max(0, Math.min(x, 800 - width));
        y = Math.max(0, Math.min(y, 600 - height));
    }

    /**
     * Draws the player ship and its health bar.
     */
    public void draw(Graphics g) {
        if (image != null) {
            g.drawImage(image, x, y, width, height, null);
        } else {
            g.setColor(Color.BLUE);
            g.fillRect(x, y, width, height);
        }

        drawHealthBar(g);
    }

    private void drawHealthBar(Graphics g) {
        int barWidth = 40;
        int barHeight = 6;
        int barX = x;
        int barY = y - 10;

        HealthBarRenderer.drawHealthBar(g, barX, barY, barWidth, barHeight, currentHealth, maxHealth);
    }

    public boolean canShoot() {
        return System.currentTimeMillis() - lastShotTime >= SHOT_COOLDOWN;
    }

    public Projectile shoot() {
        lastShotTime = System.currentTimeMillis();
        return new Projectile(x + width / 2 - 3, y, -8, NEON_GREEN);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public void takeDamage(int damage) {
        currentHealth -= damage;
        if (currentHealth < 0) currentHealth = 0;
    }

    public boolean isDead() {
        return currentHealth <= 0;
    }

    public void resetHealth() {
        currentHealth = maxHealth;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
