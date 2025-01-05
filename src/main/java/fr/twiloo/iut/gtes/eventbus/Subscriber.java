package fr.twiloo.iut.gtes.eventbus;

import fr.twiloo.iut.gtes.common.EventType;
import fr.twiloo.iut.gtes.common.model.Event;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import static java.lang.System.err;

public final class Subscriber implements Runnable {
    private final ArrayList<EventType> subscribedEvents = new ArrayList<>();
    private final EventBus eventBus;
    private final Socket socket;
    private final ObjectInputStream in;
    private final ObjectOutputStream out;

    public Subscriber(EventBus eventBus, Socket socket) throws IOException {
        this.eventBus = eventBus;
        this.socket = socket;
        this.in = new ObjectInputStream(socket.getInputStream());
        this.out = new ObjectOutputStream(socket.getOutputStream());
    }

    @Override
    public void run() {
        makeSubscription();
        try {
            listenProducedEvents();
        } catch (Exception ignored) { }
    }

    private void listenProducedEvents() {
        while (!socket.isClosed()) {
            Object event;
            try {
                event = in.readUnshared();
            } catch (IOException | ClassNotFoundException e) {
                err.println("Error while receiving event: " + e.getMessage());
                try {
                    eventBus.disconnectSubscriber(this);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                break;
            }

            if (!(event instanceof Event<?> && ((Event<?>) event).type() != null)) {
                try {
                    eventBus.disconnectSubscriber(this);
                } catch (IOException e) {
                    err.println("Error disconnecting subscriber: " + e.getMessage());
                }
                break;
            }

            try {
                eventBus.dispatch((Event<?>) event);
            } catch (IOException e) {
                err.println("Error dispatching event: " + e.getMessage());
            }
        }
    }

    private void makeSubscription() {
        try {
            Object subscription = in.readUnshared();
            if (subscription instanceof List<?> && !((List<?>) subscription).isEmpty()) {
                for (Object eventType : (List<?>) subscription) {
                    if (eventType instanceof EventType) {
                        subscribedEvents.add((EventType) eventType);
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            err.println("Error during subscription: " + e.getMessage());
        }
        System.out.println("New subscription: " + this);
    }

    public void notify(Event<?> event) throws IOException {
        if (event != null && event.type() != null && subscribedEvents.contains(event.type())) {
            System.out.println("Receiver : " + this.hashCode());
            out.writeObject(event);
            out.flush();
        }
    }

    public void closeSubscriberSafely() {
        try {
            if (!socket.isClosed())
                socket.close();
        } catch (IOException e) {
            err.println("Error closing socket: " + e.getMessage());
        }
        try {
            if (out != null)
                out.close();
        } catch (IOException e) {
            err.println("Error closing output stream: " + e.getMessage());
        }
    }

    @Override
    public String toString() {
        return this.hashCode() + ": " + subscribedEvents;
    }
}
