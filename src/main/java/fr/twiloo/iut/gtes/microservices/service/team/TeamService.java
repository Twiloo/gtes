package fr.twiloo.iut.gtes.microservices.service.team;

import fr.twiloo.iut.gtes.common.ServiceConfig;
import fr.twiloo.iut.gtes.common.Team;
import fr.twiloo.iut.gtes.common.dto.request.team.TeamRequest;
import fr.twiloo.iut.gtes.common.dto.request.team.UpdateTeamRequest;
import fr.twiloo.iut.gtes.common.dto.response.team.*;
import fr.twiloo.iut.gtes.microservices.CallableService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class TeamService extends CallableService<TeamRequest<?>, TeamResponse<?>> {
    private final List<Team> teams = new ArrayList<>();

    public TeamService() throws IOException {
        super(ServiceConfig.TEAM);

        TeamCron cron = new TeamCron(teams);
        Thread t = new Thread(cron);
        t.start();
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

    private CreateTeamResponse createTeam(Team payload) {
        if (payload == null ||
                payload.getName() == null || payload.getName().isEmpty() ||
                payload.getPlayers() == null || payload.getPlayers().size() != 3 ||
                findTeam(payload.getName()) != null) {
            return new CreateTeamResponse(new CreateTeamResponse.Content(false, null));
        }
        Team team = new Team(payload.getPlayers(), payload.getName(), 500, 0, true);
        synchronized (teams) {
            teams.add(team);
        }
        return new CreateTeamResponse(new CreateTeamResponse.Content(true, team));
    }

    private ListTeamsResponse listTeams() {
        synchronized (teams) {
            List<Team> orderedTeams = teams.stream()
                    .filter(Team::isActive)
                    .sorted(Comparator.comparing(Team::getRanking))
                    .toList();
            return new ListTeamsResponse(orderedTeams);
        }
    }

    /**
     * This performs patch like update (null values from the request are kept as the original ones)
     */
    private UpdateTeamResponse updateTeam(UpdateTeamRequest.Payload payload) {
        Team team = null;
        if (payload != null && payload.newTeam() != null)
            team = findTeam(payload.teamName());
        if (team == null)
            return new UpdateTeamResponse(new UpdateTeamResponse.Content(false, null));

        Team newTeam = payload.newTeam();
        if (newTeam.getName() != null && !newTeam.getName().isEmpty() && findTeam(newTeam.getName()) == null)
            team.setName(newTeam.getName());

        if (newTeam.getPlayers() != null && newTeam.getPlayers().size() == 3)
            team.setPlayers(newTeam.getPlayers());

        return new UpdateTeamResponse(new UpdateTeamResponse.Content(true, team));
    }

    /**
     * This performs soft delete
     */
    private DeleteTeamResponse deleteTeam(String payload) {
        Team team = findTeam(payload);
        if (team != null)
            team.deactivate();
        return new DeleteTeamResponse(team != null);
    }

    private Team findTeam(String name) {
        synchronized (teams) {
            return teams.stream()
                    .filter(team -> team.getName().equals(name))
                    .findFirst()
                    .orElse(null);
        }
    }
}
