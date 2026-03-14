package main;

import view.MainFrame;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== RPG Turn-Based Game ===");
        System.out.println("Starting GUI application...");
        
        // Porneste aplicatia Swing pe Event Dispatch Thread
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                MainFrame frame = new MainFrame();
                frame.updateStatus("RPG Game Started Successfully!");
                frame.updateStatus("Controls: WASD to move, ENTER to fight, E at shop");
                frame.updateStatus("Save/Load buttons in bottom panel");
            }
        });
    }
}