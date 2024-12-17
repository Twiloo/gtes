package fr.twiloo.iut.gtes.common.dto.request.match;

import fr.twiloo.iut.gtes.common.Match;

public final class UpdateMatchRequest extends MatchRequest<UpdateMatchRequest.Payload> {
    public UpdateMatchRequest(Payload payload) {
        super(payload);
    }

    public record Payload(
            String teamAName,
            String teamBName,
            Match newMatch) {
    }
}
