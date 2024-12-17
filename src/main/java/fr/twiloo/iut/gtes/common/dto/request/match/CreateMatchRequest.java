package fr.twiloo.iut.gtes.common.dto.request.match;

import fr.twiloo.iut.gtes.common.Match;

public final class CreateMatchRequest extends MatchRequest<Match> {
    public CreateMatchRequest(Match match) {
        super(match);
    }
}
