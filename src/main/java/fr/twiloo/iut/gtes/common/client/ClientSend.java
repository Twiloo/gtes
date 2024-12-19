package fr.twiloo.iut.gtes.common.client;

import fr.twiloo.iut.gtes.common.model.dto.Request;
import fr.twiloo.iut.gtes.common.model.dto.Response;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public final class ClientSend<R extends Request<?>, ER extends Response<?>> {
    private final ObjectOutputStream out;
    private final ObjectInputStream in;

    public ClientSend(ObjectOutputStream out, ObjectInputStream in) {
        this.out = out;
        this.in = in;
    }

    public ER sendRequest(R request) {
        try {
            out.writeObject(request);
            out.flush();
            Object response = in.readObject();
            if (response instanceof Response<?>) {
                try {
                    return (ER) response;
                } catch (ClassCastException e) {
                    throw new IllegalStateException("Invalid response type received from service.");
                }
            } else {
                throw new IllegalStateException("Invalid response type received from service.");
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Failed to send request or read response", e);
        }
    }
}
