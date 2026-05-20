package com.arktrainer;

import java.util.Scanner;

/**
 * Main application class for the ARK: Survival Evolved Trainer.
 * Provides a console-based interface to toggle various cheats.
 */
public class TrainerApp {

    private static final int ARK_PROCESS_ID = 12345; // Placeholder; replace with actual PID detection
    private static MemoryManager memoryManager;

    // Known memory offsets for ARK (version-specific, placeholder values)
    private static final long OFFSET_HEALTH = 0x00A1B2C0;
    private static final long OFFSET_STAMINA = 0x00A1B2C4;
    private static final long OFFSET_OXYGEN = 0x00A1B2C8;
    private static final long OFFSET_FOOD = 0x00A1B2CC;
    private static final long OFFSET_WATER = 0x00A1B2D0;

    public static void main(String[] args) {
        System.out.println("ARK: Survival Evolved Trainer v1.0");
        System.out.println("Connecting to process ID: " + ARK_PROCESS_ID);
        try {
            memoryManager = new MemoryManager(ARK_PROCESS_ID);
            System.out.println("Connected successfully.");
        } catch (Exception e) {
            System.err.println("Failed to connect to ARK process: " + e.getMessage());
            return;
        }

        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        while (running) {
            System.out.println("\n--- Trainer Menu ---");
            System.out.println("1. Set Health to Max (9999)");
            System.out.println("2. Set Stamina to Max (9999)");
            System.out.println("3. Set Oxygen to Max (9999)");
            System.out.println("4. Set Food to Max (9999)");
            System.out.println("5. Set Water to Max (9999)");
            System.out.println("6. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            try {
                switch (choice) {
                    case 1 -> setStat(OFFSET_HEALTH, 9999.0f);
                    case 2 -> setStat(OFFSET_STAMINA, 9999.0f);
                    case 3 -> setStat(OFFSET_OXYGEN, 9999.0f);
                    case 4 -> setStat(OFFSET_FOOD, 9999.0f);
                    case 5 -> setStat(OFFSET_WATER, 9999.0f);
                    case 6 -> running = false;
                    default -> System.out.println("Invalid option.");
                }
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
            }
        }
        scanner.close();
        memoryManager.close();
        System.out.println("Trainer exited.");
    }

    /**
     * Sets a stat at a given memory offset to a specified value.
     *
     * @param offset The memory offset relative to the base address.
     * @param value  The new float value for the stat.
     */
    private static void setStat(long offset, float value) {
        memoryManager.writeFloat(offset, value);
        System.out.println("Stat at offset " + Long.toHexString(offset) + " set to " + value);
    }
}
