# RPG-Turn-Based-Game
## Overview:
This project is a 2D turn-based RPG game developed in Java using Swing.
The player controls a hero on a map, explores the environment, fights enemies in turn-based combat, collects items, and purchases equipment from a shop.
<br>
The objective of the game is to defeat all enemies on the map while managing the hero’s health, mana, inventory, and skills.
The game demonstrates concepts from Object-Oriented Programming (OOP) such as inheritance, interfaces, encapsulation, polymorphism, and modular design using the MVC pattern.

## Game Features:

### Map Exploration
The player moves the hero on the map using W, A, S, D keys
<br>
The map contains: enemies, trees (obstacles), a shop.
<br>
The hero can interact with enemies and the shop.

### Combat System: 
The game uses a turn-based combat system where the player and enemy take turns performing actions.
<br>
Available combat actions:
<br>
-Attack – Basic attack that deals damage.
<br>
-Skill – Use special abilities that consume mana.
<br>
-Defend – Temporarily increases defense.
<br>
-Item – Use items from inventory.
<br>
-Flee – Attempt to escape from combat.
<br>
Enemy behavior is automated and enemies attack after the player's turn.

### Hero System
The hero has the following attributes: health, mana, attack, defense, speed, level, experience, gold.
<br>
The hero gains experience points (XP) after defeating enemies and levels up, improving stats and unlocking new abilities.

### Skills
The hero can learn and use multiple skills:
<br>
-Fireball	Deals fire damage to the enemy
<br>
-Heal	Restores hero health
<br>
-Shield	Temporarily increases defense
<br>
-Double Strike	Performs two attacks in one turn
<br>
Skills consume mana points (MP) and become available as the hero levels up.

### Inventory System
The hero has an inventory that stores items such as:
<br>
-Health potions
<br>
-Mana potions
<br>
-Weapons
<br>
-Armor
<br>
-Scrolls
<br>
<br>
Items can:
<br>
-restore health or mana
<br>
-increase attack or defense
<br>
-produce special effects during combat.
<br>

### Shop System
The game contains a shop where the player can buy items using gold earned from defeating enemies.
<br>
Available shop items include:
<br>
-Health Potion
<br>
-Mana Potion
<br>
-Great Health Potion
<br>
-Iron Sword
<br>
-Steel Armor
<br>
-Fire Scroll
<br>
<br>

Purchased items are automatically added to the hero’s inventory.

### Enemies
Different types of enemies appear on the map: Goblin, Orc, Skeleton, Slime, Wolf
<br>
Each enemy has different: health, attack, defense, speed, rewards (XP and gold)
<br>
When the hero defeats an enemy, rewards are automatically granted.

### Save and Load System
The game supports saving and loading the game state.
<br>
Saved data includes: hero statistics, hero inventory, hero skills, hero position, enemies remaining on the map
<br>
Game data is stored using Java serialization in a binary file.

### Controls:
Key	Action:
<br>
W	Move Up
<br>
S	Move Down
<br>
A	Move Left
<br>
D	Move Right
<br>
ENTER	Start combat with nearby enemy
<br>
E	Open shop

The project follows a Model–View–Controller (MVC) architecture:
<br>
Model – game logic and data
<br>
View – graphical interface
<br>
Controller – game saving and file management
<br>
<strong>Technologies Used:</strong> Java, Java Swing (GUI), Object-Oriented Programming, Serialization for game saving, Event-driven programming
