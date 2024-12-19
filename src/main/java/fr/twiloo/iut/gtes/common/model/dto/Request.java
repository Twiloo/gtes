package fr.twiloo.iut.gtes.common.model.dto;

import fr.twiloo.iut.gtes.common.Action;

import java.io.Serializable;

/**
 * @param <P> payload type
 */
public abstract class Request<P> implements Serializable {
    protected final P payload;

    public Request(P payload) {
        this.payload = payload;
    }

    public P getPayload() {
        return payload;
    }

    public Action getAction() {
        return Action.actionOf(this);
    }
}
