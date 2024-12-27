package fr.twiloo.iut.gtes.eventbus;

import fr.twiloo.iut.gtes.common.model.Event;

import java.io.IOException;
import java.util.ArrayList;

import static java.lang.System.out;

public final class EventBus {
    private final int port;
    private final ArrayList<Subscriber> subscribers = new ArrayList<>();
    private final Connection connection;

    public EventBus(int port) throws IOException {
        this.port = port;
        connection = new Connection(this);
        new Thread(connection).start();
        out.println("Eventbus started");
    }

    public void addSubscriber(Subscriber subscriber) {
        subscribers.add(subscriber);
    }

    public void disconnectSubscriber(Subscriber subscriber) throws IOException {
        subscriber.closeSubscriber();
        subscribers.remove(subscriber);
    }

    public void dispatch(Event<?> event) throws IOException {
        for (Subscriber subscriber : subscribers) {
            subscriber.notify(event);
        }
    }

    public int getPort() {
        return port;
    }

    public void stop() throws IOException {
        for (Subscriber subscriber : subscribers) {
            subscriber.closeSubscriber();
        }
        connection.stop();
        System.out.println("Service stopped");
    }
}
