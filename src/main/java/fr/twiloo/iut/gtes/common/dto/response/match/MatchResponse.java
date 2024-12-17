package fr.twiloo.iut.gtes.common.dto.response.match;

import fr.twiloo.iut.gtes.common.Response;

public abstract class MatchResponse<C> extends Response<C> {
    public MatchResponse(C response) {
        super(response);
    }
}
