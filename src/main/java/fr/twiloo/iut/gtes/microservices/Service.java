package fr.twiloo.iut.gtes.microservices;

import fr.twiloo.iut.gtes.common.Config;
import fr.twiloo.iut.gtes.common.client.Client;
import fr.twiloo.iut.gtes.common.client.EventDispatcher;
import fr.twiloo.iut.gtes.common.model.Event;

import java.io.IOException;

public abstract class Service implements EventDispatcher {
    private final Client client;

    protected Service() throws IOException {
        client = new Client(getConfig(), this);
    }

    protected void sendEvent(Event<?> event) {
        client.sendEvent(event);
    }

    public void stop() {
        try {
            client.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    abstract protected Config getConfig();
}
