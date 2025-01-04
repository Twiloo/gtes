package fr.twiloo.iut.gtes.mvc.controller;

import fr.twiloo.iut.gtes.common.EventType;
import fr.twiloo.iut.gtes.common.model.Event;
import fr.twiloo.iut.gtes.common.model.Team;
import fr.twiloo.iut.gtes.mvc.MVCApp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import static java.lang.System.out;

public final class TeamController {

    public static void addTeamAction() throws IOException {
        Scanner scanner = new Scanner(System.in);

        // Collecte des informations pour l'équipe
        out.println("Entrez le nom de l'équipe : ");
        String name = scanner.nextLine();

        out.println("Entrez les noms des joueurs (séparés par des virgules) : ");
        String playersInput = scanner.nextLine();
        ArrayList<String> players = new ArrayList<>();
        for (String player : playersInput.split(",")) {
            players.add(player.trim());
        }

        out.println("Entrez le classement initial de l'équipe : ");
        int ranking = scanner.nextInt();

        out.println("Entrez l'Elo de l'équipe : ");
        int elo = scanner.nextInt();

        // Création de l'objet Team
        Team team = new Team(players, name, elo, ranking, true);

        // Envoi d'un événement de type CREATE_TEAM
        Event<Team> event = new Event<>(EventType.CREATE_TEAM, team);
        MVCApp.getInstance().getClient().sendEvent(event);

        out.println("Équipe ajoutée avec succès (en attente de confirmation du serveur).");
    }

    public static void showTeamsListAction() throws IOException {
        // Envoyer une demande pour récupérer les équipes
        MVCApp.getInstance().getClient().sendEvent(new Event<>(EventType.GET_TEAMS_LIST, null));

        // Ici, l'attente d'une réponse peut être ajoutée si le bus d'événements est asynchrone
        System.out.println("Demande envoyée pour afficher la liste des équipes. Veuillez attendre...");
    }

}
