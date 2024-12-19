package fr.twiloo.iut.gtes.microservices;

import fr.twiloo.iut.gtes.common.model.dto.Response;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public final class ListeningClient<R extends Response<?>> {
    private final Socket socket;
    private final ObjectOutputStream out;

    public ListeningClient(Socket socket) throws IOException {
        this.socket = socket;
        this.out = new ObjectOutputStream(socket.getOutputStream());
    }

    public void notify(R notification) {
        if (socket.isClosed())
            return;
        try {

            out.writeObject(notification);
            out.flush();
        } catch (IOException e) {
            System.err.println("Failed to notify client: " + e.getMessage());
            try {
                closeClient();
            } catch (IOException ex) {
                System.err.println("Failed to close client: " + ex.getMessage());
            }
        }
    }

    public void closeClient() throws IOException {
        out.close();
        socket.close();
    }
}