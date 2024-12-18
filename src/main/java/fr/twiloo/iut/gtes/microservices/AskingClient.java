package fr.twiloo.iut.gtes.microservices;

import fr.twiloo.iut.gtes.common.Request;
import fr.twiloo.iut.gtes.common.Response;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public final class AskingClient<R extends Request<?>, ER extends Response<?>> implements Runnable {
    private final CallableService<R, ER> service;
    private final Socket socket;
    private final ObjectOutputStream out;
    private final ObjectInputStream in;

    public AskingClient(CallableService<R, ER> service, Socket socket) throws IOException, RuntimeException {
        this.service = service;
        this.socket = socket;
        this.out = new ObjectOutputStream(socket.getOutputStream());
        try {
            in = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        while (true) {
            Object request;
            try {
                request = in.readObject();
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }

            boolean correctRequest = request instanceof Request<?> &&
                    ((Request<?>) request).getAction() != null &&
                    request.getClass().equals(service.getClass().getDeclaredClasses()[0]); // request should extend R class
            if (!correctRequest) {
                try {
                    service.disconnectAskingClient(this);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                break;
            }

            try {
                @SuppressWarnings("unchecked") // request getPayload should always be of type P
                ER response = service.dispatch((R) request);
                if (response != null)
                    out.writeObject(response);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void closeClient() throws IOException {
        in.close();
        out.close();
        socket.close();
    }
}
