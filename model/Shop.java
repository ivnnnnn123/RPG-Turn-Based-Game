package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Shop implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<Item> availableItems;
    private String shopName;

    public Shop(String shopName) {
        this.shopName = shopName;
        this.availableItems = new ArrayList<>();
        initializeShopItems();
    }

    private void initializeShopItems() {
        availableItems.add(new Item("Health Potion", Item.ItemType.POTION, 30, 50)); // 50 gold
        availableItems.add(new Item("Mana Potion", Item.ItemType.POTION, 20, 40));   // 40 gold
        availableItems.add(new Item("Great Health Potion", Item.ItemType.POTION, 60, 100)); // 100 gold
        availableItems.add(new Item("Iron Sword", Item.ItemType.WEAPON, 10, 200));   // +10 attack
        availableItems.add(new Item("Steel Armor", Item.ItemType.ARMOR, 15, 300));   // +15 defense
        availableItems.add(new Item("Fire Scroll", Item.ItemType.SCROLL, 50, 150));  // Fire damage
    }

    public void displayShop() {
        System.out.println("=== " + shopName + " ===");
        System.out.println("Welcome traveler! What would you like to buy?");
        System.out.println("--------------------------------------------");

        for (int i = 0; i < availableItems.size(); i++) {
            Item item = availableItems.get(i);
            System.out.println((i + 1) + ". " + item.getName()
                    + " - " + item.getValue() + " gold");
            System.out.println("   " + item.getType().getDescription());
            System.out.println("   Quantity: " + item.getQuantity());
            System.out.println();
        }
        System.out.println("0. Exit shop");
    }

    public boolean buyItem(int itemIndex, Hero hero) {
        if (itemIndex < 0 || itemIndex >= availableItems.size()) {
            System.out.println("Invalid item selection!");
            return false;
        }

        Item itemToBuy = availableItems.get(itemIndex);

        if (hero.getGold() < itemToBuy.getValue()) {
            System.out.println("Not enough gold! You need "
                    + itemToBuy.getValue() + " gold.");
            return false;
        }

        if (!hero.getInventory().hasSpace()) {
            System.out.println("Your inventory is full!");
            return false;
        }

        Item purchasedItem = new Item(itemToBuy.getName(),
                itemToBuy.getType(),
                itemToBuy.getValue(),
                1);

        if (hero.getInventory().addItem(purchasedItem)) {
            hero.addGold(-itemToBuy.getValue());
            System.out.println("You bought " + itemToBuy.getName() + " for "
                    + itemToBuy.getValue() + " gold!");
            return true;
        }

        return false;
    }

    public List<Item> getAvailableItems() {
        return availableItems;
    }

    public String getShopName() {
        return shopName;
    }
}
