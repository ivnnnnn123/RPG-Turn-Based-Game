package view;

import model.Enemy;
import model.Hero;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

public class GamePanel extends JPanel implements KeyListener {

    private int playerX;
    private int playerY;
    private final int tileSize = 50;
    private final int playerSize = 30;

    private List<Enemy> enemies;
    private Hero hero;

    private boolean inCombat = false;
    private Enemy currentEnemy;

    public GamePanel() {
        this.playerX = 100;
        this.playerY = 100;
        this.enemies = new ArrayList<>();
        this.hero = new Hero("Arthur");

        initializeEnemies();

        setPreferredSize(new Dimension(800, 600));
        setBackground(new Color(34, 139, 34));
        setFocusable(true);
        addKeyListener(this);
    }

    private void initializeEnemies() {
        enemies.add(new Enemy("Goblin", 50, 10, 5, 8, 50, 25, 300, 200));
        enemies.add(new Enemy("Orc", 80, 15, 10, 5, 80, 40, 500, 350));
        enemies.add(new Enemy("Skeleton", 60, 12, 7, 10, 60, 30, 400, 100));
        enemies.add(new Enemy("Slime", 40, 8, 3, 15, 40, 20, 200, 400));
        enemies.add(new Enemy("Wolf", 70, 13, 6, 12, 70, 35, 600, 250));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // iarba
        g.setColor(new Color(50, 205, 50));
        for (int i = 0; i < getWidth(); i += tileSize) {
            for (int j = 0; j < getHeight(); j += tileSize) {
                g.fillRect(i, j, tileSize, tileSize);
                g.setColor(Color.BLACK);
                g.drawRect(i, j, tileSize, tileSize);
                g.setColor(new Color(50, 205, 50));
            }
        }

        // shop ul
        g.setColor(new Color(210, 180, 140));
        g.fillRect(700, 500, 40, 40);
        g.setColor(Color.BLACK);
        g.drawString("SHOP", 695, 490);

        // inamicii
        for (Enemy enemy : enemies) {
            if (!enemy.isAlive()) {
                continue;
            }

            switch (enemy.getName()) {
                case "Goblin":
                    g.setColor(new Color(0, 128, 0));
                    break;
                case "Orc":
                    g.setColor(new Color(128, 0, 0));
                    break;
                case "Skeleton":
                    g.setColor(Color.LIGHT_GRAY);
                    break;
                case "Slime":
                    g.setColor(new Color(0, 128, 128));
                    break;
                case "Wolf":
                    g.setColor(new Color(139, 69, 19));
                    break;
                default:
                    g.setColor(Color.RED);
            }

            g.fillOval(enemy.getX(), enemy.getY(), enemy.getSize(), enemy.getSize());

            g.setColor(Color.YELLOW);
            g.fillOval(enemy.getX() + 7, enemy.getY() + 7, 6, 6);
            g.fillOval(enemy.getX() + 17, enemy.getY() + 7, 6, 6);

            g.setColor(Color.WHITE);
            g.drawString(enemy.getName(), enemy.getX() - 5, enemy.getY() - 5);

            drawHealthBar(g, enemy.getX(), enemy.getY() - 10,
                    enemy.getSize(), enemy.getHealth(), enemy.getMaxHealth());
        }

        // eroul
        g.setColor(Color.BLUE);
        g.fillOval(playerX, playerY, playerSize, playerSize);

        g.setColor(Color.WHITE);
        g.fillOval(playerX + 7, playerY + 7, 8, 8);
        g.fillOval(playerX + 15, playerY + 7, 8, 8);

        drawPlayerHealthBar(g);

        // copacii
        g.setColor(new Color(101, 67, 33));
        for (int i = 0; i < 5; i++) {
            int treeX = 200 + i * 100;
            int treeY = 150 + (i % 2) * 100;
            g.fillRect(treeX, treeY, 15, 40);
            g.setColor(Color.GREEN);
            g.fillOval(treeX - 15, treeY - 20, 45, 40);
            g.setColor(new Color(101, 67, 33));
        }

        g.setColor(Color.WHITE);
        g.drawString("Use WASD to move", 10, 20);
        g.drawString("Player Position: (" + playerX + ", " + playerY + ")", 10, 40);
        g.drawString("Enemies remaining: " + getAliveEnemiesCount() + "/" + enemies.size(), 10, 60);
        g.drawString("Hero HP: " + hero.getHealth() + "/" + hero.getMaxHealth(), 10, 80);
        g.drawString("Hero MP: " + hero.getMana() + "/" + hero.getMaxMana(), 10, 100);
        g.drawString("Gold: " + hero.getGold(), 10, 120);
        g.drawString("Attack: " + hero.getAttack(), 10, 140);
        g.drawString("Defense: " + hero.getDefense(), 10, 160);
        g.drawString("Press E at shop to trade", 10, 180);
        g.drawString("Press ENTER near enemy to fight", 10, 200);
    }

    private void drawHealthBar(Graphics g, int x, int y, int width, int health, int maxHealth) {
        int barHeight = 5;
        int healthWidth = (int) ((double) health / maxHealth * width);

        g.setColor(Color.RED);
        g.fillRect(x, y, width, barHeight);
        g.setColor(Color.GREEN);
        g.fillRect(x, y, healthWidth, barHeight);
        g.setColor(Color.BLACK);
        g.drawRect(x, y, width, barHeight);
    }

