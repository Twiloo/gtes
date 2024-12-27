package fr.twiloo.iut.gtes.common.client;

import fr.twiloo.iut.gtes.common.model.Event;

public interface EventDispatcher {
    void dispatch(Event<?> event) throws Exception;
}
