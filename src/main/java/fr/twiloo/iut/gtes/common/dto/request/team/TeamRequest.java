package fr.twiloo.iut.gtes.common.dto.request.team;

import fr.twiloo.iut.gtes.common.Request;

public abstract class TeamRequest<P> extends Request<P> {
    public TeamRequest(P payload) {
        super(payload);
    }
}
