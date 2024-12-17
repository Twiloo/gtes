package fr.twiloo.iut.gtes.mvc.event;

public abstract class Event<P, R> {
    P payload;
    R response;

    // Constructeur
    public Event(P payload) {
        this.payload = payload;
    }

    // Getters et Setteurs
    public P getPayload() {
        return payload;
    }

    public void setPayload(P payload) {
        this.payload = payload;
    }

    public R getResponse() {
        return response;
    }

    public void setResponse(R response) {
        this.response = response;
    }

    // Méthode abstraite pour obtenir le nom de l'événement
    public abstract String getName();
}
