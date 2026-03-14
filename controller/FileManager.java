package controller;

import model.Hero;
import model.Enemy;
import model.Item;
import model.Skill;
import java.io.*;
import java.util.List;
import java.util.Date;

public class FileManager {

    private static final String SAVE_FILE = "game_save.dat";
    private static final String TEXT_FILE = "game_save.txt";
    private static final String BACKUP_FILE = "game_save_backup.dat";

    public static class FullGameState implements Serializable {

        private static final long serialVersionUID = 1L;

        private Hero hero;
        private List<Enemy> enemies;
        private int playerX;
        private int playerY;
        private String timestamp;
        private int enemiesDefeated;

        public FullGameState(Hero hero, List<Enemy> enemies, int playerX, int playerY) {
            this.hero = hero;
            this.enemies = enemies;
            this.playerX = playerX;
            this.playerY = playerY;
            this.timestamp = new Date().toString();
            this.enemiesDefeated = calculateDefeatedEnemies(enemies);
        }

        private int calculateDefeatedEnemies(List<Enemy> enemies) {
            int count = 0;
            for (Enemy enemy : enemies) {
                if (!enemy.isAlive()) {
                    count++;
                }
            }
            return count;
        }

        public int getAliveEnemiesCount() {
            int count = 0;
            for (Enemy enemy : enemies) {
                if (enemy.isAlive()) {
                    count++;
                }
            }
            return count;
        }

        public Hero getHero() {
            return hero;
        }

        public List<Enemy> getEnemies() {
            return enemies;
        }

        public int getPlayerX() {
            return playerX;
        }

        public int getPlayerY() {
            return playerY;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public int getEnemiesDefeated() {
            return enemiesDefeated;
        }

        @Override
        public String toString() {
            return "GameState [Hero=" + hero.getName()
                    + ", Level=" + hero.getLevel()
                    + ", Position=(" + playerX + "," + playerY + ")"
                    + ", Enemies=" + enemies.size()
                    + ", Defeated=" + enemiesDefeated
                    + ", Timestamp=" + timestamp + "]";
        }
    }

    public static class GameState implements Serializable {

        private static final long serialVersionUID = 1L;
        private Hero hero;
        private List<Enemy> enemies;

        public GameState(Hero hero, List<Enemy> enemies) {
            this.hero = hero;
            this.enemies = enemies;
        }

        public Hero getHero() {
            return hero;
        }

        public List<Enemy> getEnemies() {
            return enemies;
        }
    }

    // Salveaza jocul intr-un fisier binar
    public static void saveFullGame(Hero hero, List<Enemy> enemies, int playerX, int playerY)
            throws IOException {

        createBackup();

        FullGameState state = new FullGameState(hero, enemies, playerX, playerY);

        try (ObjectOutputStream oos = new ObjectOutputStream(
                new BufferedOutputStream(new FileOutputStream(SAVE_FILE)))) {

            oos.writeObject(state);
            System.out.println("[FileManager] Full game state saved to: " + SAVE_FILE);
            System.out.println("[FileManager] " + state.toString());

        } catch (IOException e) {
            System.err.println("[FileManager] Error saving binary file: " + e.getMessage());
            throw e;
        }

        saveToTextFile(hero, TEXT_FILE);

        System.out.println("[FileManager] Game saved successfully!");
    }

    public static FullGameState loadFullGame() throws IOException, ClassNotFoundException {
        File file = new File(SAVE_FILE);

        if (!file.exists() || file.length() == 0) {
            System.out.println("[FileManager] No save file found. Starting fresh game.");
            return null; // Return null pentru a începe joc nou
        }

        if (file.length() == 0) {
            System.out.println("[FileManager] Save file is empty: " + SAVE_FILE);
            return null;
        }

        try (ObjectInputStream ois = new ObjectInputStream(
                new BufferedInputStream(new FileInputStream(SAVE_FILE)))) {

            FullGameState state = (FullGameState) ois.readObject();
            System.out.println("[FileManager] Full game state loaded from: " + SAVE_FILE);
            System.out.println("[FileManager] " + state.toString());

            return state;

        } catch (FileNotFoundException e) {
            System.err.println("[FileManager] Save file not found: " + e.getMessage());
            throw e;
        } catch (IOException e) {
            System.err.println("[FileManager] Error reading save file: " + e.getMessage());
            throw e;
        } catch (ClassNotFoundException e) {
            System.err.println("[FileManager] Invalid save file format: " + e.getMessage());
            throw e;
        }
    }

