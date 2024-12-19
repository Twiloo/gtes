package fr.twiloo.iut.gtes.common.model.dto.request.team;

import fr.twiloo.iut.gtes.common.model.dto.Request;

public abstract class TeamRequest<P> extends Request<P> {
    public TeamRequest(P payload) {
        super(payload);
    }
}
