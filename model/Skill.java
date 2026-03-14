package model;

public enum Skill {
    FIREBALL("Fireball", 25, 10, "Deals fire damage"),
    HEAL("Heal", 0, 15, "Restores health"),
    SHIELD("Shield", 0, 20, "Increases defense"),
    DOUBLE_STRIKE("Double Strike", 30, 25, "Attacks twice");

    private final String name;
    private final int damage;
    private final int manaCost;
    private final String description;

    Skill(String name, int damage, int manaCost, String description) {
        this.name = name;
        this.damage = damage;
        this.manaCost = manaCost;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public int getDamage() {
        return damage;
    }

    public int getManaCost() {
        return manaCost;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return name + " (Cost: " + manaCost + ", DMG: " + damage + ") - " + description;
    }
}
