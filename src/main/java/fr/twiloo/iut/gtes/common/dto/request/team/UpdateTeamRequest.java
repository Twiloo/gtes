package fr.twiloo.iut.gtes.common.dto.request.team;

import fr.twiloo.iut.gtes.common.Team;

public final class UpdateTeamRequest implements TeamRequest {
    private final String teamName;
    private final Team newTeam;

    public UpdateTeamRequest(String teamName, Team newTeam) {
        this.teamName = teamName;
        this.newTeam = newTeam;
    }

    public String getTeamName() {
        return teamName;
    }

    public Team getNewTeam() {
        return newTeam;
    }
}
