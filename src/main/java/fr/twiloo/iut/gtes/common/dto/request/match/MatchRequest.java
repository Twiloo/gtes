package fr.twiloo.iut.gtes.common.dto.request.match;

import fr.twiloo.iut.gtes.common.Request;

public abstract class MatchRequest<P> extends Request<P> {
    public MatchRequest(P payload) {
        super(payload);
    }
}
