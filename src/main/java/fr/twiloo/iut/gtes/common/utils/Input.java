package fr.twiloo.iut.gtes.common.utils;

import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public final class Input {
    private static final Scanner sc = new Scanner(System.in);
    private static final BlockingQueue<String> inputQueue = new LinkedBlockingQueue<>();
    private static boolean running = true;

    public static void start() {
        Thread inputThread = new Thread(() -> {
            while (running) {
                String input = sc.nextLine();
                try {
                    inputQueue.put(input); // Put input into the queue
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
        inputThread.setDaemon(true);
        inputThread.start();
    }

    public static String next() throws InterruptedException {
        return inputQueue.take(); // Waits for and retrieves the next input
    }

    public static void stop() {
        running = false;
        sc.close();
    }
}
