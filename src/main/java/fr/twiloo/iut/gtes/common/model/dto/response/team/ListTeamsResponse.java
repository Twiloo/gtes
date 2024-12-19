package fr.twiloo.iut.gtes.common.model.dto.response.team;

import fr.twiloo.iut.gtes.common.model.Team;

public final class ListTeamsResponse extends TeamResponse<Iterable<Team>> {
    public ListTeamsResponse(Iterable<Team> content) {
        super(content);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for(Team team : content) {
            result.append(team.toString());
        }
        return """
               Liste des équipes :
               
               """ + result;
    }
}