    private void drawPlayerHealthBar(Graphics g) {
        int barWidth = 100;
        int barHeight = 10;
        int x = playerX - 35;
        int y = playerY - 15;

        int health = hero.getHealth();
        int maxHealth = hero.getMaxHealth();
        int healthWidth = (int) ((double) health / maxHealth * barWidth);

        g.setColor(Color.RED);
        g.fillRect(x, y, barWidth, barHeight);
        g.setColor(Color.GREEN);
        g.fillRect(x, y, healthWidth, barHeight);
        g.setColor(Color.BLACK);
        g.drawRect(x, y, barWidth, barHeight);
        g.drawString("HP: " + health + "/" + maxHealth, x, y - 5);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (inCombat) {
            return;
        }

        int step = 10;
        int oldX = playerX;
        int oldY = playerY;

        switch (e.getKeyCode()) {
            case KeyEvent.VK_W:
                playerY = Math.max(0, playerY - step);
                break;
            case KeyEvent.VK_S:
                playerY = Math.min(getHeight() - playerSize, playerY + step);
                break;
            case KeyEvent.VK_A:
                playerX = Math.max(0, playerX - step);
                break;
            case KeyEvent.VK_D:
                playerX = Math.min(getWidth() - playerSize, playerX + step);
                break;

            case KeyEvent.VK_ENTER:
                Enemy nearest = findNearestEnemy();
                if (nearest != null && nearest.isAlive()) {
                    startCombatWith(nearest);
                }
                break;

            case KeyEvent.VK_E:
                if (isAtShop(playerX, playerY)) {
                    openShop();
                }
                break;
        }

        checkEnemyCollisions();

        if (collidesWithTree(playerX, playerY)) {
            playerX = oldX;
            playerY = oldY;
        }

        repaint();
    }

    private void checkEnemyCollisions() {
        for (Enemy enemy : enemies) {
            if (enemy.isAlive() && enemy.collidesWith(playerX, playerY, playerSize)) {
                startCombatWith(enemy);
                break;
            }
        }
    }

    private void startCombatWith(Enemy enemy) {
        inCombat = true;
        currentEnemy = enemy;

        Frame parentFrame = (Frame) SwingUtilities.getWindowAncestor(this);

        CombatFrame combatFrame = new CombatFrame(parentFrame, hero, enemy);
        combatFrame.setVisible(true);

        MainFrame mainFrame = (MainFrame) SwingUtilities.getWindowAncestor(this);
        if (mainFrame != null && !enemy.isAlive()) {
            mainFrame.updateStatus("Defeated " + enemy.getName() + "!");
            mainFrame.updateStatus("Gained " + enemy.getExperienceReward() + " XP and "
                    + enemy.getGoldReward() + " gold!");
        }

        if (getAliveEnemiesCount() == 0) {
            JOptionPane.showMessageDialog(this,
                    "Congratulations! You defeated all enemies!\nGame Over",
                    "Victory", JOptionPane.INFORMATION_MESSAGE);
        }

        inCombat = false;
        currentEnemy = null;
        repaint();
        requestFocusInWindow();
    }

    private Enemy findNearestEnemy() {
        Enemy nearest = null;
        double minDistance = Double.MAX_VALUE;

        for (Enemy enemy : enemies) {
            if (!enemy.isAlive()) {
                continue;
            }

            double distance = Math.sqrt(
                    Math.pow(enemy.getX() - playerX, 2)
                    + Math.pow(enemy.getY() - playerY, 2)
            );

            if (distance < minDistance && distance < 100) {
                minDistance = distance;
                nearest = enemy;
            }
        }
        return nearest;
    }

    private boolean collidesWithTree(int x, int y) {
        int[][] trees = {{200, 150}, {300, 250}, {400, 150}, {500, 250}, {600, 150}};
        for (int[] tree : trees) {
            int treeX = tree[0];
            int treeY = tree[1];
            int treeWidth = 45;
            int treeHeight = 40;

            if (x < treeX + treeWidth
                    && x + playerSize > treeX
                    && y < treeY + treeHeight
                    && y + playerSize > treeY) {
                return true;
            }
        }
        return false;
    }

    private boolean isAtShop(int x, int y) {
        int shopX = 700;
        int shopY = 500;
        int shopRadius = 50;

        double distance = Math.sqrt(Math.pow(x - shopX, 2) + Math.pow(y - shopY, 2));
        return distance < shopRadius;
    }

    private void openShop() {
        if (hero != null) {
            Frame parentFrame = (Frame) SwingUtilities.getWindowAncestor(this);
            ShopFrame shopFrame = new ShopFrame(parentFrame, hero);
            shopFrame.setVisible(true);
            repaint();
            requestFocusInWindow();
        } else {
            System.out.println("Error: Hero not found!");
        }
    }

    public int getAliveEnemiesCount() {
        int count = 0;
        for (Enemy enemy : enemies) {
            if (enemy.isAlive()) {
                count++;
            }
        }
        return count;
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    public int getPlayerX() {
        return playerX;
    }

    public int getPlayerY() {
        return playerY;
    }

    public boolean isInCombat() {
        return inCombat;
    }

    public Enemy getCurrentEnemy() {
        return currentEnemy;
    }

    public Hero getHero() {
        return hero;
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }

    public void setHero(Hero newHero) {
        this.hero = newHero;
    }

    public void setEnemies(List<Enemy> newEnemies) {
        this.enemies = new ArrayList<>(newEnemies); // Facem o copie a listei
        System.out.println("[GamePanel] Enemies set. Total: " + enemies.size()
                + ", Alive: " + getAliveEnemiesCount());
        repaint();
    }

    public void setPlayerPosition(int x, int y) {
        this.playerX = x;
        this.playerY = y;
        repaint();
    }

    public void resetGame() {
        this.playerX = 100;
        this.playerY = 100;
        this.hero = new Hero("Arthur");
        this.enemies.clear();
        initializeEnemies();
        this.inCombat = false;
        this.currentEnemy = null;
        repaint();
        requestFocusInWindow();
    }
}
