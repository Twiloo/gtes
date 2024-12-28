package fr.twiloo.iut.gtes.microservices.match;
//
//import fr.twiloo.iut.gtes.mvc.event.match.MatchFinishedEvent;
//import fr.twiloo.iut.gtes.mvc.event.update.RankingUpdatedEvent;
//
//import java.util.Map;
//
//import static java.lang.System.out;
//
//public class MatchService {
//
//    public void organizeMatch(String team1, int score1, String team2, int score2, Map<String, Integer> teamScores) {
//        out.println("\n=== Organizing Match ===");
//        out.println(team1 + " [" + score1 + "] vs " + team2 + " [" + score2 + "]");
//
//        int pointsTeam1 = 0, pointsTeam2 = 0;
//
//        // Calculate points
//        if (score1 > score2) {
//            pointsTeam1 = 3;
//            out.println(team1 + " wins!");
//        } else if (score1 < score2) {
//            pointsTeam2 = 3;
//            out.println(team2 + " wins!");
//        } else {
//            pointsTeam1 = 1;
//            pointsTeam2 = 1;
//            out.println("It's a draw!");
//        }
//
//        // Update team scores
//        teamScores.put(team1, teamScores.get(team1) + pointsTeam1);
//        teamScores.put(team2, teamScores.get(team2) + pointsTeam2);
//
//        // Publish MatchFinishedEvent
//        MatchFinishedEvent event = new MatchFinishedEvent(team1, score1, team2, score2);
//        out.println("Event Published: " + event.getName());
//
//        // Publish RankingUpdatedEvent
//        RankingUpdatedEvent rankingEvent = new RankingUpdatedEvent(teamScores);
//        out.println("Event Published: " + rankingEvent.getName());
//    }
//}


/*
package fr.twiloo.iut.gtes.microservices;

import fr.twiloo.iut.gtes.common.model.Match;
import fr.twiloo.iut.gtes.common.model.dto.Request;
import fr.twiloo.iut.gtes.common.model.Team;

import java.io.IOException;

public class MatchService {

    private Match currentMatch;  // Stocke le match actuel en cours

    protected MatchService(int port) throws IOException {
        super(port);
    }

    // Méthode pour organiser un match entre deux équipes
    public void organizeMatch(Team teamA, Team teamB) {
        // Crée un match non terminé avec un score initial de 0-0
        this.currentMatch = new Match(teamA, teamB, false, 0, 0);
    }

    // Méthode pour marquer le match comme terminé et définir les scores
    public Match finishMatch(int scoreA, int scoreB) {
        if (currentMatch == null) {
            throw new IllegalStateException("No match has been organized yet.");
        }
        // Crée un nouveau match avec le statut terminé et les nouveaux scores
        currentMatch = new Match(currentMatch.teamA(), currentMatch.teamB(), true, scoreA, scoreB);
        return currentMatch;
    }

    // Méthode pour annuler un match
    public void cancelMatch() {
        if (currentMatch == null) {
            throw new IllegalStateException("No match to cancel.");
        }
        // Réinitialiser le match à son état initial (ou supprimer la référence)
        currentMatch = null;  // On supprime la référence du match en cours
    }

    @Override
    Object run(Request request) throws Exception {
        return switch (request.getAction()) {
            case CREATE_MATCH -> {
                Team[] teams = (Team[]) request.getPayload();
                organizeMatch(teams[0], teams[1]);
                yield "Match organized between " + teams[0].getName() + " and " + teams[1].getName();
            }
            case END_MATCH -> {
                int[] scores = (int[]) request.getPayload();
                finishMatch(scores[0], scores[1]);
                yield "Match finished with score " + scores[0] + " - " + scores[1];
            }
            case CANCEL_MATCH -> {
                cancelMatch();
                yield "Match has been cancelled.";
            }
            default -> throw new Exception("Service is unable to process this getAction: " + request.getAction());
        };
    }

    // Méthode pour obtenir le gagnant du match
    public Team getWinner() {
        if (currentMatch == null || !currentMatch.isFinished()) {
            throw new IllegalStateException("Match not finished yet.");
        }
        if (currentMatch.scoreA() > currentMatch.scoreB()) {
            return currentMatch.teamA();
        } else if (currentMatch.scoreA() < currentMatch.scoreB()) {
            return currentMatch.teamB();
        } else {
            return null; // Match nul
        }
    }

    // Méthode pour calculer et obtenir le score final sous forme de chaîne
    public String getScore() {
        if (currentMatch == null) {
            throw new IllegalStateException("No match has been organized yet.");
        }
        return currentMatch.scoreA() + " - " + currentMatch.scoreB();
    }

    // Vérifie si le match est en cours
    public boolean isInProgress() {
        return currentMatch == null || !currentMatch.isFinished();
    }
}
*/
