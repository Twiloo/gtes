package fr.twiloo.iut.gtes.common.model;

import fr.twiloo.iut.gtes.common.EventType;

public record Event<P>(
        EventType type,
        P payload) {
}
