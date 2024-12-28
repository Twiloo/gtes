package fr.twiloo.iut.gtes.eventbus;

import fr.twiloo.iut.gtes.common.EventType;
import fr.twiloo.iut.gtes.common.model.Event;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

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
        } finally {
            closeSubscriberSafely();
        }
    }

    private void listenProducedEvents() {
        while (!socket.isClosed()) {
            Object event;
            try {
                event = in.readObject();
            } catch (IOException e) {
                // Handle client disconnection
                System.err.println("Client disconnected : " + e.getMessage());
                closeSubscriberSafely();
                break;
            } catch (ClassNotFoundException e) {
                System.err.println("Received invalid event: " + e.getMessage());
                continue;
            }

            if (!(event instanceof Event<?> && ((Event<?>) event).type() != null)) {
                try {
                    eventBus.disconnectSubscriber(this);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                break;
            }

            try {
                eventBus.dispatch((Event<?>) event);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void makeSubscription() {
        Object subscription;
        try {
            subscription = in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        if (subscription instanceof List<?> && !((List<?>) subscription).isEmpty()) {
            for (Object eventType : (List<?>) subscription) {
                if (eventType instanceof EventType) {
                    subscribedEvents.add((EventType) eventType);
                }
            }
        }
    }

    public void notify(Event<?> event) throws IOException {
        if (event != null && event.type() != null && subscribedEvents.contains(event.type())) {
            out.writeObject(event);
            out.flush();
        }
    }

    private void closeSubscriberSafely() {
        try {
            if (!socket.isClosed())
                socket.close();
        } catch (IOException e) {
            System.err.println("Error closing socket: " + e.getMessage());
        }
        try {
            if (in != null)
                in.close();
        } catch (IOException e) {
            System.err.println("Error closing input stream: " + e.getMessage());
        }
        try {
            if (out != null)
                out.close();
        } catch (IOException e) {
            System.err.println("Error closing output stream: " + e.getMessage());
        }
    }

    public void closeSubscriber() throws IOException {
        in.close();
        out.close();
        socket.close();
    }
}
