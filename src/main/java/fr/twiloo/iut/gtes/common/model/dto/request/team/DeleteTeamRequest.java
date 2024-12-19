package fr.twiloo.iut.gtes.common.model.dto.request.team;

public final class DeleteTeamRequest extends TeamRequest<String> {
    public DeleteTeamRequest(String teamName) {
        super(teamName);
    }
}
