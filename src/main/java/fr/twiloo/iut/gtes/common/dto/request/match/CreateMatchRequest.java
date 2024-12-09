package fr.twiloo.iut.gtes.common.dto.request.match;

import fr.twiloo.iut.gtes.common.Match;

public class CreateMatchRequest implements MatchRequest {
    private final Match match;

    public CreateMatchRequest(Match match) {
        this.match = match;
    }

    public Match getMatch() {
        return match;
    }
}
