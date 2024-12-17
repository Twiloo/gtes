package fr.twiloo.iut.gtes.microservices;

import fr.twiloo.iut.gtes.common.Request;
import fr.twiloo.iut.gtes.common.Response;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public final class Connection<R extends Request<?>, ER extends Response<?>> implements Runnable {
    private final CallableService<R, ER> callableService;
    private final ServerSocket serverSocket;

    public Connection(CallableService<R, ER> callableService) throws IOException {
        this.callableService = callableService;
        this.serverSocket = new ServerSocket(callableService.getPort());
    }

    @Override
    public void run() {
        while (true) {
            Socket socketNewClient;
            try {
                socketNewClient = serverSocket.accept();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            ConnectedClient<R, ER> newClient;
            try {
                newClient = new ConnectedClient<>(callableService, socketNewClient);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            callableService.addClient(newClient);
            Thread threadNewClient = new Thread(newClient);
            threadNewClient.start();
        }
    }
}