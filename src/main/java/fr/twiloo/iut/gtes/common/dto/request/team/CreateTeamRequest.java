package fr.twiloo.iut.gtes.common.dto.request.team;

import fr.twiloo.iut.gtes.common.Team;

public final class CreateTeamRequest extends TeamRequest<Team> {
    public CreateTeamRequest(Team team) {
        super(team);
    }
}
