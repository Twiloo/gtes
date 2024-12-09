package fr.twiloo.iut.gtes.common.dto.request.team;

import fr.twiloo.iut.gtes.common.Team;

public final class CreateTeamRequest implements TeamRequest {
    private final Team team;

    public CreateTeamRequest(Team team) {
        this.team = team;
    }

    public Team getTeam() {
        return team;
    }
}
