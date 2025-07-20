package game;

import java.awt.*;
import java.util.Objects;
import javax.swing.ImageIcon;

/**
 * PlayerShip class representing the player's ship with image rendering.
 */
public class PlayerShip extends GameObject {

    private final Image image;

    public PlayerShip(int x, int y) {
        super(x, y, 40, 40);
        // Load image from resources folder
        image = new ImageIcon(
                Objects.requireNonNull(
                        getClass().getResource("/images/playerImgs/playership.png")
                )
        ).getImage();
    }

    @Override
    public void update() {
        // no implementation
    }

    @Override
    public void update(boolean left, boolean right, boolean up, boolean down) {
        int SPEED = 5;
        if (left && x > 0) x -= SPEED;
        if (right && x < 800 - width) x += SPEED;
        if (up && y > 0) y -= SPEED;
        if (down && y < 600 - height) y += SPEED;
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(image, x, y, width, height, null);
    }
}
