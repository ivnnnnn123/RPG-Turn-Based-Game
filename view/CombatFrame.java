package view;

import model.Hero;
import model.Enemy;
import model.Skill;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import model.Item;

public class CombatFrame extends JDialog {

    private Hero hero;
    private Enemy enemy;
    private JTextArea combatLog;
    private JLabel heroHPLabel;
    private JLabel heroMPLabel;
    private JLabel enemyHPLabel;
    private JButton attackButton;
    private JButton skillButton;
    private JButton defendButton;
    private JButton itemButton;
    private JButton fleeButton;
    private boolean playerTurn;
    private JLabel heroAttackLabel;
    private JLabel heroDefenseLabel;
    private JLabel heroExpLabel;

    public CombatFrame(Frame parent, Hero hero, Enemy enemy) {
        super(parent, "Combat", true);
        this.hero = hero;
        this.enemy = enemy;
        this.playerTurn = true;

        setLayout(new BorderLayout());
        setSize(800, 600);
        setLocationRelativeTo(parent);

        JPanel infoPanel = new JPanel(new GridLayout(1, 2, 10, 10));

        // Informatii erou
        JPanel heroPanel = new JPanel(new BorderLayout());
        heroPanel.setBorder(BorderFactory.createTitledBorder("Hero: " + hero.getName()));
        JPanel heroStats = new JPanel(new GridLayout(6, 1));
        heroHPLabel = new JLabel("HP: " + hero.getHealth() + "/" + hero.getMaxHealth());
        heroMPLabel = new JLabel("MP: " + hero.getMana() + "/" + hero.getMaxMana());
        heroAttackLabel = new JLabel("Attack: " + hero.getAttack());
        heroDefenseLabel = new JLabel("Defense: " + hero.getDefense());

        JLabel heroLevelLabel = new JLabel("Level: " + hero.getLevel());
        heroExpLabel = new JLabel("EXP: " + hero.getExperience());

        heroStats.add(heroHPLabel);
        heroStats.add(heroMPLabel);
        heroStats.add(heroAttackLabel);
        heroStats.add(heroDefenseLabel);
        heroStats.add(heroLevelLabel);
        heroStats.add(heroExpLabel);
        heroPanel.add(heroStats, BorderLayout.CENTER);

        // Informatii inamic
        JPanel enemyPanel = new JPanel(new BorderLayout());
        enemyPanel.setBorder(BorderFactory.createTitledBorder("Enemy: " + enemy.getName()));
        JPanel enemyStats = new JPanel(new GridLayout(4, 1));
        enemyHPLabel = new JLabel("HP: " + enemy.getHealth() + "/" + enemy.getMaxHealth());
        JLabel enemyAttackLabel = new JLabel("Attack: " + enemy.getAttack());
        JLabel enemyDefenseLabel = new JLabel("Defense: " + enemy.getDefense());
        JLabel enemyRewardLabel = new JLabel("Reward: " + enemy.getExperienceReward() + " XP, "
                + enemy.getGoldReward() + " Gold");

        enemyStats.add(enemyHPLabel);
        enemyStats.add(enemyAttackLabel);
        enemyStats.add(enemyDefenseLabel);
        enemyStats.add(enemyRewardLabel);
        enemyPanel.add(enemyStats, BorderLayout.CENTER);

        infoPanel.add(heroPanel);
        infoPanel.add(enemyPanel);
        add(infoPanel, BorderLayout.NORTH);

        combatLog = new JTextArea();
        combatLog.setEditable(false);
        combatLog.setFont(new Font("Monospaced", Font.PLAIN, 12));
        combatLog.setBackground(Color.BLACK);
        combatLog.setForeground(Color.WHITE);
        combatLog.setText("=== COMBAT STARTED ===\n");
        combatLog.append(hero.getName() + " vs " + enemy.getName() + "\n\n");

        JScrollPane scrollPane = new JScrollPane(combatLog);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Combat Log"));
        add(scrollPane, BorderLayout.CENTER);

        JPanel actionPanel = new JPanel(new GridLayout(1, 5, 5, 5));

        attackButton = new JButton("Attack");
        skillButton = new JButton("Skill");
        defendButton = new JButton("Defend");
        itemButton = new JButton("Item");
        fleeButton = new JButton("Flee");

        actionPanel.add(attackButton);
        actionPanel.add(skillButton);
        actionPanel.add(defendButton);
        actionPanel.add(itemButton);
        actionPanel.add(fleeButton);

        add(actionPanel, BorderLayout.SOUTH);

        setupActions();

        updateUI();

        if (hero.getSpeed() < enemy.getSpeed()) {
            combatLog.append(enemy.getName() + " is faster and attacks first!\n\n");
            playerTurn = false;
            disableButtons();
            enemyTurn();
        } else {
            combatLog.append(hero.getName() + " acts first!\n\n");
            playerTurn = true;
            enableButtons();
        }

    }

