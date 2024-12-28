package fr.twiloo.iut.gtes.common.client;

import fr.twiloo.iut.gtes.common.EventType;
import fr.twiloo.iut.gtes.common.model.Event;

import java.util.List;

public interface EventDispatcher {
    void dispatch(Event<?> event);
    List<EventType> supportedEventTypes();
}
