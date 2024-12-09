package fr.twiloo.iut.gtes.microservices;

import fr.twiloo.iut.gtes.common.Request;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public final class ConnectedClient implements Runnable {
    private final CallableService service;
    private final Socket socket;
    private final ObjectOutputStream out;
    private ObjectInputStream in;

    public ConnectedClient(CallableService service, Socket socket) throws IOException {
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

            if (!(request instanceof Request)) {
                try {
                    service.disconnectClient(this);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                break;
            }

            try {
                Object response = service.run((Request) request);
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
