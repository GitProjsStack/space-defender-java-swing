package game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Main game panel handling rendering, game loop, input, level progression, enemy shooting, and win condition.
 */
public class GamePanel extends JPanel implements ActionListener, KeyListener {

    private final Timer timer;
    private final Random random = new Random();

    private boolean left;
    private boolean right;
    private boolean up;
    private boolean down;
    private boolean space;

    private final PlayerShip player;
    private final List<Enemy> enemies;
    private final List<Projectile> enemyProjectiles;

    private int level = 1;
    private long levelStartTime = System.currentTimeMillis();
    private final int[] levelDurations = {20000, 30000, 40000, 50000, 60000, 70000}; // in ms
    private static final int maxLevel = 7;
    private boolean gameWon = false;

    /**
     * Constructs the game panel and initializes game objects.
     */
    public GamePanel() {
        setPreferredSize(new Dimension(800, 600));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);

        player = new PlayerShip(400, 500);
        enemies = new ArrayList<>();
        enemyProjectiles = new ArrayList<>();

        timer = new Timer(16, this); // ~60 FPS
    }

    /**
     * Starts the game loop timer.
     */
    public void startGameLoop() {
        timer.start();
    }

    /**
     * Game loop callback: updates game logic and triggers repaint.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        update();
        repaint();
    }

    /**
     * Updates player, enemies, projectiles, handles shooting, collisions, and level progression.
     */
    private void update() {
        player.update(left, right, up, down);
        maybeSpawnEnemy();
        updateEnemies();
        updateEnemyProjectiles();
        handleLevelProgression();
    }

    private void maybeSpawnEnemy() {
        if (random.nextDouble() < 0.02) {
            int x = random.nextInt(800 - 40); // enemy width = 40
            enemies.add(new Enemy(x, -50, level));
        }
    }

    private void updateEnemies() {
        Iterator<Enemy> enemyIterator = enemies.iterator();
        while (enemyIterator.hasNext()) {
            Enemy enemy = enemyIterator.next();
            enemy.update();

            if (enemy.getY() > 600) {
                enemyIterator.remove();
                continue;
            }

            if (player.getBounds().intersects(enemy.getBounds())) {
                gameOver("Crashed into enemy!");
                return;
            }

            if (enemy.canShoot()) {
                enemyProjectiles.add(enemy.shoot());
            }
        }
    }

    private void updateEnemyProjectiles() {
        Iterator<Projectile> projectileIterator = enemyProjectiles.iterator();
        while (projectileIterator.hasNext()) {
            Projectile p = projectileIterator.next();
            p.update();

            if (p.getY() > 600) {
                projectileIterator.remove();
                continue;
            }

            if (player.getBounds().intersects(p.getBounds())) {
                gameOver("Shot by an enemy!");
                return;
            }
        }
    }

    private void handleLevelProgression() {
        if (level < maxLevel) {
            long elapsed = System.currentTimeMillis() - levelStartTime;
            if (elapsed > levelDurations[level - 1]) {
                level++;
                levelStartTime = System.currentTimeMillis();
                System.out.println("Level up! Now at level: " + level);
            }
        } else if (!gameWon) {
            gameWon = true;
            timer.stop();
            JOptionPane.showMessageDialog(this, "You Win!");
            System.exit(0);
        }
    }

    /**
     * Ends the game with a message.
     *
     * @param reason The reason for game over.
     */
    private void gameOver(String reason) {
        timer.stop();
        JOptionPane.showMessageDialog(this, "Game Over! " + reason);
        System.exit(0);
    }

    /**
     * Paints the game objects and HUD.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw player
        player.draw(g);

        // Draw enemies
        for (Enemy enemy : enemies) {
            enemy.draw(g);
        }

        // Draw enemy bullets
        for (Projectile p : enemyProjectiles) {
            p.draw(g);
        }

        // Level display
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 16));
        g.drawString("Level: " + level, 10, 20);
    }

    // ===== Input Handling =====

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT, KeyEvent.VK_A -> left = true;
            case KeyEvent.VK_RIGHT, KeyEvent.VK_D -> right = true;
            case KeyEvent.VK_UP, KeyEvent.VK_W -> up = true;
            case KeyEvent.VK_DOWN, KeyEvent.VK_S -> down = true;
            case KeyEvent.VK_SPACE -> space = true;
            default -> left = right = up = down = space = false;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT, KeyEvent.VK_A -> left = false;
            case KeyEvent.VK_RIGHT, KeyEvent.VK_D -> right = false;
            case KeyEvent.VK_UP, KeyEvent.VK_W -> up = false;
            case KeyEvent.VK_DOWN, KeyEvent.VK_S -> down = false;
            case KeyEvent.VK_SPACE -> space = false;
            default -> left = right = up = down = space = false;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Not used
        // We must override this method from the abstract class though
    }
}
