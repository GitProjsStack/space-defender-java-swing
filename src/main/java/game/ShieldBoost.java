package game;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class ShieldBoost {
    private final int x;
    private int y;
    private static final int WIDTH = 30;
    private static final int HEIGHT = 30;
    private BufferedImage image;
    private static final int SPEED = 2;

    public ShieldBoost(int x, int y) {
        this.x = x;
        this.y = y;
        loadImage();
    }

    private void loadImage() {
        try {
            image = ImageIO.read(Objects.requireNonNull(
                    getClass().getResourceAsStream("/images/shieldBoost.png")
            ));
        } catch (IOException e) {
            System.err.println("Failed to load shield boost image.");
        }
    }

    public void update() {
        y += SPEED;
    }

    public void draw(Graphics g) {
        if (image != null) {
            g.drawImage(image, x, y, WIDTH, HEIGHT, null);
        } else {
            g.setColor(Color.CYAN);
            g.fillOval(x, y, WIDTH, HEIGHT);
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, WIDTH, HEIGHT);
    }

    public int getY() {
        return y;
    }
}
