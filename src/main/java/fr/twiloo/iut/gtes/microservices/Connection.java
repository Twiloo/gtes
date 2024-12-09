package fr.twiloo.iut.gtes.microservices;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Connection implements Runnable {
    private final CallableService callableService;
    private final ServerSocket serverSocket;

    public Connection(CallableService callableService) throws IOException {
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

            ConnectedClient newClient;
            try {
                newClient = new ConnectedClient(callableService, socketNewClient);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            callableService.addClient(newClient);
            Thread threadNewClient = new Thread(newClient);
            threadNewClient.start();
        }
    }
}