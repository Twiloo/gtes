package fr.twiloo.iut.gtes.common.dto.response.team;

import fr.twiloo.iut.gtes.common.Response;

public abstract class TeamResponse<C> extends Response<C> {
    public TeamResponse(C content) {
        super(content);
    }
}
