package fr.twiloo.iut.gtes.common.model.dto.request.match;

import fr.twiloo.iut.gtes.common.model.Match;

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
