package model;

import java.io.Serializable;

public class Item implements Serializable {

    private static final long serialVersionUID = 1L;

    public enum ItemType {
        POTION("Potion", "Restores health"),
        WEAPON("Weapon", "Increases attack"),
        ARMOR("Armor", "Increases defense"),
        SCROLL("Scroll", "Special effect");

        private final String name;
        private final String description;

        ItemType(String name, String description) {
            this.name = name;
            this.description = description;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }
    }

    private String name;
    private ItemType type;
    private int value;
    private int quantity;

    public Item(String name, ItemType type, int value) {
        this.name = name;
        this.type = type;
        this.value = value;
        this.quantity = 1;
    }

    public Item(String name, ItemType type, int value, int quantity) {
        this.name = name;
        this.type = type;
        this.value = value;
        this.quantity = quantity;
    }

    public String use(Hero hero) {
        if (quantity <= 0) {
            return "No more " + name + " left!";
        }

        String message = "";

        switch (type) {
            case POTION:
                if (name.toLowerCase().contains("mana")) {
                    int oldMana = hero.getMana();
                    hero.restoreMana(value);
                    int restored = Math.min(value, hero.getMaxMana() - oldMana);
                    message = hero.getName() + " uses " + name + " and restores " + restored + " MP!";
                } else {
                    int oldHealth = hero.getHealth();
                    hero.heal(value);
                    int healed = Math.min(value, hero.getMaxHealth() - oldHealth);
                    message = hero.getName() + " uses " + name + " and heals " + healed + " HP!";
                }
                break;

            case WEAPON:
                int oldAttack = hero.getAttack();
                hero.setAttack(oldAttack + value);
                message = hero.getName() + " equips " + name + "! Attack increased by " + value + "! (Now: " + hero.getAttack() + ")";
                break;

            case ARMOR:
                int oldDefense = hero.getDefense();
                hero.setDefense(oldDefense + value);
                message = hero.getName() + " equips " + name + "! Defense increased by " + value + "! (Now: " + hero.getDefense() + ")";
                break;

            case SCROLL:
                if (name.toLowerCase().contains("fire")) {
                    message = hero.getName() + " reads the " + name + "! A fiery explosion erupts!";
                } else {
                    message = hero.getName() + " reads the " + name + "! Magical energies swirl around!";
                }
                break;

            default:
                message = hero.getName() + " tries to use " + name + ", but nothing happens.";
        }

        quantity--;
        return message;
    }

    public String getName() {
        return name;
    }

    public ItemType getType() {
        return type;
    }

    public int getValue() {
        return value;
    }

    public int getQuantity() {
        return quantity;
    }

    public void addQuantity(int amount) {
        quantity += amount;
    }

    @Override
    public String toString() {
        return name + " [" + type.getName() + "] (x" + quantity + ") - "
                + type.getDescription() + " (" + value + ")";
    }
}
