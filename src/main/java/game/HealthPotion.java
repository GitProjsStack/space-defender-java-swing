package game;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;
import java.util.Random;
import javax.imageio.ImageIO;

public class HealthPotion {
    private final int x;
    private int y;
    private static final int SIZE = 30;
    private static final int SPEED = 2;

    private static final BufferedImage[] potionImages = new BufferedImage[4];
    private static final Random random = new Random();

    static {
        for (int i = 0; i < 4; i++) {
            try {
                potionImages[i] = ImageIO.read(Objects.requireNonNull(
                        HealthPotion.class.getResourceAsStream("/images/healthPotionImgs/healthPotion" + (i + 1) + ".png")
                ));
            } catch (IOException e) {
                System.err.println("Failed to load healthPotion" + (i + 1) + ".png");
            }
        }
    }

    private final BufferedImage image;

    public HealthPotion(int x, int y) {
        this.x = x;
        this.y = y;
        this.image = potionImages[random.nextInt(potionImages.length)];
    }

    public void update() {
        y += SPEED;
    }

    public void draw(Graphics g) {
        if (image != null) {
            g.drawImage(image, x, y, SIZE, SIZE, null);
        } else {
            g.setColor(Color.PINK);
            g.fillOval(x, y, SIZE, SIZE);
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, SIZE, SIZE);
    }

    public int getY() {
        return y;
    }
}
