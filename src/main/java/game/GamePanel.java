package game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;

public class GamePanel extends JPanel implements ActionListener, KeyListener {

    private Timer timer;
    private boolean left, right, up, down, space;

    private PlayerShip player;
    private java.util.List<Enemy> enemies;

    public GamePanel() {
        setPreferredSize(new Dimension(800, 600));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);

        player = new PlayerShip(400, 500);
        enemies = new ArrayList<>();

        timer = new Timer(16, this); // ~60 FPS
    }

    public void startGameLoop() {
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        update();
        repaint();
    }

    private void update() {
        player.update(left, right, up, down);

        // Spawn enemies randomly
        if (Math.random() < 0.02) {
            enemies.add(new Enemy((int) (Math.random() * 760), -50));
        }

        Iterator<Enemy> iterator = enemies.iterator();
        while (iterator.hasNext()) {
            Enemy enemy = iterator.next();
            enemy.update();

            if (enemy.getY() > 600) {
                iterator.remove(); // off screen
            }

            if (player.getBounds().intersects(enemy.getBounds())) {
                timer.stop();
                JOptionPane.showMessageDialog(this, "Game Over!");
                System.exit(0);
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        player.draw(g);
        for (Enemy e : enemies) {
            e.draw(g);
        }
    }

    // Input handling
    @Override public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT, KeyEvent.VK_A -> left = true;
            case KeyEvent.VK_RIGHT, KeyEvent.VK_D -> right = true;
            case KeyEvent.VK_UP, KeyEvent.VK_W -> up = true;
            case KeyEvent.VK_DOWN, KeyEvent.VK_S -> down = true;
            case KeyEvent.VK_SPACE -> space = true;
        }
    }

    @Override public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT, KeyEvent.VK_A -> left = false;
            case KeyEvent.VK_RIGHT, KeyEvent.VK_D -> right = false;
            case KeyEvent.VK_UP, KeyEvent.VK_W -> up = false;
            case KeyEvent.VK_DOWN, KeyEvent.VK_S -> down = false;
            case KeyEvent.VK_SPACE -> space = false;
        }
    }

    @Override public void keyTyped(KeyEvent e) {}
}
