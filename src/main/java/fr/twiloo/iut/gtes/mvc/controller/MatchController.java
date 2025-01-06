package fr.twiloo.iut.gtes.mvc.controller;

import fr.twiloo.iut.gtes.common.EventType;
import fr.twiloo.iut.gtes.common.model.Event;
import fr.twiloo.iut.gtes.common.model.Match;
import fr.twiloo.iut.gtes.mvc.MVCApp;
import fr.twiloo.iut.gtes.mvc.view.View;

import java.io.IOException;

import static fr.twiloo.iut.gtes.common.utils.Input.next;
import static java.lang.System.out;

public final class MatchController {

    public static void scheduleMatchAction() throws InterruptedException, IOException {
        // Collecte des informations pour le match
        out.println(View.ASK_TEAM_A_NAME);
        String teamAName = next();
        out.println(View.ASK_TEAM_B_NAME);
        String teamBName = next();

        Match match = new Match(teamAName, teamBName, null, 0, 0);
        // Envoi de l'événement de création du match
        MVCApp.getInstance().getClient().sendEvent(new Event<>(EventType.CREATE_MATCH, match));
    }

    public static void showNewMatchCreatedAction(Match match) {
        if (match == null) {
            out.println(View.MATCH_NON_CREE);
            return;
        }

        out.println(View.MATCH_CREE);
        out.println(match);
    }

    public static void playMatchAction() throws InterruptedException, IOException {
        out.println(View.ASK_TEAM_A_NAME);
        String teamAName = next();
        out.println(View.ASK_TEAM_B_NAME);
        String teamBName = next();
        out.println(View.ASK_TEAM_A_SCORE);
        int teamAScore = Integer.parseInt(next());
        out.println(View.ASK_TEAM_B_SCORE);
        int teamBScore = Integer.parseInt(next());

        Match match = new Match(teamAName, teamBName, null, teamAScore, teamBScore);
        // Envoi de l'événement de fin du match
        MVCApp.getInstance().getClient().sendEvent(new Event<>(EventType.SET_MATCH_RESULTS, match));
    }

    public static void showMatchFinishedAction(Match match) {
        if (match == null) {
            out.println(View.MATCH_NON_FINI);
            return;
        }
        out.println(View.MATCH_FINI);
        out.println(match);
    }

    public static void cancelMatchAction() throws InterruptedException, IOException {
        out.println(View.ASK_TEAM_A_NAME);
        String teamAName = next();
        out.println(View.ASK_TEAM_B_NAME);
        String teamBName = next();

        Match match = new Match(teamAName, teamBName, null, 0, 0);
        // Envoi de l'événement d'annulation du match
        MVCApp.getInstance().getClient().sendEvent(new Event<>(EventType.CANCEL_MATCH, match));
    }

    public static void showMatchCanceledAction(Match match) {
        if (match == null) {
            out.println(View.MATCH_NON_ANNULE);
            return;
        }
        out.println(View.MATCH_ANNULE);
        out.println(match);
    }
}
