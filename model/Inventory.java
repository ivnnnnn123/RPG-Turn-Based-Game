package model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Inventory implements Serializable {

    private static final long serialVersionUID = 1L;

    private Map<String, Item> items;
    private int capacity;
    private int gold;

    public Inventory(int capacity) {
        this.items = new HashMap<>();
        this.capacity = capacity;
        this.gold = 100; // cat avem la inceput
    }

    public boolean addItem(Item item) {
        if (items.size() >= capacity) {
            System.out.println("Inventory is full!");
            return false;
        }

        String key = item.getName() + "_" + item.getType();
        if (items.containsKey(key)) {
            Item existing = items.get(key);
            existing.addQuantity(item.getQuantity());
            System.out.println("Added " + item.getQuantity() + " " + item.getName() + "(s)");
        } else {
            items.put(key, item);
            System.out.println("Added new items: " + item.getName());
        }
        return true;
    }

    public boolean removeItem(String itemName, int quantity) {
        for (String key : items.keySet()) {
            Item item = items.get(key);
            if (item.getName().equalsIgnoreCase(itemName)) {
                if (item.getQuantity() >= quantity) {
                    item.addQuantity(-quantity);
                    if (item.getQuantity() <= 0) {
                        items.remove(key);
                    }
                    System.out.println("Removed " + quantity + " " + itemName + "(s)");
                    return true;
                } else {
                    System.out.println("Not enough " + itemName + " in inventory!");
                    return false;
                }
            }
        }
        System.out.println("Item not found: " + itemName);
        return false;
    }

    public Item getItem(String itemName) {
        for (String key : items.keySet()) {
            Item item = items.get(key);
            if (item.getName().equalsIgnoreCase(itemName)) {
                return item;
            }
        }
        return null;
    }

    public void useItem(String itemName, Hero hero) {
        Item item = getItem(itemName);
        if (item != null) {
            String message = item.use(hero);
            System.out.println(message);
            removeItem(itemName, 1);
        } else {
            System.out.println("Item not found: " + itemName);
        }
    }

    public void displayInventory() {
        System.out.println("=== INVENTORY ===");
        System.out.println("Gold: " + gold);
        System.out.println("Capacity: " + items.size() + "/" + capacity);
        System.out.println("Items:");

        if (items.isEmpty()) {
            System.out.println("  (Empty)");
        } else {
            for (Item item : items.values()) {
                System.out.println("  - " + item);
            }
        }
    }

    public Map<String, Item> getItems() {
        return items;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public void addGold(int amount) {
        this.gold += amount;
    }

    public boolean removeGold(int amount) {
        if (gold >= amount) {
            gold -= amount;
            return true;
        }
        return false;
    }

    public int getItemCount() {
        int count = 0;
        for (Item item : items.values()) {
            count += item.getQuantity();
        }
        return count;
    }

    public boolean hasSpace() {
        return items.size() < capacity;
    }

}
