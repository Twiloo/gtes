package fr.twiloo.iut.gtes.common.client;

import fr.twiloo.iut.gtes.common.model.Event;

import java.io.Closeable;
import java.io.IOException;
import java.io.ObjectInputStream;

public final class ClientReceive implements Runnable, Closeable {
    private final ObjectInputStream in;
    private final EventDispatcher eventDispatcher;
    private volatile boolean running = true; // Contrôle de l'exécution

    public ClientReceive(ObjectInputStream in, EventDispatcher eventDispatcher) {
        this.in = in;
        this.eventDispatcher = eventDispatcher;
    }

    @Override
    public void run() {
        try {
            while (running) {
                Object event;
                try {
                    event = in.readUnshared();
                } catch (ClassNotFoundException e) {
                    System.err.println("Événement non reconnu : " + e.getMessage());
                    continue; // Ignorer cet événement et poursuivre l'écoute
                }

                if (event instanceof Event<?>) {
                    handleEvent((Event<?>) event);
                } else {
                    System.err.println("Événement invalide reçu : " + event);
                }
            }
        } catch (IOException ignored) {
        } finally {
            close(); // Fermeture sécurisée en fin de boucle
            eventDispatcher.close();
        }
    }

    /**
     * Traitement de l'événement reçu.
     */
    private void handleEvent(Event<?> event) {
        eventDispatcher.dispatch(event);
    }

    /**
     * Stoppe l'écoute des événements.
     */
    public void stop() {
        running = false; // Met fin à la boucle
        close();
    }

    /**
     * Ferme le flux d'entrée.
     */
    @Override
    public void close() {
        try {
            if (in != null) {
                in.close();
            }
        } catch (IOException e) {
            System.err.println("Erreur lors de la fermeture du flux : " + e.getMessage());
        }
    }
}
