package fr.twiloo.iut.gtes.common.dto.response.team;

import fr.twiloo.iut.gtes.common.Team;

public final class CreateTeamResponse extends TeamResponse<CreateTeamResponse.Content> {
    public CreateTeamResponse(Content content) {
        super(content);
    }

    public record Content(
            boolean success,
            Team team) {
    }
}
