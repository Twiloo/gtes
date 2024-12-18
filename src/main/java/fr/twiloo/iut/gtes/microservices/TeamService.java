package fr.twiloo.iut.gtes.microservices;

import fr.twiloo.iut.gtes.common.ServiceConfig;
import fr.twiloo.iut.gtes.common.Team;
import fr.twiloo.iut.gtes.common.dto.request.team.TeamRequest;
import fr.twiloo.iut.gtes.common.dto.request.team.UpdateTeamRequest;
import fr.twiloo.iut.gtes.common.dto.response.team.ListTeamsResponse;
import fr.twiloo.iut.gtes.common.dto.response.team.TeamResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class TeamService extends CallableService<TeamRequest<?>, TeamResponse<?>> {
    private final List<Team> teams = new ArrayList<>();

    public TeamService() throws IOException {
        super(ServiceConfig.TEAM);
    }

    @Override
    public TeamResponse<?> dispatch(TeamRequest<?> request) throws Exception {
        return switch (request.getAction()) {
            case CREATE_TEAM -> createTeam((Team) request.getPayload());
            case LIST_TEAMS -> listTeams();
            case UPDATE_TEAM -> updateTeam((UpdateTeamRequest.Payload) request.getPayload());
            case DELETE_TEAM -> deleteTeam((String) request.getPayload());
            default -> throw new Exception("Service is unable to process this getAction : " + request.getAction());
        };
    }

    private TeamResponse<?> createTeam(Team payload) {
        return null;
    }

    private ListTeamsResponse listTeams() {
        List<Team> orderedTeams = new ArrayList<>(teams);
        orderedTeams.sort(Comparator.comparing(Team::ranking));
        return new ListTeamsResponse(orderedTeams);
    }

    private TeamResponse<?> updateTeam(UpdateTeamRequest.Payload payload) {
        return null;
    }

    private TeamResponse<?> deleteTeam(String payload) {
        return null;
    }
}
