package fr.twiloo.iut.gtes.common.model.dto.response.team;

import fr.twiloo.iut.gtes.common.model.dto.Response;

public abstract class TeamResponse<C> extends Response<C> {
    public TeamResponse(C content) {
        super(content);
    }
}
