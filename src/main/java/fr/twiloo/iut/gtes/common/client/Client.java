package fr.twiloo.iut.gtes.common.client;

import fr.twiloo.iut.gtes.common.Config;
import fr.twiloo.iut.gtes.common.model.Event;

import java.io.Closeable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import static java.lang.System.err;

public final class Client implements Closeable {
    private final Socket socket;
    private final ObjectOutputStream out;
    private final Thread clientReceiveThread;
    private final ClientReceive clientReceive;

    public Client(Config config, EventDispatcher eventDispatcher) throws IOException {
        // Établir la connexion au Bus d'Événements
        try {
            socket = new Socket("127.0.0.1", config.port);
            out = new ObjectOutputStream(socket.getOutputStream());

            // Envoi des types d'événements pris en charge
            out.writeUnshared(eventDispatcher.supportedEventTypes());
            out.flush();

            // Initialiser le thread de réception pour gérer les événements asynchrones
            clientReceive = new ClientReceive(new ObjectInputStream(socket.getInputStream()), eventDispatcher);
            clientReceiveThread = new Thread(clientReceive);
            clientReceiveThread.start();

            System.out.println("Connecté au bus d'événements : " + socket.getRemoteSocketAddress());
        } catch (IOException e) {
            // En cas d'échec de la connexion
            throw new IOException("Impossible de se connecter au bus d'événements.", e);
        }
    }

    public void sendEvent(Event<?> event) {
        if (event == null || event.type() == null) {
            throw new IllegalArgumentException("L'événement ou son type ne peut pas être nul.");
        }

        try {
            out.writeUnshared(event);
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException("Échec de l'envoi de l'événement : " + event, e);
        }
    }

    @Override
    public void close() throws IOException {
        try {
            // Interrompre le thread de réception
            clientReceive.stop();
            clientReceiveThread.interrupt();
        } catch (Exception ignored) {  }

        try {
            // Fermer le socket
            if (!socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            err.println("Erreur lors de la fermeture du socket : " + e.getMessage());
        }

        try {
            // Fermer le flux de sortie
            out.close();
        } catch (IOException e) {
            err.println("Erreur lors de la fermeture du flux de sortie : " + e.getMessage());
        }
    }
}
