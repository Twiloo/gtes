package fr.twiloo.iut.gtes.microservices;

import fr.twiloo.iut.gtes.common.Request;

import java.io.IOException;
import java.util.ArrayList;

import static java.lang.System.out;

abstract public class CallableService<P> {
    protected final int port;
    protected final ArrayList<ConnectedClient<P>> clients = new ArrayList<>();

    protected CallableService(int port) throws IOException {
        this.port = port;
        Thread threadConnection = new Thread(new Connection<>(this));
        out.println(this.getClass().getSimpleName() + " started");
        threadConnection.start();
    }

    abstract Object run(Request<P> request) throws Exception;

    public void disconnectClient(ConnectedClient<P> client) throws IOException {
        client.closeClient();
        clients.remove(client);
    }

    public void addClient(ConnectedClient<P> client) {
        clients.add(client);
    }

    public int getPort() {
        return port;
    }
}
