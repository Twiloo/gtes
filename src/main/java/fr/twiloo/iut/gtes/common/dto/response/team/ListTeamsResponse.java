package fr.twiloo.iut.gtes.common.dto.response.team;

import fr.twiloo.iut.gtes.common.Team;

public final class ListTeamsResponse extends TeamResponse<Iterable<Team>> {
    public ListTeamsResponse(Iterable<Team> content) {
        super(content);
    }
}