    private void showItemsDialog() {
        Map<String, Item> items = hero.getInventory().getItems();

        if (items.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Your inventory is empty!",
                    "Inventory", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        JDialog itemsDialog = new JDialog(this, "Select Item", true);
        itemsDialog.setLayout(new BorderLayout());
        itemsDialog.setSize(400, 300);
        itemsDialog.setLocationRelativeTo(this);

        JPanel itemsPanel = new JPanel(new GridLayout(0, 1, 5, 5));

        ArrayList<Item> itemList = new ArrayList<>(items.values());

        for (Item item : itemList) {
            if (item.getQuantity() <= 0) {
                continue;
            }

            JButton itemButton = new JButton(item.getName() + " (x" + item.getQuantity() + ")");
            itemButton.setToolTipText(item.getType().getDescription() + " - Value: " + item.getValue());

            itemButton.addActionListener(e -> {
                // Folosește itemul si obtine mesajul din Item
                String message = item.use(hero);
                combatLog.append(message + "\n");

                if (item.getType() == Item.ItemType.SCROLL && item.getName().toLowerCase().contains("fire")) {
                    int damage = item.getValue();
                    enemy.takeDamage(damage);
                    combatLog.append("The fire deals damage to " + enemy.getName() + "!\n");
                }

                updateUI();

                itemsDialog.dispose();

                checkCombatStatus();
                if (enemy.isAlive()) {
                    enemyTurn();
                }
            });

            itemsPanel.add(itemButton);
        }

        JButton closeBtn = new JButton("Close");
        closeBtn.addActionListener(e -> itemsDialog.dispose());

        itemsDialog.add(new JScrollPane(itemsPanel), BorderLayout.CENTER);
        itemsDialog.add(closeBtn, BorderLayout.SOUTH);
        itemsDialog.setVisible(true);
    }

    private void refreshGamePanel() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                java.awt.Window window = SwingUtilities.getWindowAncestor(CombatFrame.this);
                if (window != null) {
                    window.repaint();
                }
            }
        });
    }

    private void setupActions() {
        attackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (playerTurn) {
                    hero.attack(enemy);
                    combatLog.append(hero.getName() + " attacks!\n");
                    checkCombatStatus();
                    if (enemy.isAlive()) {
                        enemyTurn();
                    }
                }
            }
        });

        skillButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (playerTurn) {
                    showSkillsDialog();
                }
            }
        });

        defendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (playerTurn) {
                    hero.defend();
                    combatLog.append(hero.getName() + " takes defensive stance!\n");
                    combatLog.append("Defense increased temporarily!\n");
                    enemyTurn();
                }
            }
        });

        fleeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int chance = (int) (Math.random() * 100);
                if (chance > 30) {
                    combatLog.append("You successfully fled from combat!\n");
                    dispose();
                } else {
                    combatLog.append("Failed to flee! Enemy gets a free attack!\n");
                    enemyTurn();
                }
            }
        });

        itemButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (playerTurn) {
                    showItemsDialog();
                }
            }
        });

    }

    private void showSkillsDialog() {
        JDialog skillDialog = new JDialog(this, "Select Skill", true);
        skillDialog.setLayout(new BorderLayout());
        skillDialog.setSize(400, 300);
        skillDialog.setLocationRelativeTo(this);

        JPanel skillsPanel = new JPanel(new GridLayout(0, 1, 5, 5));

        for (Skill skill : hero.getAvailableSkills()) {
            int skillLevel = hero.getSkillLevel(skill);
            JButton skillBtn = new JButton(skill.getName() + " (Lvl " + skillLevel + ", MP " + skill.getManaCost() + ")");
            skillBtn.setToolTipText(skill.getDescription() + " - Level " + skillLevel);

            skillBtn.addActionListener(e -> {
                if (hero.getMana() < skill.getManaCost()) {
                    JOptionPane.showMessageDialog(this, "Not enough mana!", "Warning", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                model.Character target = (skill == Skill.HEAL) ? hero : enemy;

                String message = hero.useSkillWithMessage(skill, target);
                combatLog.append(message + "\n");

                updateUI();

                refreshGamePanel();

                skillDialog.dispose();
                checkCombatStatus();
                if (enemy.isAlive()) {
                    enemyTurn();
                }
            });
            skillsPanel.add(skillBtn);
        }

        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.addActionListener(e -> skillDialog.dispose());

        skillDialog.add(new JScrollPane(skillsPanel), BorderLayout.CENTER);
        skillDialog.add(cancelBtn, BorderLayout.SOUTH);
        skillDialog.setVisible(true);
    }

    private void enemyTurn() {
        playerTurn = false;
        disableButtons();

        javax.swing.Timer timer = new javax.swing.Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hero.resetTemporaryDefense();

                if (enemy.isAlive()) {
                    enemy.attackHero(hero);
                    combatLog.append(enemy.getName() + " attacks!\n");
                    updateUI();
                    checkCombatStatus();

                    if (hero.isAlive()) {
                        playerTurn = true;
                        enableButtons();
                    }
                }
            }
        });
        timer.setRepeats(false);
        timer.start();
    }

    private void checkCombatStatus() {
        updateUI();

        if (!enemy.isAlive()) {
            combatLog.append("\n=== VICTORY! ===\n");
            combatLog.append("You defeated " + enemy.getName() + "!\n");
            combatLog.append("Reward: " + enemy.getExperienceReward() + " XP, "
                    + enemy.getGoldReward() + " Gold\n");

            hero.gainExperience(enemy.getExperienceReward());
            hero.addGold(enemy.getGoldReward());

            disableButtons();

            JButton closeBtn = new JButton("Close");
            closeBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    dispose();
                }
            });

            fleeButton.setVisible(false);
            ((JPanel) fleeButton.getParent()).add(closeBtn);

            if (getParent() instanceof MainFrame) {
                ((MainFrame) getParent()).updateStatus("Defeated " + enemy.getName() + "!");
            }
        }

        if (!hero.isAlive()) {
            combatLog.append("\n=== DEFEAT ===\n");
            combatLog.append("You were defeated by " + enemy.getName() + "!\n");
            disableButtons();

            JButton closeBtn = new JButton("Close");
            closeBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    dispose();
                }
            });

            fleeButton.setVisible(false);
            ((JPanel) fleeButton.getParent()).add(closeBtn);
        }
    }

    private void updateUI() {
        heroHPLabel.setText("HP: " + hero.getHealth() + "/" + hero.getMaxHealth());
        heroMPLabel.setText("MP: " + hero.getMana() + "/" + hero.getMaxMana());
        heroAttackLabel.setText("Attack: " + hero.getAttack());
        heroDefenseLabel.setText("Defense: " + hero.getDefense());
        heroExpLabel.setText("EXP: " + hero.getExperience());
        enemyHPLabel.setText("HP: " + enemy.getHealth() + "/" + enemy.getMaxHealth());

        combatLog.setCaretPosition(combatLog.getDocument().getLength());
    }

    private void disableButtons() {
        attackButton.setEnabled(false);
        skillButton.setEnabled(false);
        defendButton.setEnabled(false);
        itemButton.setEnabled(false);
        fleeButton.setEnabled(false);
    }

    private void enableButtons() {
        attackButton.setEnabled(true);
        skillButton.setEnabled(true);
        defendButton.setEnabled(true);
        itemButton.setEnabled(true);
        fleeButton.setEnabled(true);
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


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
