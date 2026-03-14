# RPG-Turn-Based-Game
Overview:
This project is a 2D turn-based RPG game developed in Java using Swing.
The player controls a hero on a map, explores the environment, fights enemies in turn-based combat, collects items, and purchases equipment from a shop.
The objective of the game is to defeat all enemies on the map while managing the hero’s health, mana, inventory, and skills.
The game demonstrates concepts from Object-Oriented Programming (OOP) such as inheritance, interfaces, encapsulation, polymorphism, and modular design using the MVC pattern.

Game Features:

Map Exploration
The player moves the hero on the map using W, A, S, D keys
The map contains: enemies, trees (obstacles), a shop.
The hero can interact with enemies and the shop.

Combat System: the game uses a turn-based combat system where the player and enemy take turns performing actions.
Available combat actions:
-Attack – Basic attack that deals damage.
-Skill – Use special abilities that consume mana.
-Defend – Temporarily increases defense.
-Item – Use items from inventory.
-Flee – Attempt to escape from combat.
Enemy behavior is automated and enemies attack after the player's turn.

Hero System
The hero has the following attributes: health, mana, attack, defense, speed, level, experience, gold.
The hero gains experience points (XP) after defeating enemies and levels up, improving stats and unlocking new abilities.

Skills
The hero can learn and use multiple skills:
-Fireball	Deals fire damage to the enemy
-Heal	Restores hero health
-Shield	Temporarily increases defense
-Double Strike	Performs two attacks in one turn
Skills consume mana points (MP) and become available as the hero levels up.

Inventory System
The hero has an inventory that stores items such as:
-Health potions
-Mana potions
-Weapons
-Armor
-Scrolls

Items can:
-restore health or mana
-increase attack or defense
-produce special effects during combat.

Shop System
The game contains a shop where the player can buy items using gold earned from defeating enemies.
Available shop items include:
-Health Potion
-Mana Potion
-Great Health Potion
-Iron Sword
-Steel Armor
-Fire Scroll

Purchased items are automatically added to the hero’s inventory.

Enemies
Different types of enemies appear on the map: Goblin, Orc, Skeleton, Slime, Wolf
Each enemy has different: health, attack, defense, speed, rewards (XP and gold)
When the hero defeats an enemy, rewards are automatically granted.

Save and Load System
The game supports saving and loading the game state.
Saved data includes: hero statistics, hero inventory, hero skills, hero position, enemies remaining on the map
Game data is stored using Java serialization in a binary file.

Controls:
Key	Action:
W	Move Up
S	Move Down
A	Move Left
D	Move Right
ENTER	Start combat with nearby enemy
E	Open shop

The project follows a Model–View–Controller (MVC) architecture:
Model – game logic and data
View – graphical interface
Controller – game saving and file management
Technologies Used: Java, Java Swing (GUI), Object-Oriented Programming, Serialization for game saving, Event-driven programming
