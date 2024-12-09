package fr.twiloo.iut.gtes.microservices;

import fr.twiloo.iut.gtes.common.Request;

import java.io.IOException;
import java.util.ArrayList;

abstract public class CallableService {
    protected int port;
    protected ArrayList<ConnectedClient> clients = new ArrayList<>();

    protected CallableService(int port) throws IOException {
        this.port = port;
        Thread threadConnection = new Thread(new Connection(this));
        threadConnection.start();
    }

    abstract Object run(Request request) throws Exception;

    public void disconnectClient(ConnectedClient client) throws IOException {
        client.closeClient();
        clients.remove(client);
    }

    public void addClient(ConnectedClient client) {
        clients.add(client);
    }

    public int getPort() {
        return port;
    }
}
