package fr.twiloo.iut.gtes.common.dto.request.match;

import fr.twiloo.iut.gtes.common.Request;
import fr.twiloo.iut.gtes.common.Response;

public abstract class MatchRequest<P, ER extends Response> extends Request<P, ER> {
    public MatchRequest(P payload) {
        super(payload);
    }
}
