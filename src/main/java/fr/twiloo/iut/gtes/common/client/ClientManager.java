package fr.twiloo.iut.gtes.common.client;

import fr.twiloo.iut.gtes.common.Request;
import fr.twiloo.iut.gtes.common.Response;
import fr.twiloo.iut.gtes.common.ServiceConfig;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public final class ClientManager {
    private static final ClientManager instance = new ClientManager();
    private final Map<ServiceConfig, Client<Request<?>, Response<?>>> clients = new HashMap<>();

    /**
     * Avoid ClientManager being constructed from outside
     */
    private ClientManager() { }

    public static ClientManager getInstance() {
        return instance;
    }

    public Client<Request<?>, Response<?>> getClient(ServiceConfig serviceConfig) throws IOException {
        if (!clients.containsKey(serviceConfig)) {
            Client<Request<?>, Response<?>> client = new Client<>(serviceConfig);
            clients.put(serviceConfig, client);
            return client;
        } else {
            return clients.get(serviceConfig);
        }
    }

    public void closeClients() throws IOException {
        for (Client<?, ?> client : clients.values()) {
            client.close();
        }
        clients.clear();
    }

}
