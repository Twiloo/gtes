package fr.twiloo.iut.gtes.eventbus;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public final class Connection implements Runnable {
    private final EventBus eventBus;
    private final ServerSocket serverSocket;

    public Connection(EventBus eventBus) throws IOException {
        this.eventBus = eventBus;
        this.serverSocket = new ServerSocket(eventBus.getPort());
    }

    @Override
    public void run() {
        while (!serverSocket.isClosed()) {
            Socket socketNewSubscriber;
            try {
                socketNewSubscriber = serverSocket.accept();
            } catch (IOException e) {
                System.err.println("Error accepting new connection: " + e.getMessage());
                continue;
            }

            Subscriber subscriber;
            try {
                subscriber = new Subscriber(eventBus, socketNewSubscriber);
            } catch (IOException e) {
                System.err.println("Error creating subscriber: " + e.getMessage());
                continue;
            }

            eventBus.addSubscriber(subscriber);
            Thread threadNewSubscriber = new Thread(subscriber);
            threadNewSubscriber.start();
        }
    }

    public void stop() throws IOException {
        serverSocket.close();
    }
}
