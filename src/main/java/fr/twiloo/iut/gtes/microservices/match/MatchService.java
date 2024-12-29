package fr.twiloo.iut.gtes.microservices.match;

import fr.twiloo.iut.gtes.common.Config;
import fr.twiloo.iut.gtes.common.EventType;
import fr.twiloo.iut.gtes.common.model.Event;
import fr.twiloo.iut.gtes.common.model.Match;
import fr.twiloo.iut.gtes.common.model.Team;
import fr.twiloo.iut.gtes.microservices.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class MatchService extends Service {
    private final List<Team> teams = new ArrayList<>(); // Cache local pour les équipes

    public MatchService() throws IOException {
        super();
    }

    @Override
    protected Config getConfig() {
        return Config.EVENT_BUS; // Configuration pour l'EventBus
    }

    @Override
    public void dispatch(Event<?> event) {
        switch (event.type()) {
            case NEW_MATCH_CREATED -> createMatch((Match) event.payload());
            case MATCH_FINISHED -> finishMatch((Match) event.payload());
            case SHOW_TEAMS_LIST -> cacheTeams(event.payload());
        }
    }

    @Override
    public List<EventType> supportedEventTypes() {
        return List.of(
                EventType.NEW_MATCH_CREATED,
                EventType.MATCH_FINISHED,
                EventType.SHOW_TEAMS_LIST
        );
    }

    /**
     * Met en cache les équipes après un événement SHOW_TEAMS_LIST.
     *
     * @param payload La liste des équipes.
     */
    private void cacheTeams(Object payload) {
        if (payload instanceof List<?> list) {
            teams.clear();
            for (Object obj : list) {
                if (obj instanceof Team team) {
                    teams.add(team);
                }
            }
        }
    }

    /**
     * Crée un nouveau match entre deux équipes.
     *
     * @param match Le match à créer.
     */
    private void createMatch(Match match) {
        if (match == null || match.getTeam1() == null || match.getTeam2() == null) {
            sendEvent(new Event<>(EventType.MATCH_CANCELED, null));
            return;
        }

        Team team1 = findTeam(String.valueOf(match.getTeam1()));
        Team team2 = findTeam(String.valueOf(match.getTeam2()));

        if (team1 == null || team2 == null) {
            sendEvent(new Event<>(EventType.MATCH_CANCELED, null));
            return;
        }

        // Match validé et organisé
        sendEvent(new Event<>(EventType.MATCH_ORGANIZED, match));
    }

    /**
     * Met à jour les scores des équipes après un match.
     *
     * @param match Le match terminé.
     */
    private void finishMatch(Match match) {
        Team team1 = findTeam(String.valueOf(match.getTeam1()));
        Team team2 = findTeam(String.valueOf(match.getTeam2()));

        if (team1 == null || team2 == null) {
            sendEvent(new Event<>(EventType.MATCH_CANCELED, null));
            return;
        }

        // Recalculer le classement
        updateRanking();

        // Envoyer un événement signalant que le match est terminé
        sendEvent(new Event<>(EventType.MATCH_FINISHED, match));
    }

    /**
     * Met à jour l'Elo d'une équipe après un match.
     *
     * @param team    L'équipe à mettre à jour.
     * @param score   Le score de l'équipe.
     * @param opponentScore Le score de l'équipe adverse.
     */
    private void updateTeamElo(Team team, int score, int opponentScore) {
        // Exemple simple : victoire = +10 Elo, défaite = -10 Elo
        int eloChange = score > opponentScore ? 10 : (score < opponentScore ? -10 : 0);
        team.setElo(team.getElo() + eloChange);

        // Envoyer une mise à jour de l'équipe au TeamService
        sendEvent(new Event<>(EventType.TEAM_UPDATED, team));
    }

    /**
     * Met à jour le classement des équipes.
     */
    private void updateRanking() {
        // Trier les équipes par Elo (descendant) et envoyer un événement de mise à jour
        teams.sort((t1, t2) -> Integer.compare(t2.getElo(), t1.getElo()));
        sendEvent(new Event<>(EventType.RANKING_UPDATED, teams));
    }

    /**
     * Recherche une équipe par son nom dans le cache local.
     *
     * @param name Le nom de l'équipe.
     * @return L'équipe correspondante, ou null si elle n'existe pas.
     */
    private Team findTeam(String name) {
        for (Team team : teams) {
            if (team.getName().equalsIgnoreCase(name)) {
                return team;
            }
        }
        return null;
    }
}
