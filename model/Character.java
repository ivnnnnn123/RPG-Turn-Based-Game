package model;

import java.io.Serializable;

public abstract class Character implements Serializable {

    private static final long serialVersionUID = 1L;

    protected String name;
    protected int health;
    protected int maxHealth;
    protected int attack;
    protected int defense;
    protected int speed;
    protected int level;
    protected int experience;
    protected int gold;

    public Character(String name, int health, int attack, int defense, int speed) {
        this.name = name;
        this.health = health;
        this.maxHealth = health;
        this.attack = attack;
        this.defense = defense;
        this.speed = speed;
        this.level = 1;
        this.experience = 0;
        this.gold = 0;
    }

    public abstract void act();

    public void displayStatus() {
        System.out.println("=== " + name + " ===");
        System.out.println("Level: " + level);
        System.out.println("Health: " + health + "/" + maxHealth);
        System.out.println("Attack: " + attack);
        System.out.println("Defense: " + defense);
        System.out.println("Speed: " + speed);
        System.out.println("Experience: " + experience + "/" + getExpForNextLevel());
        System.out.println("Gold: " + gold);
    }

    public int takeDamage(int damage) {
        int actualDamage = Math.max(1, damage - defense);
        health -= actualDamage;
        if (health < 0) {
            health = 0;
        }
        System.out.println(name + " takes " + actualDamage + " damage!");
        return actualDamage; // Returnează damage-ul real
    }

    public void heal(int amount) {
        health += amount;
        if (health > maxHealth) {
            health = maxHealth;
        }
        System.out.println(name + " heals for " + amount + " health!");
    }

    public boolean isAlive() {
        return health > 0;
    }

    public void gainExperience(int exp) {
        experience += exp;
        System.out.println(name + " gains " + exp + " experience!");

        while (experience >= getExpForNextLevel()) {
            levelUp();
        }
    }

    protected void levelUp() {
        experience = getExperience() - getExpForNextLevel();
        level++;
        maxHealth += 20;
        health = maxHealth;
        attack += 5;
        defense += 2;
        speed += 1;

        System.out.println("=== " + name + " LEVEL UP! ===");
        System.out.println("Now level " + level + "!");
        System.out.println("Stats increased!");
    }

    private int getExpForNextLevel() {
        return level * 100; //formula
    }

    public String getName() {
        return name;
    }

    public int getHealth() {
        return health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getAttack() {
        return attack;
    }

    public int getDefense() {
        return defense;
    }

    public int getSpeed() {
        return speed;
    }

    public int getLevel() {
        return level;
    }

    public int getExperience() {
        return experience;
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

    // Ptr lupta
    public void setHealth(int health) {
        this.health = Math.min(health, maxHealth);
        if (this.health < 0) {
            this.health = 0;
        }
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

}
