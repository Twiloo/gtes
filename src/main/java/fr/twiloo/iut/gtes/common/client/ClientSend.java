package fr.twiloo.iut.gtes.common.client;

import fr.twiloo.iut.gtes.common.model.Event;
import fr.twiloo.iut.gtes.common.model.dto.Request;
import fr.twiloo.iut.gtes.common.model.dto.Response;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public final class ClientSend {
    private final ObjectOutputStream out;

    public ClientSend(ObjectOutputStream out) {
        this.out = out;
    }

    public void sendEvent(Event<?> event) {
        try {
            out.writeObject(event);
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException("Failed to send request or read response", e);
        }
    }
}
