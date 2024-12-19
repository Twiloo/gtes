package fr.twiloo.iut.gtes.common.model.dto.request.match;

import fr.twiloo.iut.gtes.common.model.dto.Request;

public abstract class MatchRequest<P> extends Request<P> {
    public MatchRequest(P payload) {
        super(payload);
    }
}
