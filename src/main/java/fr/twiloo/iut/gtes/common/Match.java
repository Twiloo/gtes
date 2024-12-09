package fr.twiloo.iut.gtes.common;

public record Match(
       Team teamA,
       Team teamB,
       boolean isFinished,
       Integer scoreA,
       Integer scoreB) {

    // Constructeur pour initialiser un match non terminé, sans score
    public Match(Team teamA, Team teamB) {
        this(teamA, teamB, false, 0, 0);
    }
}