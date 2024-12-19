package fr.twiloo.iut.gtes.common.model.dto.request.match;

import fr.twiloo.iut.gtes.common.model.Match;

public final class CreateMatchRequest extends MatchRequest<Match> {
    public CreateMatchRequest(Match match) {
        super(match);
    }
}
