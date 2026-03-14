package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Hero extends Character implements Fightable, Serializable {

    private static final long serialVersionUID = 1L;

    private HashMap<Skill, Integer> skills;
    private int mana;
    private int maxMana;
    private Inventory inventory;

    public Hero(String name) {
        super(name, 100, 15, 10, 12);
        this.mana = 50;
        this.maxMana = 50;
        this.skills = new HashMap<>();
        this.inventory = new Inventory(20);

        // Item uri de inceput
        inventory.addItem(new Item("Health Potion", Item.ItemType.POTION, 30, 3));
        inventory.addItem(new Item("Mana Potion", Item.ItemType.POTION, 20, 2));

        // Skill uri de inceput
        skills.put(Skill.FIREBALL, 1);
        System.out.println("Starting with Fireball skill!");

    }

    @Override
    public void act() {
        System.out.println(name + " is ready for action!");
    }

    @Override
    public void attack(Character target) {
        int damage = attack + (int) (Math.random() * 5);
        System.out.println(name + " attacks " + target.getName() + "!");
        target.takeDamage(damage);
    }

    @Override
    public void useSkill(Skill skill, Character target) {
        useSkillWithMessage(skill, target);
    }

    public String useSkillWithMessage(Skill skill, Character target) {
        if (!skills.containsKey(skill)) {
            return name + " doesn't know this skill!";
        }
        if (mana < skill.getManaCost()) {
            return "Not enough mana!";
        }

        mana = Math.max(0, mana - skill.getManaCost());
        String message = "";
        int skillLevel = skills.get(skill);

        int skillMultiplier = skillLevel;
        int heroLevelMultiplier = level;

        switch (skill) {
            case FIREBALL:
                int damage = skill.getDamage() + (attack / 2);
                int actualDamage = target.takeDamage(damage);
                message = name + " casts Fireball on " + target.getName() + "! Deals " + actualDamage + " damage!";
                break;

            case HEAL:
                int baseHealAmount = 30 + (defense * 2);
                int scaledHealAmount = baseHealAmount * skillMultiplier + (heroLevelMultiplier * 5);
                int oldHealth = health;
                heal(scaledHealAmount);
                int actualHeal = health - oldHealth;
                message = name + " casts Heal (Lvl " + skillLevel + ")! Restores " + actualHeal + " HP!";
                break;

            case SHIELD:
                int shieldBonus = 10 * skillMultiplier;
                increaseDefenseTemporarily(shieldBonus);
                message = name + " uses Shield (Lvl " + skillLevel + ")! Defense increased by " + shieldBonus + " for 1 turn!";
                break;

            case DOUBLE_STRIKE:
                int totalDamage = 0;
                int firstDamage = attack + (int) (Math.random() * 5);
                totalDamage += target.takeDamage(firstDamage);

                int secondDamage = (int) (attack * 0.8) + (int) (Math.random() * 5);
                totalDamage += target.takeDamage(secondDamage);
                message = name + " uses Double Strike! Deals " + totalDamage + " total damage!";
                break;

            default:
                message = "Skill not implemented yet!";
        }

        return message;
    }

    @Override
    public void defend() {
        System.out.println(name + " takes a defensive stance!");
        increaseDefenseTemporarily(5);
    }

    @Override
    protected void levelUp() {
        super.levelUp();

        learnNewSkillsOnLevelUp();
    }

    private void learnNewSkillsOnLevelUp() {
        System.out.println("Checking for new skills at level " + level + "...");

        // Deblocare skill uri la diferite nivele

        if (level >= 2 && !skills.containsKey(Skill.DOUBLE_STRIKE)) {
            learnSkill(Skill.DOUBLE_STRIKE);
            System.out.println("Double Strike unlocked at level 2!");
        }

        if (level >= 4 && !skills.containsKey(Skill.HEAL)) {
            learnSkill(Skill.HEAL);
            System.out.println("Heal unlocked at level 4!");
        }

        if (level >= 6 && !skills.containsKey(Skill.SHIELD)) {
            learnSkill(Skill.SHIELD);
            System.out.println("Shield unlocked at level 6  !");
        }

        // La nivelul 10, toate skill urile existente isi cresc nivelul cu 1
        if (level == 10) {
            for (Skill skill : skills.keySet()) {
                int currentLevel = skills.get(skill);
                skills.put(skill, currentLevel + 1);
                System.out.println(skill.getName() + " automatically improved to level " + (currentLevel + 1) + "!");
            }
            System.out.println("All skills improved at level 10!");
        }

        // La fiecare nivel incepand cu nivelul 11, skill urile existente se pot inbunatati
        if (level >= 11) {
            improveExistingSkills();
        }
    }

    private void improveExistingSkills() {
        if (level >= 11 && (level - 11) % 2 == 0) {
            for (Skill skill : skills.keySet()) {
                int currentLevel = skills.get(skill);
                if (currentLevel < 10) {
                    int newLevel = currentLevel + 1;
                    skills.put(skill, newLevel);
                    System.out.println(skill.getName() + " improved to level " + newLevel + "!");
                }
            }
        }
    }

    public void learnSkill(Skill skill) {
        if (!skills.containsKey(skill)) {
            skills.put(skill, 1);
            System.out.println(name + " learned " + skill.getName() + "!");
        } else {
            int currentLevel = skills.get(skill);
            skills.put(skill, currentLevel + 1);
            System.out.println(skill.getName() + " improved to level " + (currentLevel + 1) + "!");
        }
    }

    public ArrayList<Skill> getAvailableSkills() {
        return new ArrayList<>(skills.keySet());
    }

    public void restoreMana(int amount) {
        mana += amount;
        if (mana > maxMana) {
            mana = maxMana;
        }
    }

    public Inventory getInventory() {
        return inventory;
    }

    @Override
    public int getGold() {
        return inventory.getGold();
    }

    @Override
    public void setGold(int gold) {
        inventory.setGold(gold);
    }

    @Override
    public void addGold(int amount) {
        inventory.addGold(amount);
    }

    public void useItem(String itemName) {
        inventory.useItem(itemName, this);
    }

    public int getMana() {
        return mana;
    }

    public void setMana(int mana) {
        this.mana = Math.max(0, Math.min(mana, maxMana));
    }

    public int getMaxMana() {
        return maxMana;
    }

    public int getSkillLevel(Skill skill) {
        return skills.getOrDefault(skill, 0);
    }

    @Override
    public void displayStatus() {
        super.displayStatus();
        System.out.println("Mana: " + mana + "/" + maxMana);
        System.out.println("Skills: " + skills.size());
        System.out.println("Inventory items: " + inventory.getItemCount() + "/" + inventory.getCapacity());
    }

    private int tempDefenseBonus = 0;

    private int defenseTurnsRemaining = 0;

    public void increaseDefenseTemporarily(int bonus) {
        if (tempDefenseBonus > 0) {
            this.defense -= tempDefenseBonus;
        }

        tempDefenseBonus = bonus;
        this.defense += bonus;
        defenseTurnsRemaining = 1;
        System.out.println(name + "'s defense increased by " + bonus + " for 1 turn! (Now: " + this.defense + ")");
    }

    public void resetTemporaryDefense() {
        if (tempDefenseBonus != 0 && defenseTurnsRemaining > 0) {
            defenseTurnsRemaining--;
            if (defenseTurnsRemaining <= 0) {
                this.defense -= tempDefenseBonus;
                System.out.println(name + "'s defense returns to normal. (Now: " + this.defense + ")");
                tempDefenseBonus = 0;
            }
        }
    }

}
