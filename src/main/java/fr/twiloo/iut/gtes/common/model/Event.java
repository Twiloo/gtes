package fr.twiloo.iut.gtes.common.model;

import fr.twiloo.iut.gtes.common.EventType;
import java.io.Serializable;

public record Event<P>(
        EventType type,
        P payload) implements Serializable {
}
