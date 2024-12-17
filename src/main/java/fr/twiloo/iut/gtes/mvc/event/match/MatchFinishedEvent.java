package fr.twiloo.iut.gtes.mvc.event.match;


import fr.twiloo.iut.gtes.mvc.event.Event;

public class MatchFinishedEvent extends Event<String, Void> {
    private final String team1;
    private final int score1;
    private final String team2;
    private final int score2;

    public MatchFinishedEvent(String team1, int score1, String team2, int score2) {
        super(team1 + " [" + score1 + "] vs " + team2 + " [" + score2 + "]");
        this.team1 = team1;
        this.score1 = score1;
        this.team2 = team2;
        this.score2 = score2;
    }

    @Override
    public String getName() {
        return "MatchFinishedEvent";
    }

    public String getDetails() {
        return team1 + " [" + score1 + "] vs " + team2 + " [" + score2 + "]";
    }
}
