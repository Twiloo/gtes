package fr.twiloo.iut.gtes.common.model;

import fr.twiloo.iut.gtes.common.EventType;
import fr.twiloo.iut.gtes.common.client.EventDispatcher;

import java.util.Optional;

public record Event<P>(
        EventType type,
        P payload,
        EventDispatcher source,
        Optional<EventDispatcher> forcedTarget) {
}
