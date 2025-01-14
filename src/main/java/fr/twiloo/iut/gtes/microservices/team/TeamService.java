package fr.twiloo.iut.gtes.microservices.team;

import fr.twiloo.iut.gtes.common.Config;
import fr.twiloo.iut.gtes.common.EventType;
import fr.twiloo.iut.gtes.common.model.Event;
import fr.twiloo.iut.gtes.common.model.Match;
import fr.twiloo.iut.gtes.common.model.Team;
import fr.twiloo.iut.gtes.common.model.dto.TeamDeleted;
import fr.twiloo.iut.gtes.common.model.dto.TeamUpdate;
import fr.twiloo.iut.gtes.common.model.dto.TeamUpdated;
import fr.twiloo.iut.gtes.microservices.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public final class TeamService extends Service {
    private final List<Team> teams = new ArrayList<>();
    private final TeamCron teamCron;
    private final Thread cronThread;

    public TeamService() throws IOException {
        super();
        teamCron = new TeamCron(teams, this.client);
        cronThread = new Thread(teamCron);
        cronThread.start();
    }

    @Override
    public void dispatch(Event<?> event) {
        super.dispatch(event);
        switch (event.type()) {
            case GET_TEAMS_LIST -> listTeams();
            case CREATE_TEAM -> createTeam((Team) event.payload());
            case UPDATE_TEAM -> updateTeam((TeamUpdate) event.payload());
            case DELETE_TEAM -> deleteTeam((String) event.payload());
            case MATCH_FINISHED -> updateRanking((Match) event.payload());
        }
    }

    @Override
    public List<EventType> supportedEventTypes() {
        return List.of(EventType.GET_TEAMS_LIST,
                EventType.CREATE_TEAM,
                EventType.UPDATE_TEAM,
                EventType.DELETE_TEAM,
                EventType.MATCH_FINISHED);
    }

    private void createTeam(Team payload) {
        if (payload == null ||
                payload.getName() == null || payload.getName().isEmpty() ||
                payload.getPlayers() == null || payload.getPlayers().size() != 3 ||
                findTeam(payload.getName()) != null) {
            // Équipe invalide ou déjà existante
            sendEvent(new Event<>(EventType.NEW_TEAM_CREATED, null), true);
            return;
        }

        // Création de l'équipe avec des valeurs par défaut
        Team team = new Team(payload.getPlayers(), payload.getName(), (Integer) Config.BASE_ELO.value, -1, true);
        synchronized (teams) {
            teams.add(team);
        }

        // Notification de la création de l'équipe
        sendEvent(new Event<>(EventType.NEW_TEAM_CREATED, team), true);
    }

    private void listTeams() {
        synchronized (teams) {
            List<Team> orderedTeams = teams.stream()
                    .filter(Team::isActive)
                    .sorted(Comparator.comparing(Team::getRanking))
                    .toList();
            sendEvent(new Event<>(EventType.SHOW_TEAMS_LIST, orderedTeams), true);
        }
    }

    private void updateTeam(TeamUpdate payload) {
        Team team = null;
        if (payload != null && payload.teamName() != null) {
            team = findTeam(payload.teamName());
        }
        if (team == null) {
            sendEvent(new Event<>( EventType.TEAM_UPDATED, new TeamUpdated(Objects.requireNonNull(payload).teamName(), null)), true);
            return;
        }

        if (payload.newName() != null && !payload.newName().isEmpty() && findTeam(payload.newName()) == null)
            team.setName(payload.newName());

        if (payload.playerAName() != null && !payload.playerAName().isEmpty()) {
            team.getPlayers().removeFirst();
            team.getPlayers().addFirst(payload.playerAName());
        }

        if (payload.playerBName() != null && !payload.playerBName().isEmpty()) {
            team.getPlayers().remove(1);
            team.getPlayers().add(1, payload.playerBName());
        }

        if (payload.playerCName() != null && !payload.playerCName().isEmpty()) {
            team.getPlayers().removeLast();
            team.getPlayers().addLast(payload.playerCName());
        }

        sendEvent(new Event<>(EventType.TEAM_UPDATED, new TeamUpdated(payload.teamName(), team)), true);
    }

    /**
     * This performs soft delete
     */
    private void deleteTeam(String payload) {
        Team team = findTeam(payload);
        if (team != null)
            team.deactivate();
        sendEvent(new Event<>(EventType.TEAM_DELETED, new TeamDeleted(payload, team != null)), true);
    }

    private void updateRanking(Match match) {
        if (match == null ||
                match.getTeamAName() == null ||
                match.getTeamBName() == null ||
                findTeam(match.getTeamAName()) == null ||
                findTeam(match.getTeamBName()) == null) {
            return;
        }
        // Mise à jour du score elo en fonction des résultats du match
        synchronized (teams) {
            Team teamA = findTeam(match.getTeamAName());
            Team teamB = findTeam(match.getTeamBName());

            int eloA = teamA.getElo();
            int eloB = teamB.getElo();
            int K = 30;

            double expectedA = 1.0 / (1.0 + Math.pow(10, (eloB - eloA) / (Double) Config.ELO_DIFFERENCE_FACTOR.value));
            double expectedB = 1.0 / (1.0 + Math.pow(10, (eloA - eloB) / (Double) Config.ELO_DIFFERENCE_FACTOR.value));

            double actualA = match.getScoreA() > match.getScoreB() ? 1 : (match.getScoreA() == match.getScoreB() ? 0.5 : 0);
            double actualB = match.getScoreB() > match.getScoreA() ? 1 : (match.getScoreA() == match.getScoreB() ? 0.5 : 0);

            int newEloA = (int) (eloA + K * (actualA - expectedA));
            int newEloB = (int) (eloB + K * (actualB - expectedB));

            teamA.setElo(newEloA);
            teamB.setElo(newEloB);
        }
    }

    private Team findTeam(String name) {
        synchronized (teams) {
            return teams.stream()
                    .filter(team -> team.getName().equals(name))
                    .findFirst()
                    .orElse(null);
        }
    }

    @Override
    public void close() {
        super.close();
        teamCron.stop();
        cronThread.interrupt();
    }
}
