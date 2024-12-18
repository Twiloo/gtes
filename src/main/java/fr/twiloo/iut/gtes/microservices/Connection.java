package fr.twiloo.iut.gtes.microservices;

import fr.twiloo.iut.gtes.common.Request;
import fr.twiloo.iut.gtes.common.Response;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public final class Connection<R extends Request<?>, ER extends Response<?>> implements Runnable {
    private final CallableService<R, ER> callableService;
    private final ServerSocket serverSocket;
    private final boolean notificationConnection;

    public Connection(CallableService<R, ER> callableService, boolean notificationConnection) throws IOException {
        this.callableService = callableService;
        this.serverSocket = new ServerSocket(notificationConnection ?
                callableService.getNotificationPort() :
                callableService.getRequestPort());
        this.notificationConnection = notificationConnection;
    }

    @Override
    public void run() {
        while (!serverSocket.isClosed()) {
            Socket socketNewClient;
            try {
                socketNewClient = serverSocket.accept();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            if (notificationConnection) {
                ListeningClient<ER> newClient;
                try {
                    newClient = new ListeningClient<>(socketNewClient);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                callableService.addListeningClient(newClient);
            } else {
                AskingClient<R, ER> newClient;
                try {
                    newClient = new AskingClient<>(callableService, socketNewClient);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                callableService.addAskingClient(newClient);
                Thread threadNewClient = new Thread(newClient);
                threadNewClient.start();
            }

        }
    }

    public void stop() throws IOException {
        serverSocket.close();
    }
}