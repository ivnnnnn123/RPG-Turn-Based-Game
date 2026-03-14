package view;

import model.Hero;
import model.Shop;
import model.Item;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ShopFrame extends JDialog {

    private Shop shop;
    private Hero hero;
    private JTextArea shopTextArea;
    private JLabel goldLabel;

    public ShopFrame(Frame parent, Hero hero) {
        super(parent, "Shop", true);
        this.hero = hero;
        this.shop = new Shop("Adventurer's Shop");

        setLayout(new BorderLayout());
        setSize(600, 500);
        setLocationRelativeTo(parent);

        // Top panel - info si aur ptr shop
        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("Welcome to " + shop.getShopName() + "!");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        goldLabel = new JLabel("Your Gold: " + hero.getGold());
        goldLabel.setFont(new Font("Arial", Font.BOLD, 14));
        goldLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        topPanel.add(titleLabel, BorderLayout.CENTER);
        topPanel.add(goldLabel, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        // Center panel - item uri din shop
        JPanel centerPanel = new JPanel(new BorderLayout());

        shopTextArea = new JTextArea();
        shopTextArea.setEditable(false);
        shopTextArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        updateShopDisplay();

        JScrollPane scrollPane = new JScrollPane(shopTextArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Available Items"));
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel buyPanel = new JPanel(new GridLayout(0, 3, 5, 5));

        for (int i = 0; i < shop.getAvailableItems().size(); i++) {
            final int index = i;
            Item item = shop.getAvailableItems().get(i);
            JButton buyButton = new JButton("Buy " + item.getName());
            buyButton.setToolTipText(item.getValue() + " gold");

            buyButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    buyItem(index);
                }
            });

            buyPanel.add(buyButton);
        }

        JPanel buttonContainer = new JPanel();
        buttonContainer.add(buyPanel);
        centerPanel.add(buttonContainer, BorderLayout.SOUTH);

        add(centerPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());

        JButton inventoryButton = new JButton("View Inventory");
        inventoryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showInventory();
            }
        });

        JButton closeButton = new JButton("Leave Shop");
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        JPanel bottomButtons = new JPanel();
        bottomButtons.add(inventoryButton);
        bottomButtons.add(closeButton);

        bottomPanel.add(bottomButtons, BorderLayout.CENTER);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void updateShopDisplay() {
        StringBuilder sb = new StringBuilder();
        sb.append("Welcome to ").append(shop.getShopName()).append("!\n\n");

        for (int i = 0; i < shop.getAvailableItems().size(); i++) {
            Item item = shop.getAvailableItems().get(i);
            sb.append((i + 1)).append(". ").append(item.getName()).append("\n");
            sb.append("   Type: ").append(item.getType().getName()).append("\n");
            sb.append("   Effect: ").append(item.getType().getDescription()).append("\n");
            sb.append("   Price: ").append(item.getValue()).append(" gold\n");
            sb.append("   In stock: ").append(item.getQuantity()).append("\n\n");
        }

        sb.append("Your gold: ").append(hero.getGold()).append("\n");
        sb.append("Inventory space: ").append(hero.getInventory().getItemCount())
                .append("/").append(hero.getInventory().getCapacity());

        shopTextArea.setText(sb.toString());
        goldLabel.setText("Your Gold: " + hero.getGold());
    }

    private void buyItem(int itemIndex) {
        boolean success = shop.buyItem(itemIndex, hero);
        if (success) {
            updateShopDisplay();
            JOptionPane.showMessageDialog(this,
                    "Purchase successful! Item added to your inventory.",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this,
                    "Purchase failed. Check your gold or inventory space.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showInventory() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== YOUR INVENTORY ===\n\n");
        sb.append("Gold: ").append(hero.getGold()).append("\n");
        sb.append("Capacity: ").append(hero.getInventory().getItemCount())
                .append("/").append(hero.getInventory().getCapacity()).append("\n\n");

        if (hero.getInventory().getItems().isEmpty()) {
            sb.append("Your inventory is empty.\n");
        } else {
            sb.append("Items:\n");
            for (Item item : hero.getInventory().getItems().values()) {
                sb.append("- ").append(item.getName())
                        .append(" (x").append(item.getQuantity()).append(")\n");
                sb.append("  Type: ").append(item.getType().getName()).append("\n");
                sb.append("  Effect: ").append(item.getType().getDescription()).append("\n\n");
            }
        }

        JTextArea inventoryArea = new JTextArea(sb.toString());
        inventoryArea.setEditable(false);
        inventoryArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

        JScrollPane scrollPane = new JScrollPane(inventoryArea);
        scrollPane.setPreferredSize(new Dimension(400, 300));

        JOptionPane.showMessageDialog(this, scrollPane,
                "Your Inventory", JOptionPane.INFORMATION_MESSAGE);
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
