package fr.twiloo.iut.gtes.common.client;

import fr.twiloo.iut.gtes.common.EventType;
import fr.twiloo.iut.gtes.common.model.Event;

import java.io.IOException;
import java.util.List;

public interface EventDispatcher {
    void dispatch(Event<?> event) throws IOException;

    List<EventType> supportedEventTypes(); // Ajoutez cette méthode si elle n'existe pas.
}
