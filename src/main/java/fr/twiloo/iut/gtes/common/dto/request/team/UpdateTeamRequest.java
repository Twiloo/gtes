package fr.twiloo.iut.gtes.common.dto.request.team;

import fr.twiloo.iut.gtes.common.Team;

public final class UpdateTeamRequest extends TeamRequest<UpdateTeamRequest.Payload> {
    public UpdateTeamRequest(Payload payload) {
        super(payload);
    }

    public record Payload(
            String teamName,
            Team newTeam) {
    }
}
