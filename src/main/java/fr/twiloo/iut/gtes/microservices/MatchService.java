package fr.twiloo.iut.gtes.microservices;

import fr.twiloo.iut.gtes.common.Match;
import fr.twiloo.iut.gtes.common.Request;
import fr.twiloo.iut.gtes.common.Team;

import java.io.IOException;

public class MatchService extends CallableService {

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
        return switch (request.action()) {
            case MATCH_ORGANISE -> {
                Team[] teams = (Team[]) request.payload();
                organizeMatch(teams[0], teams[1]);
                yield "Match organized between " + teams[0].name() + " and " + teams[1].name();
            }
            case MATCH_TERMINE -> {
                int[] scores = (int[]) request.payload();
                finishMatch(scores[0], scores[1]);
                yield "Match finished with score " + scores[0] + " - " + scores[1];
            }
            case MATCH_ANNULE -> {
                cancelMatch();
                yield "Match has been cancelled.";
            }
            default -> throw new Exception("Service is unable to process this action: " + request.action());
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
