package app;

import javax.swing.*;
import java.awt.*;
import game.GamePanel;

/**
 * Main application class that displays a splash screen with the game logo and loading message
 * for a few seconds before switching to the main game panel.
 * The splash screen provides a visual welcome and feedback to the user during game startup,
 * improving the user experience by showing the Space Defender logo and "Loading..." text.
 */
public class Main {

    static final int WIDTH = 800;
    static final int HEIGHT = 600;

    public static void main(String[] args) {
        JFrame window = new JFrame("Space Defender");
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setSize(WIDTH, HEIGHT);
        window.setLocationRelativeTo(null);

        // Splash panel with scaled logo and loading text
        JPanel splashPanel = new JPanel() {
            final Image gameLogo = new ImageIcon("images/space-defender-logo.png").getImage();
            private int dotCount = 0;

            {
                // Timer for updating the dots every 500ms
                new Timer(500, e -> {
                    dotCount = (dotCount + 1) % 4;
                    repaint();
                }).start();
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(Color.BLACK);
                g.fillRect(0, 0, getWidth(), getHeight());

                g.drawImage(gameLogo, 0, 0, Main.WIDTH, Main.HEIGHT, null);

                // Draw loading text with animated dots
                g.setColor(Color.WHITE);
                g.setFont(new Font("SansSerif", Font.BOLD, 24));
                String loadingText = "Loading" + ".".repeat(dotCount);
                int strWidth = g.getFontMetrics().stringWidth(loadingText);
                g.drawString(loadingText, (getWidth() - strWidth) / 2, 40);
            }
        };

        window.add(splashPanel);
        window.setVisible(true);

        // After 5 seconds, switch to game panel
        new Timer(5000, e -> {
            window.remove(splashPanel);
            GamePanel gamePanel = new GamePanel();
            window.add(gamePanel);
            window.revalidate();
            window.repaint();
            gamePanel.requestFocusInWindow();
            gamePanel.startGameLoop();
            ((Timer) e.getSource()).stop();
        }).start();
    }
}