    public static void saveToTextFile(Hero hero, String filename) throws IOException {
        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(filename)))) {

            writer.println("=== RPG GAME SAVE FILE ===");
            writer.println("Saved at: " + new Date());
            writer.println();

            writer.println("=== HERO STATISTICS ===");
            writer.println("Name: " + hero.getName());
            writer.println("Level: " + hero.getLevel());
            writer.println("Experience: " + hero.getExperience() + " / " + (hero.getLevel() * 100));
            writer.println("Health: " + hero.getHealth() + " / " + hero.getMaxHealth());
            writer.println("Mana: " + hero.getMana() + " / " + hero.getMaxMana());
            writer.println("Attack: " + hero.getAttack());
            writer.println("Defense: " + hero.getDefense());
            writer.println("Speed: " + hero.getSpeed());
            writer.println("Gold: " + hero.getGold());
            writer.println();

            writer.println("=== SKILLS ===");
            List<Skill> skills = hero.getAvailableSkills();
            if (skills.isEmpty()) {
                writer.println("No skills learned");
            } else {
                for (Skill skill : skills) {
                    writer.println("- " + skill.getName() + ": " + skill.getDescription());
                }
            }
            writer.println();

            writer.println("=== INVENTORY ===");
            writer.println("Capacity: " + hero.getInventory().getItemCount()
                    + " / " + hero.getInventory().getCapacity());
            writer.println("Gold: " + hero.getInventory().getGold());

            if (hero.getInventory().getItems().isEmpty()) {
                writer.println("Inventory is empty");
            } else {
                writer.println("Items:");
                for (Item item : hero.getInventory().getItems().values()) {
                    writer.println("- " + item.getName()
                            + " (x" + item.getQuantity() + ")"
                            + " - " + item.getType().getDescription());
                }
            }

            System.out.println("[FileManager] Text save file created: " + filename);

        } catch (IOException e) {
            System.err.println("[FileManager] Error creating text file: " + e.getMessage());
            throw e;
        }
    }

    public static GameState loadGame() throws IOException, ClassNotFoundException {
        FullGameState fullState = loadFullGame();
        if (fullState != null) {
            return new GameState(fullState.getHero(), fullState.getEnemies());
        }
        return null;
    }

    public static boolean saveFileExists() {
        File file = new File(SAVE_FILE);
        return file.exists() && file.length() > 0;
    }

    public static boolean deleteSaveFile() {
        File file = new File(SAVE_FILE);
        if (file.exists()) {
            boolean deleted = file.delete();
            if (deleted) {
                System.out.println("[FileManager] Save file deleted: " + SAVE_FILE);
            }
            return deleted;
        }
        return false;
    }

    public static String getSaveFileInfo() {
        File file = new File(SAVE_FILE);
        if (!file.exists()) {
            return "No save file found";
        }

        return "Save file: " + SAVE_FILE
                + "\nSize: " + file.length() + " bytes"
                + "\nLast modified: " + new Date(file.lastModified());
    }

    public static String readTextSaveFile() throws IOException {
        File file = new File(TEXT_FILE);
        if (!file.exists()) {
            return "No text save file found";
        }

        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        }

        return content.toString();
    }

    private static void createBackup() {
        File source = new File(SAVE_FILE);
        File backup = new File(BACKUP_FILE);

        if (source.exists()) {
            try (FileInputStream fis = new FileInputStream(source); FileOutputStream fos = new FileOutputStream(backup)) {

                byte[] buffer = new byte[1024];
                int length;
                while ((length = fis.read(buffer)) > 0) {
                    fos.write(buffer, 0, length);
                }

                System.out.println("[FileManager] Backup created: " + BACKUP_FILE);

            } catch (IOException e) {
                System.err.println("[FileManager] Failed to create backup: " + e.getMessage());
            }
        }
    }

    public static boolean restoreBackup() {
        File backup = new File(BACKUP_FILE);
        File save = new File(SAVE_FILE);

        if (backup.exists()) {
            try (FileInputStream fis = new FileInputStream(backup); FileOutputStream fos = new FileOutputStream(save)) {

                byte[] buffer = new byte[1024];
                int length;
                while ((length = fis.read(buffer)) > 0) {
                    fos.write(buffer, 0, length);
                }

                System.out.println("[FileManager] Backup restored from: " + BACKUP_FILE);
                return true;

            } catch (IOException e) {
                System.err.println("[FileManager] Failed to restore backup: " + e.getMessage());
                return false;
            }
        }

        return false;
    }

}
