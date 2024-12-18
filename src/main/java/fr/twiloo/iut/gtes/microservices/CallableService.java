package fr.twiloo.iut.gtes.microservices;

import fr.twiloo.iut.gtes.common.Request;
import fr.twiloo.iut.gtes.common.Response;
import fr.twiloo.iut.gtes.common.ServiceConfig;

import java.io.IOException;
import java.util.ArrayList;

import static java.lang.System.out;

abstract public class CallableService<R extends Request<?>, ER extends Response<?>> {
    protected final int requestPort;
    protected final Integer notificationPort;
    protected final ArrayList<AskingClient<R, ER>> askingClients = new ArrayList<>();
    protected final ArrayList<ListeningClient<ER>> listeningClients = new ArrayList<>();

    private final Connection<R, ER> requestConnection;
    private final Connection<R, ER> notificationConnection;

    protected CallableService(ServiceConfig config) throws IOException {
        this.requestPort = config.getRequestPort();
        this.notificationPort = config.getSubscriptionPort();

        requestConnection = new Connection<>(this, false);
        new Thread(requestConnection).start();

        if (notificationPort != null) {
            notificationConnection = new Connection<>(this, true);
            new Thread(notificationConnection).start();
        } else {
            notificationConnection = null;
        }

        out.println(this.getClass().getSimpleName() + " started");
    }

    abstract ER dispatch(R request) throws Exception;

    public void addAskingClient(AskingClient<R, ER> client) {
        askingClients.add(client);
    }

    public void addListeningClient(ListeningClient<ER> client) {
        listeningClients.add(client);
    }

    public void notifyListeningClients(ER notification) {
        for (ListeningClient<ER> client : new ArrayList<>(listeningClients)) {
            client.notify(notification);
        }
    }

    public void stop() throws IOException {
        for (ListeningClient<ER> client : listeningClients) {
            client.closeClient();
        }
        for (AskingClient<R, ER> client : askingClients) {
            client.closeClient();
        }
        requestConnection.stop();
        if (notificationConnection != null)
            notificationConnection.stop();
        System.out.println("Service stopped");
    }

    public void disconnectAskingClient(AskingClient<R, ER> client) throws IOException {
        client.closeClient();
        askingClients.remove(client);
    }

    public int getRequestPort() {
        return requestPort;
    }

    public Integer getNotificationPort() {
        return notificationPort;
    }
}
