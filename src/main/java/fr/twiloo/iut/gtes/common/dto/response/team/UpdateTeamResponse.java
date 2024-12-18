package fr.twiloo.iut.gtes.common.dto.response.team;

import fr.twiloo.iut.gtes.common.Team;

public final class UpdateTeamResponse extends TeamResponse<UpdateTeamResponse.Content> {
    public UpdateTeamResponse(Content content) {
        super(content);
    }

    public record Content(
            boolean success,
            Team updatedTeam) {
    }
}
