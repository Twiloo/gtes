package fr.twiloo.iut.gtes.common.model.dto.response.team;

import fr.twiloo.iut.gtes.common.model.Team;

public final class UpdateTeamResponse extends TeamResponse<UpdateTeamResponse.Content> {
    public UpdateTeamResponse(Content content) {
        super(content);
    }

    public record Content(
            boolean success,
            Team updatedTeam) {
    }
}
