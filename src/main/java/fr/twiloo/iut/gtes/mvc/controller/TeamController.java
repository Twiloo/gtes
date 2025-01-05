package fr.twiloo.iut.gtes.mvc.controller;

import fr.twiloo.iut.gtes.common.EventType;
import fr.twiloo.iut.gtes.common.model.Event;
import fr.twiloo.iut.gtes.common.model.Team;
import fr.twiloo.iut.gtes.common.model.dto.TeamUpdate;
import fr.twiloo.iut.gtes.mvc.MVCApp;

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
        out.println("Liste des équipes : ");
        for (Object team : teams) {
            out.println(team);
        }
    }

    public static void addTeamAction() throws IOException, InterruptedException {
        // Collecte des informations pour l'équipe
        out.println("Entrez le nom de l'équipe : ");
        String name = next();

        ArrayList<String> players = new ArrayList<>();
        out.println("Entrez le nom du chef d'équipe : ");
        players.add(next());
        out.println("Entrez le nom du deuxième joueur : ");
        players.add(next());
        out.println("Entrez le nom du dernier joueur : ");
        players.add(next());

        Team team = new Team(players, name, null, null, null);

        // Envoi de l'événement de création d'équipe
        MVCApp.getInstance().getClient().sendEvent(new Event<>(EventType.CREATE_TEAM, team));
    }

    public static void showNewTeamCreatedAction(Team team) {
        if (team == null) {
            out.println("L'équipe n'a pas pû être créée");
            return;
        }

        out.println("L'équipe a été créée : ");
        out.println(team);
    }

    public static void updateTeamAction() throws IOException, InterruptedException {
        out.println("Entrez le nom de l'équipe à modifier : ");
        String name = next();

        out.println("""
            Entrez les nouvelles informations de l'équipe (vide pour garder les anciennes valeurs)
            Nom de l'équipe :
            """);
        String newName = next();

        ArrayList<String> players = new ArrayList<>();
        out.println("Nom du chef d'équipe : ");
        players.add(next());
        out.println("Nom du deuxième joueur : ");
        players.add(next());
        out.println("Nom du dernier joueur : ");
        players.add(next());

        Event<TeamUpdate> event = new Event<>(
                EventType.UPDATE_TEAM,
                new TeamUpdate(name, newName, players.getFirst(), players.get(1), players.getLast()));

        out.println(event);

        // Envoi de l'événement de modification de l'équipe
        MVCApp.getInstance().getClient().sendEvent(event);
    }

    public static void showTeamUpdatedAction(Team team) {
        if (team == null) {
            out.println("L'équipe n'a pas pû être modifiée");
            return;
        }

        out.println("L'équipe a été modifiée : ");
        out.println(team);
    }
}
