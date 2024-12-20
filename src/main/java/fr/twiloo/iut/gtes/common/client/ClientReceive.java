package fr.twiloo.iut.gtes.common.client;

import fr.twiloo.iut.gtes.common.model.dto.Response;

import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * @param <R> Response class (here I use it as notification class because I don't want/need to complexify this anymore
 */
public final class ClientReceive<R extends Response<?>> implements Runnable {
    private final ObjectInputStream in;

    public ClientReceive(ObjectInputStream notificationIn) {
        this.in = notificationIn;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Object notification = in.readObject();
                boolean correctNotification = notification instanceof Response<?> &&
                        notification.getClass().equals(this.getClass().getDeclaredClasses()[0]);
                if (correctNotification) {
                    handleNotification((R) notification);
                } else {
                    throw new IllegalStateException("Invalid notification type received from service.");
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Notification listener stopped: " + e.getMessage());
        }
    }

    private void handleNotification(R notification) {
        System.out.println("Notification reçue : " + notification.getContent());
    }
}