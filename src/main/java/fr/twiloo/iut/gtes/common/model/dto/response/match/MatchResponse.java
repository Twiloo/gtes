package fr.twiloo.iut.gtes.common.model.dto.response.match;

import fr.twiloo.iut.gtes.common.model.dto.Response;

public abstract class MatchResponse<C> extends Response<C> {
    public MatchResponse(C response) {
        super(response);
    }
}
