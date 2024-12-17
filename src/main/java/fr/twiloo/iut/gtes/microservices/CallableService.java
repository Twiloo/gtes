package fr.twiloo.iut.gtes.microservices;

import fr.twiloo.iut.gtes.common.Request;
import fr.twiloo.iut.gtes.common.Response;

import java.io.IOException;
import java.util.ArrayList;

import static java.lang.System.out;

abstract public class CallableService<R extends Request<?>, ER extends Response<?>> {
    protected final int port;
    protected final ArrayList<ConnectedClient<R, ER>> clients = new ArrayList<>();

    protected CallableService(int port) throws IOException {
        this.port = port;
        Thread threadConnection = new Thread(new Connection<>(this));
        out.println(this.getClass().getSimpleName() + " started");
        threadConnection.start();
    }

    abstract ER run(R request) throws Exception;

    public void disconnectClient(ConnectedClient<R, ER> client) throws IOException {
        client.closeClient();
        clients.remove(client);
    }

    public void addClient(ConnectedClient<R, ER> client) {
        clients.add(client);
    }

    public int getPort() {
        return port;
    }
}
