package fr.twiloo.iut.gtes.mvc.controller;

import fr.twiloo.iut.gtes.common.EventType;
import fr.twiloo.iut.gtes.common.model.Event;
import fr.twiloo.iut.gtes.common.model.Team;
import fr.twiloo.iut.gtes.common.model.dto.TeamDeleted;
import fr.twiloo.iut.gtes.common.model.dto.TeamUpdate;
import fr.twiloo.iut.gtes.common.model.dto.TeamUpdated;
import fr.twiloo.iut.gtes.mvc.MVCApp;
import fr.twiloo.iut.gtes.mvc.view.View;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static fr.twiloo.iut.gtes.common.utils.Input.next;
import static java.lang.System.out;

public final class TeamController {

    public static void getTeamListAction() throws IOException {
        MVCApp.getInstance().getClient().sendEvent(new Event<>(EventType.GET_TEAMS_LIST, null));
    }

    public static void showTeamsListAction(List<?> teams) {
        out.println(View.TEAM_LIST);
        for (Object team : teams) {
            out.println(team);
        }
    }

    public static void addTeamAction() throws IOException, InterruptedException {
        // Collecte des informations pour l'équipe
        out.println(View.ASK_TEAM_NAME);
        String name = next();
        ArrayList<String> players = getPlayers();

        Team team = new Team(players, name, null, null, null);
        // Envoi de l'événement de création d'équipe
        MVCApp.getInstance().getClient().sendEvent(new Event<>(EventType.CREATE_TEAM, team));
    }

    public static void showNewTeamCreatedAction(Team team) {
        if (team == null) {
            out.println(View.TEAM_NON_CREE);
            return;
        }

        out.println(View.TEAM_CREE);
        out.println(team);
    }

    public static void updateTeamAction() throws IOException, InterruptedException {
        out.println(View.ASK_TEAM_NAME_UPDATE);
        String name = next();

        out.println(View.UPDATE_TEAM_NAME);
        String newName = next();
        ArrayList<String> players = getPlayers();

        Event<TeamUpdate> event = new Event<>(
                EventType.UPDATE_TEAM,
                new TeamUpdate(name, newName, players.getFirst(), players.get(1), players.getLast()));

        // Envoi de l'événement de modification de l'équipe
        MVCApp.getInstance().getClient().sendEvent(event);
    }

    public static void showTeamUpdatedAction(TeamUpdated payload) {
        if (payload.newTeam() == null) {
            out.println(View.TEAM_NON_UPDATE);
            return;
        }

        out.println("L'équipe " + payload.oldTeamName() + " a été modifiée : ");
        out.println(payload.newTeam());
    }

    public static void deleteTeamAction() throws IOException, InterruptedException {
        out.println(View.ASK_TEAM_NAME_DELETE);
        String name = next();

        Event<String> event = new Event<>(EventType.DELETE_TEAM, name);

        // Envoi de l'événement de modification de l'équipe
        MVCApp.getInstance().getClient().sendEvent(event);
    }

    public static void showTeamDeletedAction(TeamDeleted payload) {
        if (payload.success()) {
            out.println("L'équipe " + payload.teamName() + " a bien été supprimée");
        } else {
            out.println("L'équipe " + payload.teamName() + " n'a pas pû être supprimée");
        }
    }

    private static ArrayList<String> getPlayers() throws InterruptedException {
        ArrayList<String> players = new ArrayList<>();
        out.println(View.ASK_PLAYER_1);
        players.add(next());
        out.println(View.ASK_PLAYER_2);
        players.add(next());
        out.println(View.ASK_PLAYER_3);
        players.add(next());
        return players;
    }
}
