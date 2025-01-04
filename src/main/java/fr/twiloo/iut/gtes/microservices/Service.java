package fr.twiloo.iut.gtes.microservices;

import fr.twiloo.iut.gtes.common.Config;
import fr.twiloo.iut.gtes.common.client.Client;
import fr.twiloo.iut.gtes.common.client.EventDispatcher;
import fr.twiloo.iut.gtes.common.model.Event;

import java.io.IOException;

import static java.lang.System.out;

public abstract class Service implements EventDispatcher {
    protected final Client client;

    protected Service() throws IOException {
        client = new Client(getConfig(), this);
    }

    @Override
    public void dispatch(Event<?> event) {
        out.println("Event " + event.type() + " received");
    }

    protected void sendEvent(Event<?> event) {
        out.println("Sending event: " + event.type());
        client.sendEvent(event);
    }

    public void close() {
        try {
            client.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    abstract protected Config getConfig();
}
