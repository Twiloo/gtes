package fr.twiloo.iut.gtes.common.client;

import fr.twiloo.iut.gtes.common.Request;
import fr.twiloo.iut.gtes.common.Response;

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
            boolean correctResponse = response instanceof Response<?> &&
                    response.getClass().equals(this.getClass().getDeclaredClasses()[1]);
            if (correctResponse) {
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
