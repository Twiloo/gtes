package fr.twiloo.iut.gtes.microservices.team;

import fr.twiloo.iut.gtes.common.Config;
import fr.twiloo.iut.gtes.common.EventType;
import fr.twiloo.iut.gtes.common.model.Event;
import fr.twiloo.iut.gtes.common.model.Match;
import fr.twiloo.iut.gtes.common.model.Team;
import fr.twiloo.iut.gtes.microservices.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class TeamService extends Service {
    private final List<Team> teams = new ArrayList<>();
    private final TeamCron teamCron;

    public TeamService() throws IOException {
        super();
        teamCron = new TeamCron(teams);
        Thread t = new Thread(teamCron);
        t.start();
    }

    @Override
    protected Config getConfig() {
        return Config.EVENT_BUS;
    }

    @Override
    public void dispatch(Event<?> event) {
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
            sendEvent(new Event<>(EventType.NEW_TEAM_CREATED, null));
            return;
        }

        // Création de l'équipe avec des valeurs par défaut
        Team team = new Team(payload.getPlayers(), payload.getName(), 500, 0, true);
        synchronized (teams) {
            teams.add(team);
        }

        // Notification de la création de l'équipe
        sendEvent(new Event<>(EventType.NEW_TEAM_CREATED, team));
    }

    private void listTeams() {
        synchronized (teams) {
            List<Team> orderedTeams = teams.stream()
                    .filter(Team::isActive)
                    .sorted(Comparator.comparing(Team::getRanking))
                    .toList();
            sendEvent(new Event<>(EventType.SHOW_TEAMS_LIST, orderedTeams));
        }
    }

    private void updateTeam(TeamUpdate payload) {
        Team team = null;
        if (payload != null && payload.getValue() != null)
            team = findTeam(payload.getKey());
        if (team == null) {
            sendEvent(new Event<>(EventType.TEAM_UPDATED, null));
            return;
        }

        Team newTeam = payload.getValue();
        if (newTeam.getName() != null && !newTeam.getName().isEmpty() && findTeam(newTeam.getName()) == null)
            team.setName(newTeam.getName());

        if (newTeam.getPlayers() != null && newTeam.getPlayers().size() == 3)
            team.setPlayers(newTeam.getPlayers());

        sendEvent(new Event<>(EventType.TEAM_UPDATED, team));
    }

    /**
     * This performs soft delete
     */
    private void deleteTeam(String payload) {
        Team team = findTeam(payload);
        if (team != null)
            team.deactivate();
        sendEvent(new Event<>(EventType.TEAM_DELETED, team != null));
    }

    private void updateRanking(Match match) {
        // Mise à jour des classements en fonction des résultats du match
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
    public void stop() {
        super.stop();
        teamCron.stop();
    }

    /**
     * Classe interne pour les mises à jour partielles d'équipe.
     */
    private static class TeamUpdate {
        private final String key;
        private final Team value;

        public TeamUpdate(String key, Team value) {
            this.key = key;
            this.value = value;
        }

        public String getKey() {
            return key;
        }

        public Team getValue() {
            return value;
        }
    }
}
