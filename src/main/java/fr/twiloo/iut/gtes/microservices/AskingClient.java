package fr.twiloo.iut.gtes.microservices;

import fr.twiloo.iut.gtes.common.model.dto.Request;
import fr.twiloo.iut.gtes.common.model.dto.Response;

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
        try {
            while (!socket.isClosed()) {
                Object request;
                try {
                    request = in.readObject();
                } catch (IOException e) {
                    // Handle client disconnection
                    System.err.println("Client disconnected : " + e.getMessage());
                    closeClientSafely();
                    break;
                } catch (ClassNotFoundException e) {
                    System.err.println("Received invalid object: " + e.getMessage());
                    continue;
                }

                boolean correctRequest = request instanceof Request<?> &&
                        ((Request<?>) request).getAction() != null; // request should extend R class
                if (!correctRequest) {
                    try {
                        service.disconnectAskingClient(this);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                }

                try {
                    // request getPayload should always be of type P
                    ER response = service.dispatch((R) request);
                    if (response != null)
                        out.writeObject(response);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        } finally {
            closeClientSafely();
        }
    }

    private void closeClientSafely() {
        try {
            if (!socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            System.err.println("Error closing socket: " + e.getMessage());
        }
        try {
            if (in != null) {
                in.close();
            }
        } catch (IOException e) {
            System.err.println("Error closing input stream: " + e.getMessage());
        }
        try {
            if (out != null) {
                out.close();
            }
        } catch (IOException e) {
            System.err.println("Error closing output stream: " + e.getMessage());
        }
    }

    public void closeClient() throws IOException {
        in.close();
        out.close();
        socket.close();
    }
}
