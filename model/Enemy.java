package model;

import java.awt.Point;
import java.io.Serializable;

public class Enemy extends Character implements Serializable {

    private static final long serialVersionUID = 1L;

    private int experienceReward;
    private int goldReward;
    private Point position;
    private final int size = 30;

    public Enemy(String name, int health, int attack, int defense, int speed,
            int experienceReward, int goldReward, int x, int y) {
        super(name, health, attack, defense, speed);
        this.experienceReward = experienceReward;
        this.goldReward = goldReward;
        this.position = new Point(x, y);
    }

    @Override
    public void act() {
        System.out.println(name + " growls menacingly!");
    }

    public int attackHero(Hero hero) {
        int damage = attack + (int) (Math.random() * 3);
        System.out.println(name + " attacks " + hero.getName() + "!");
        int actualDamage = hero.takeDamage(damage);
        return actualDamage;
    }

    public int getExperienceReward() {
        return experienceReward;
    }

    public int getGoldReward() {
        return goldReward;
    }

    public int getX() {
        return position.x;
    }

    public int getY() {
        return position.y;
    }

    public Point getPosition() {
        return position;
    }

    public int getSize() {
        return size;
    }

    public boolean collidesWith(int px, int py, int psize) {
        int enemyX = position.x;
        int enemyY = position.y;
        int enemySize = this.size;

        return px < enemyX + enemySize
                && px + psize > enemyX
                && py < enemyY + enemySize
                && py + psize > enemyY;
    }

    @Override
    public void displayStatus() {
        System.out.println("=== ENEMY: " + name + " ===");
        System.out.println("Health: " + health + "/" + maxHealth);
        System.out.println("Attack: " + attack);
        System.out.println("Defense: " + defense);
        System.out.println("Reward: " + experienceReward + " XP, " + goldReward + " Gold");
    }

}
