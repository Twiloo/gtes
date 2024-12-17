package fr.twiloo.iut.gtes.microservices;

import fr.twiloo.iut.gtes.common.Request;
import fr.twiloo.iut.gtes.common.Response;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public final class ConnectedClient<R extends Request<?>, ER extends Response<?>> implements Runnable {
    private final CallableService<R, ER> service;
    private final Socket socket;
    private final ObjectOutputStream out;
    private ObjectInputStream in;

    public ConnectedClient(CallableService<R, ER> service, Socket socket) throws IOException {
        this.service = service;
        this.socket = socket;
        this.out = new ObjectOutputStream(socket.getOutputStream());
    }

    @Override
    public void run() {
        try {
            in = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        while (true) {
            Object request;
            try {
                request = in.readObject();
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }

            boolean correctRequest = (request instanceof Request<?> && ((Request<?>) request).getPayload() != null) && ((Request<?>) request).getAction() != null;
            if (!correctRequest) {
                try {
                    service.disconnectClient(this);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                break;
            }

            try {
                @SuppressWarnings("unchecked") // request getPayload should always be of type P
                ER response = service.run((R) request);
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
