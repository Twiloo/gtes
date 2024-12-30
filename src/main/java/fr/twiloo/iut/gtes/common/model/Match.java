package fr.twiloo.iut.gtes.common.model;

public class Match {
    private final String teamAName;
    private final String teamBName;
    private final Integer scoreA;
    private final Integer scoreB;

    public Match(Team teamA, Team teamB) {
        this.teamAName = teamA.getName();
        this.teamBName = teamB.getName();
        this.scoreA = 0;
        this.scoreB = 0;
    }

    // Getters Setters
    public String getTeamAName() { return teamAName; }
    public String getTeamBName() { return teamBName; }
    public Integer getScoreA() { return scoreA; }
    public Integer getScoreB() { return scoreB; }
}
