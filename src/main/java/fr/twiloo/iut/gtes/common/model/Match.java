package fr.twiloo.iut.gtes.common.model;

import fr.twiloo.iut.gtes.common.MatchStatus;

import java.io.Serializable;

public final class Match implements Serializable {
    private String teamAName;
    private String teamBName;
    private MatchStatus status;
    private int scoreA;
    private int scoreB;

    public Match(String teamAName, String teamBName, MatchStatus status, int scoreA, int scoreB) {
        this.teamAName = teamAName;
        this.teamBName = teamBName;
        this.status = status;
        this.scoreA = scoreA;
        this.scoreB = scoreB;
    }

    public String getTeamAName() {
        return teamAName;
    }

    public void setTeamAName(String teamAName) {
        this.teamAName = teamAName;
    }

    public String getTeamBName() {
        return teamBName;
    }

    public void setTeamBName(String teamBName) {
        this.teamBName = teamBName;
    }

    public MatchStatus getStatus() {
        return status;
    }

    public void setStatus(MatchStatus status) {
        this.status = status;
    }

    public int getScoreA() {
        return scoreA;
    }

    public void setScoreA(int scoreA) {
        this.scoreA = scoreA;
    }

    public int getScoreB() {
        return scoreB;
    }

    public void setScoreB(int scoreB) {
        this.scoreB = scoreB;
    }

    @Override
    public String toString() {
        return "Match " + teamAName + "-" + teamBName + " : " + status + " (" + scoreA + "-" + scoreB + ")";
    }
}
