package model;

public interface Fightable {
    void attack(Character target);
    void useSkill(Skill skill, Character target);
    void defend();
}