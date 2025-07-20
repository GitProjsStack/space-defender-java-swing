package game;

import java.awt.*;

public class HealthBarRenderer {

    /**
     * Draws a health bar at the specified location.
     *
     * @param g             The Graphics context
     * @param x             The x-coordinate of the bar
     * @param y             The y-coordinate of the bar
     * @param width         Total width of the health bar
     * @param height        Height of the health bar
     * @param currentHealth Current health value
     * @param maxHealth     Maximum health value
     */
    public static void drawHealthBar(Graphics g, int x, int y, int width, int height,
                                     int currentHealth, int maxHealth) {
        g.setColor(Color.RED);
        g.fillRect(x, y, width, height);

        g.setColor(Color.GREEN);
        int healthWidth = (int) ((currentHealth / (double) maxHealth) * width);
        g.fillRect(x, y, healthWidth, height);

        g.setColor(Color.WHITE);
        g.drawRect(x, y, width, height);
    }
}
