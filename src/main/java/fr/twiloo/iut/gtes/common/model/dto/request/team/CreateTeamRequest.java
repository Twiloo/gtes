package fr.twiloo.iut.gtes.common.model.dto.request.team;

import fr.twiloo.iut.gtes.common.model.Team;

public final class CreateTeamRequest extends TeamRequest<Team> {
    public CreateTeamRequest(Team team) {
        super(team);
    }
}
