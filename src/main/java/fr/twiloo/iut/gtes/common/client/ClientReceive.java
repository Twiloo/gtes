package fr.twiloo.iut.gtes.common.client;

import fr.twiloo.iut.gtes.common.model.Event;

import java.io.IOException;
import java.io.ObjectInputStream;

public final class ClientReceive implements Runnable {
    private final ObjectInputStream in;
    private final EventDispatcher eventDispatcher;

    public ClientReceive(ObjectInputStream in, EventDispatcher eventDispatcher) {
        this.in = in;
        this.eventDispatcher = eventDispatcher;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Object event = in.readObject();
                if (event instanceof Event<?>) {
                    handleNotification((Event<?>) event);
                } else {
                    throw new IllegalStateException("Invalid event received from event bus.");
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Event listener stopped: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void handleNotification(Event<?> event) throws Exception {
        eventDispatcher.dispatch(event);
    }
}