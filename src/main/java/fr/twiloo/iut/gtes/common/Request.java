package fr.twiloo.iut.gtes.common;

/**
 * @param <P> payload type
 */
public abstract class Request<P> {
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
