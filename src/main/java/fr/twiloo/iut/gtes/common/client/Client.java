package fr.twiloo.iut.gtes.common.client;

import fr.twiloo.iut.gtes.common.Config;
import fr.twiloo.iut.gtes.common.model.Event;

import java.io.Closeable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public final class Client implements Closeable {
    private final Socket socket;
    private final ObjectOutputStream out;
    private final Thread clientReceiveThread;

    public Client(Config config, EventDispatcher eventDispatcher) throws IOException {
        // Socket for synchronous events produced by client
        socket = new Socket("127.0.0.1", config.port);
        out = new ObjectOutputStream(socket.getOutputStream());

        // Send subscription list to event bus
        out.writeObject(eventDispatcher.supportedEventTypes());
        out.flush();

        // Thread for async events coming from event bus (like a webhook, you subscribe by connecting to said events)
        ClientReceive clientReceive = new ClientReceive(new ObjectInputStream(socket.getInputStream()), eventDispatcher);
        clientReceiveThread = new Thread(clientReceive);
        clientReceiveThread.start();
        System.out.println("Connected to event bus : " + socket.getRemoteSocketAddress() + " on port " + socket.getPort());
    }

    public void sendEvent(Event<?> event) {
        try {
            out.writeObject(event);
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException("Failed to send request or read response", e);
        }
    }

    @Override
    public void close() throws IOException {
        try {
            clientReceiveThread.interrupt();
        } catch (Exception ignored) { }
        socket.close();
        out.close();
    }
}
