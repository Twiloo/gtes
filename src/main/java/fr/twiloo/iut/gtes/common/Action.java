package fr.twiloo.iut.gtes.common;

import fr.twiloo.iut.gtes.common.dto.request.match.CreateMatchRequest;

public enum Action {
    VOIR_EQUIPES,
    NOUVELLE_EQUIPE_CREE,
    EQUIPE_MODIFIEE,
    EQUIPE_SUPPRIMEE,
    MATCH_ORGANISE(CreateMatchRequest.class),
    MATCH_TERMINE,
    MATCH_ANNULE;

    private final Class<? extends Request<?>> requestClass;
    private final Class<? extends Response> responseClass;

    // This constructor should be removed
    Action(Class<? extends Request<?>> requestClass) {
        this.requestClass = requestClass;
        this.responseClass = null;
    }

    Action(Class<? extends Request<?>> requestClass, Class<? extends Response> responseClass) {
        this.requestClass = requestClass;
        this.responseClass = responseClass;
    }

    public static Action actionOf(Request<?> request) {
        for (Action action : values()) {
            if (action.requestClass.isAssignableFrom(request.getClass())) {
                return action;
            }
        }
        return null;
    }

    public Class<? extends Response> getResponseClass() {
        return responseClass;
    }
}
