package fr.twiloo.iut.gtes.mvc.controller;

import fr.twiloo.iut.gtes.common.EventType;
import fr.twiloo.iut.gtes.common.model.Event;
import fr.twiloo.iut.gtes.common.model.Match;
import fr.twiloo.iut.gtes.mvc.MVCApp;

import java.io.IOException;

import static fr.twiloo.iut.gtes.common.utils.Input.next;
import static java.lang.System.out;

public final class MatchController {

    public static void scheduleMatchAction() throws InterruptedException, IOException {
        // Collecte des informations pour le match
        out.println("Entrez le nom de la première équipe : ");
        String teamAName = next();
        out.println("Entrez le nom de la deuxième équipe : ");
        String teamBName = next();

        Match match = new Match(teamAName, teamBName, null, 0, 0);
        // Envoi de l'événement de création du match
        MVCApp.getInstance().getClient().sendEvent(new Event<>(EventType.CREATE_MATCH, match));
    }

    public static void showNewMatchCreatedAction(Match match) {
        if (match == null) {
            out.println("Le match n'a pas pû être créé");
            return;
        }

        out.println("Le match a été créé : ");
        out.println(match);
    }

    // TODO
    public static void playMatchAction() {

    }

    public static void showMatchFinishedAction(Match match) {

    }

    public static void cancelMatchAction() {

    }

    public static void showMatchCanceledAction(Match match) {

    }
}
