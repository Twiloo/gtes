package fr.twiloo.iut.gtes.common.dto.request.match;

import fr.twiloo.iut.gtes.common.Match;
import fr.twiloo.iut.gtes.common.dto.response.match.CreateMatchResponse;

public final class CreateMatchRequest extends MatchRequest<Match, CreateMatchResponse> {
    public CreateMatchRequest(Match match) {
        super(match);
    }
}
