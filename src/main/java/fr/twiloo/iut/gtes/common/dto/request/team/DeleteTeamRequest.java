package fr.twiloo.iut.gtes.common.dto.request.team;

public final class DeleteTeamRequest implements TeamRequest {
    private final String teamName;

    public DeleteTeamRequest(String teamName) {
        this.teamName = teamName;
    }

    public String getTeamName() {
        return teamName;
    }
}
