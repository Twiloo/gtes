package fr.twiloo.iut.gtes.common.client;

import fr.twiloo.iut.gtes.common.model.dto.Request;
import fr.twiloo.iut.gtes.common.model.dto.Response;
import fr.twiloo.iut.gtes.common.ServiceConfig;

import java.io.Closeable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


public final class Client implements Closeable {
    private final Socket requestSocket;
    private final Socket notificationSocket;

    private final ObjectOutputStream eventOut;

    private final ClientSend clientSend;
    private final Thread clientReceiveThread;

    public Client(ServiceConfig config) throws IOException {
        // Socket for synchronous requests made by client
        requestSocket = new Socket(config.getAddress(), config.getRequestPort());
        eventOut = new ObjectOutputStream(requestSocket.getOutputStream());
        clientSend = new ClientSend(eventOut);

        // Socket for async notifications coming from service (like a webhook, you subscribe by connecting to said service)
        if (config.getSubscriptionPort() != null) {
            notificationSocket = new Socket(config.getAddress(), config.getSubscriptionPort());
            ObjectInputStream notificationIn = new ObjectInputStream(notificationSocket.getInputStream());
            clientReceiveThread = new Thread(new ClientReceive(notificationIn));
            clientReceiveThread.setDaemon(true);
            clientReceiveThread.start();
        } else {
            notificationSocket = null;
            clientReceiveThread = null;
        }
    }

    public ClientSend getClientSend() {
        return clientSend;
    }

    @Override
    public void close() throws IOException {
        try {
            clientReceiveThread.interrupt();
        } catch (Exception ignored) {
        }

        // Close all sockets and streams
        requestSocket.close();
        if (notificationSocket != null)
            notificationSocket.close();

        eventOut.close();
    }
}
