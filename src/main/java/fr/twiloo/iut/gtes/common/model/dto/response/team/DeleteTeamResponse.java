package fr.twiloo.iut.gtes.common.model.dto.response.team;

public final class DeleteTeamResponse extends TeamResponse<Boolean> {
    public DeleteTeamResponse(Boolean success) {
        super(success);
    }
}
