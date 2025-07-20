package game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

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
    private final List<Projectile> playerProjectiles;
    private final List<Explosion> explosions;

    private Explosion playerExplosion = null;
    private boolean playerDead = false;
    private int enemiesKilled = 0;

    private int level = 1;
    private long levelStartTime = System.currentTimeMillis();
    private final int[] levelDurations = {20000, 30000, 40000, 50000, 60000, 70000};
    private static final int maxLevel = 7;
    private boolean gameWon = false;

    public GamePanel() {
        setPreferredSize(new Dimension(800, 600));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);

        player = new PlayerShip(400, 500);
        enemies = new ArrayList<>();
        enemyProjectiles = new ArrayList<>();
        playerProjectiles = new ArrayList<>();
        explosions = new ArrayList<>();

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
        if (!playerDead) {
            player.update(left, right, up, down);

            if (space && player.canShoot()) {
                playerProjectiles.add(player.shoot());
            }

            maybeSpawnEnemy();
            updateEnemies();
            updateEnemyProjectiles();
        }

        updatePlayerProjectiles();
        updateExplosions();

        if (!playerDead && player.isDead()) {
            playerDead = true;
            playerExplosion = new Explosion(player.getX(), player.getY());
        }

        if (playerDead && playerExplosion != null && playerExplosion.isFinished()) {
            gameOver();
        }

        handleLevelProgression();
    }

    private void maybeSpawnEnemy() {
        if (!playerDead && random.nextDouble() < 0.02) {
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

            if (!playerDead && player.getBounds().intersects(enemy.getBounds())) {
                player.takeDamage(20);
                enemy.takeDamage(999);
                if (enemy.isDead()) {
                    explosions.add(new Explosion(enemy.getX(), enemy.getY()));
                    enemyIterator.remove();
                }
                continue;
            }

            if (enemy.canShoot()) {
                enemyProjectiles.add(enemy.shoot());
            }
        }
    }

    private void updateEnemyProjectiles() {
        Iterator<Projectile> iterator = enemyProjectiles.iterator();
        while (iterator.hasNext()) {
            Projectile p = iterator.next();
            p.update();

            if (p.getY() > 600) {
                iterator.remove();
                continue;
            }

            if (!playerDead && player.getBounds().intersects(p.getBounds())) {
                player.takeDamage(10);
                iterator.remove();
            }
        }
    }

    private void updatePlayerProjectiles() {
        Iterator<Projectile> projIt = playerProjectiles.iterator();
        while (projIt.hasNext()) {
            Projectile proj = projIt.next();
            proj.update();

            if (proj.getY() < 0) {
                projIt.remove();
                continue;
            }

            Iterator<Enemy> enemyIt = enemies.iterator();
            while (enemyIt.hasNext()) {
                Enemy enemy = enemyIt.next();
                if (proj.getBounds().intersects(enemy.getBounds())) {
                    enemy.takeDamage(20);
                    projIt.remove();

                    if (enemy.isDead()) {
                        explosions.add(new Explosion(enemy.getX(), enemy.getY()));
                        enemyIt.remove();
                        enemiesKilled ++;
                    }
                    break;
                }
            }
        }
    }

    private void updateExplosions() {
        Iterator<Explosion> expIt = explosions.iterator();
        while (expIt.hasNext()) {
            Explosion exp = expIt.next();
            exp.update();
            if (exp.isFinished()) {
                expIt.remove();
            }
        }
        if (playerExplosion != null && !playerExplosion.isFinished()) {
            playerExplosion.update();
        }
    }

    private void handleLevelProgression() {
        if (!playerDead && level < maxLevel) {
            long elapsed = System.currentTimeMillis() - levelStartTime;
            if (elapsed > levelDurations[level - 1]) {
                level++;
                levelStartTime = System.currentTimeMillis();
                System.out.println("Level up! Now at level: " + level);
            }
        } else if (!gameWon && !playerDead) {
            gameWon = true;
            timer.stop();
            JOptionPane.showMessageDialog(this, "You Win!");
            System.exit(0);
        }
    }

    private void gameOver() {
        timer.stop();
        int choice = JOptionPane.showConfirmDialog(this,
                "Game Over! You were destroyed!\nPlay again?",
                "Game Over",
                JOptionPane.YES_NO_OPTION);

        if (choice == JOptionPane.YES_OPTION) {
            restartGame();
        } else {
            System.exit(0);
        }
    }

    private void restartGame() {
        // Reset everything to initial state

        playerDead = false;
        gameWon = false;
        level = 1;
        levelStartTime = System.currentTimeMillis();

        player.resetHealth();
        player.setPosition(400, 500);

        enemies.clear();
        enemyProjectiles.clear();
        playerProjectiles.clear();
        explosions.clear();

        playerExplosion = null;

        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (!playerDead) {
            player.draw(g);
        } else if (playerExplosion != null) {
            playerExplosion.draw(g);
        }

        for (Enemy enemy : enemies) {
            enemy.draw(g);
        }

        for (Projectile p : enemyProjectiles) {
            p.draw(g);
        }

        for (Projectile p : playerProjectiles) {
            p.draw(g);
        }

        for (Explosion explosion : explosions) {
            explosion.draw(g);
        }

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 16));
        g.drawString("Level: " + level + "  Kills: " + enemiesKilled, 10, 20);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (!playerDead) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT, KeyEvent.VK_A -> left = true;
                case KeyEvent.VK_RIGHT, KeyEvent.VK_D -> right = true;
                case KeyEvent.VK_UP, KeyEvent.VK_W -> up = true;
                case KeyEvent.VK_DOWN, KeyEvent.VK_S -> down = true;
                case KeyEvent.VK_SPACE -> space = true;
                default -> {} // do nothing
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (!playerDead) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT, KeyEvent.VK_A -> left = false;
                case KeyEvent.VK_RIGHT, KeyEvent.VK_D -> right = false;
                case KeyEvent.VK_UP, KeyEvent.VK_W -> up = false;
                case KeyEvent.VK_DOWN, KeyEvent.VK_S -> down = false;
                case KeyEvent.VK_SPACE -> space = false;
                default -> {} // do nothing
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Not used
    }
}
