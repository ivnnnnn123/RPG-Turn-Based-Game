package view;

import model.Hero;
import controller.FileManager;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import model.Item;

public class MainFrame extends JFrame {

    private GamePanel gamePanel;
    private JTextArea statusArea;

    public MainFrame() {
        setTitle("Turn-Based RPG");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        gamePanel = new GamePanel();
        add(gamePanel, BorderLayout.CENTER);

        JPanel statusPanel = new JPanel();
        statusPanel.setLayout(new BorderLayout());
        statusPanel.setBackground(Color.DARK_GRAY);
        statusPanel.setPreferredSize(new Dimension(800, 120));

        statusArea = new JTextArea();
        statusArea.setEditable(false);
        statusArea.setBackground(Color.BLACK);
        statusArea.setForeground(Color.GREEN);
        statusArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        statusArea.setText("=== RPG GAME ===\nMove with WASD keys\nPress ENTER near enemy to fight\nPress E at shop to trade\n");

        JScrollPane scrollPane = new JScrollPane(statusArea);
        statusPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton saveButton = new JButton("Save Game");
        JButton loadButton = new JButton("Load Game");
        JButton inventoryButton = new JButton("Inventory");
        JButton heroStatsButton = new JButton("Hero Stats");
        JButton shopButton = new JButton("Shop");
        JButton newGameButton = new JButton("New Game");
        newGameButton.addActionListener(e -> {
            startNewGame();
            gamePanel.requestFocusInWindow();
        });
        buttonPanel.add(newGameButton);

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveGame();
            }
        });

        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadGame();
            }
        });

        inventoryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showInventory();
            }
        });

        heroStatsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showHeroStats();
            }
        });

        shopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openShop();
            }
        });

        buttonPanel.add(saveButton);
        buttonPanel.add(loadButton);
        buttonPanel.add(inventoryButton);
        buttonPanel.add(heroStatsButton);
        buttonPanel.add(shopButton);

        statusPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(statusPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        gamePanel.requestFocusInWindow();
    }

    private void saveGame() {
        try {
            Hero hero = gamePanel.getHero();
            List<model.Enemy> enemies = gamePanel.getEnemies();
            int playerX = gamePanel.getPlayerX();
            int playerY = gamePanel.getPlayerY();

            if (hero != null) {
                FileManager.saveFullGame(hero, enemies, playerX, playerY);

                updateStatus("Game saved successfully!");
                updateStatus("Hero: " + hero.getName() + " (Level " + hero.getLevel() + ")");
                updateStatus("Gold: " + hero.getGold());
                updateStatus("Position: " + playerX + ", " + playerY);
                updateStatus("Enemies remaining: " + gamePanel.getAliveEnemiesCount());

                JOptionPane.showMessageDialog(this,
                        "Game saved successfully!\n"
                        + "Timestamp: " + new java.util.Date() + "\n"
                        + "Hero: " + hero.getName() + " (Level " + hero.getLevel() + ")\n"
                        + "Position: " + playerX + ", " + playerY + "\n"
                        + "Gold: " + hero.getGold(),
                        "Save Game", JOptionPane.INFORMATION_MESSAGE);
            } else {
                updateStatus("Error: Hero not found!");
            }
            gamePanel.requestFocusInWindow();
        } catch (Exception ex) {
            updateStatus("Error saving game: " + ex.getMessage());
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Save failed: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            gamePanel.requestFocusInWindow();
        }
    }

    private void loadGame() {
        try {
            if (!FileManager.saveFileExists()) {
                updateStatus("No save file found. Starting new game.");
                startNewGame();
                return;
            }

            FileManager.FullGameState fullState = FileManager.loadFullGame();

            if (fullState != null) {
                Hero loadedHero = fullState.getHero();
                List<model.Enemy> loadedEnemies = fullState.getEnemies();
                int savedX = fullState.getPlayerX();
                int savedY = fullState.getPlayerY();

                gamePanel.setHero(loadedHero);
                gamePanel.setEnemies(loadedEnemies);
                gamePanel.setPlayerPosition(savedX, savedY);

                int aliveEnemiesCount = fullState.getAliveEnemiesCount();
                int totalEnemies = loadedEnemies.size();
                int defeatedEnemies = fullState.getEnemiesDefeated();

                updateStatus("=== GAME LOADED ===");
                updateStatus("Save time: " + fullState.getTimestamp());
                updateStatus("Hero: " + loadedHero.getName() + " (Level " + loadedHero.getLevel() + ")");
                updateStatus("HP: " + loadedHero.getHealth() + "/" + loadedHero.getMaxHealth());
                updateStatus("Gold: " + loadedHero.getGold());
                updateStatus("Position: " + savedX + ", " + savedY);
                updateStatus("Enemies: " + aliveEnemiesCount + " remaining (Total: " + totalEnemies + ", Defeated: " + defeatedEnemies + ")");

                System.out.println("[Debug] Loaded enemies - Total: " + totalEnemies
                        + ", Alive: " + aliveEnemiesCount
                        + ", Defeated: " + defeatedEnemies);

                StringBuilder message = new StringBuilder();
                message.append("=== GAME LOADED SUCCESSFULLY ===\n\n");
                message.append("Save timestamp: ").append(fullState.getTimestamp()).append("\n");
                message.append("Hero: ").append(loadedHero.getName()).append("\n");
                message.append("Level: ").append(loadedHero.getLevel()).append("\n");
                message.append("Experience: ").append(loadedHero.getExperience()).append("\n");
                message.append("Health: ").append(loadedHero.getHealth()).append("/")
                        .append(loadedHero.getMaxHealth()).append("\n");
                message.append("Mana: ").append(loadedHero.getMana()).append("/")
                        .append(loadedHero.getMaxMana()).append("\n");
                message.append("Attack: ").append(loadedHero.getAttack()).append("\n");
                message.append("Defense: ").append(loadedHero.getDefense()).append("\n");
                message.append("Gold: ").append(loadedHero.getGold()).append("\n");
                message.append("Position: ").append(savedX).append(", ").append(savedY).append("\n");
                message.append("Enemies: ").append(aliveEnemiesCount).append("/").append(totalEnemies).append(" alive\n");
                message.append("Defeated: ").append(defeatedEnemies).append("\n");
                message.append("Inventory items: ").append(loadedHero.getInventory().getItemCount());

                JTextArea textArea = new JTextArea(message.toString());
                textArea.setEditable(false);
                textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
                JScrollPane scrollPane = new JScrollPane(textArea);
                scrollPane.setPreferredSize(new Dimension(400, 300));

                JOptionPane.showMessageDialog(this, scrollPane,
                        "Game Loaded", JOptionPane.INFORMATION_MESSAGE);

            } else {
                updateStatus("No saved game found. Starting fresh.");
                JOptionPane.showMessageDialog(this,
                        "No saved game found.\nStarting a new game.",
                        "No Save File", JOptionPane.INFORMATION_MESSAGE);
            }
            gamePanel.requestFocusInWindow();
        } catch (Exception ex) {
            updateStatus("Error loading game: " + ex.getMessage());
            JOptionPane.showMessageDialog(this,
                    "Load failed: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            gamePanel.requestFocusInWindow();
        }
    }

    private void showInventory() {
        Hero hero = gamePanel.getHero();
        if (hero != null) {
            JOptionPane.showMessageDialog(this,
                    "Inventory System\n"
                    + "Coming soon!\n\n"
                    + "Hero: " + hero.getName() + "\n"
                    + "Gold: " + hero.getGold() + "\n"
                    + "Skills: " + hero.getAvailableSkills().size() + "\n"
                    + "Inventory capacity: " + hero.getInventory().getItemCount()
                    + "/" + hero.getInventory().getCapacity(),
                    "Inventory", JOptionPane.INFORMATION_MESSAGE);
            updateStatus("Inventory opened");
        }
        gamePanel.requestFocusInWindow();
    }

    private void showHeroStats() {
        Hero hero = gamePanel.getHero();
        if (hero != null) {
            StringBuilder stats = new StringBuilder();
            stats.append("=== HERO STATISTICS ===\n\n");
            stats.append("Name: ").append(hero.getName()).append("\n");
            stats.append("Level: ").append(hero.getLevel()).append("\n");
            stats.append("Experience: ").append(hero.getExperience()).append("\n");
            stats.append("Gold: ").append(hero.getGold()).append("\n\n");
            stats.append("HP: ").append(hero.getHealth()).append("/").append(hero.getMaxHealth()).append("\n");
            stats.append("MP: ").append(hero.getMana()).append("/").append(hero.getMaxMana()).append("\n");
            stats.append("Attack: ").append(hero.getAttack()).append("\n");
            stats.append("Defense: ").append(hero.getDefense()).append("\n");
            stats.append("Speed: ").append(hero.getSpeed()).append("\n\n");
            stats.append("Skills: ").append(hero.getAvailableSkills().size()).append("\n");

            for (model.Skill skill : hero.getAvailableSkills()) {
                stats.append("- ").append(skill.getName()).append(": ").append(skill.getDescription()).append("\n");
            }

            stats.append("\nInventory: ").append(hero.getInventory().getItemCount()).append(" items");

            JTextArea textArea = new JTextArea(stats.toString());
            textArea.setEditable(false);
            textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(300, 400));

            JOptionPane.showMessageDialog(this, scrollPane,
                    "Hero Statistics", JOptionPane.INFORMATION_MESSAGE);
            updateStatus("Displayed hero statistics");
        }
        gamePanel.requestFocusInWindow();
    }

    private void openShop() {
        Hero hero = gamePanel.getHero();
        if (hero != null) {
            ShopFrame shopFrame = new ShopFrame(this, hero);
            shopFrame.setVisible(true);
            gamePanel.requestFocusInWindow();
            updateStatus("Visited the shop");

            gamePanel.repaint();
        } else {
            updateStatus("Error: Hero not found!");
        }
    }

    private void testSaveLoadCycle() {
        updateStatus("=== Testing Save/Load System ===");
        try {
            Hero testHero = new Hero("TestHero");
            testHero.gainExperience(300);
            testHero.addGold(500);
            testHero.getInventory().addItem(new Item("Test Sword", Item.ItemType.WEAPON, 20, 1));

            java.util.List<model.Enemy> testEnemies = new java.util.ArrayList<>();
            testEnemies.add(new model.Enemy("Test Goblin", 50, 10, 5, 8, 50, 25, 100, 100));

            // Save
            FileManager.saveFullGame(testHero, testEnemies, 250, 250);
            updateStatus("Test save completed");

            // Load
            FileManager.FullGameState loaded = FileManager.loadFullGame();
            if (loaded != null) {
                updateStatus("✓ Hero name: " + loaded.getHero().getName());
                updateStatus("✓ Hero level: " + loaded.getHero().getLevel());
                updateStatus("✓ Hero gold: " + loaded.getHero().getGold());
                updateStatus("✓ Position: " + loaded.getPlayerX() + ", " + loaded.getPlayerY());
                updateStatus("✓ Enemies loaded: " + loaded.getEnemies().size());
                updateStatus("✓ Inventory items: "
                        + loaded.getHero().getInventory().getItemCount());
                updateStatus("Test completed successfully!");
            }

        } catch (Exception e) {
            updateStatus("✗ Test failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void updateStatus(String text) {
        statusArea.append("> " + text + "\n");
        statusArea.setCaretPosition(statusArea.getDocument().getLength());
    }

    public GamePanel getGamePanel() {
        return gamePanel;
    }

    private void startNewGame() {
        gamePanel.resetGame();
        updateStatus("New game started!");
        updateStatus("Defeat all 5 enemies on the map!");
    }

    public void refreshHeroStats() {
        Hero hero = gamePanel.getHero();
        if (hero != null) {
            String stats = "Hero: " + hero.getName()
                    + " | Lvl: " + hero.getLevel()
                    + " | HP: " + hero.getHealth() + "/" + hero.getMaxHealth()
                    + " | MP: " + hero.getMana() + "/" + hero.getMaxMana()
                    + " | ATK: " + hero.getAttack()
                    + " | DEF: " + hero.getDefense()
                    + " | Gold: " + hero.getGold();

            updateStatus(stats);

            gamePanel.repaint();
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
 /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainFrame().setVisible(true);
            }
        });
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
