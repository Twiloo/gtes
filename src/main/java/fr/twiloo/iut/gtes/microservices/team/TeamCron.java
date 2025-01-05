package fr.twiloo.iut.gtes.microservices.team;

import fr.twiloo.iut.gtes.common.model.Team;

import java.util.List;

public final class TeamCron implements Runnable {
    private final List<Team> teams;
    private boolean running = true;

    public TeamCron(List<Team> teams) {
        this.teams = teams;
    }

    @Override
    public void run() {
        while (running) {
            try {
                // order teams by elo, then update their ranking from 1 to n
                synchronized (teams) {
                    teams.sort((team1, team2) -> Integer.compare(team2.getElo(), team1.getElo()));

                    int rank = 0;
                    for (Team team : teams) {
                        team.setRanking(team.isActive() ? rank++ : -1);
                    }
                }
                Thread.sleep(5000); // wait 5 seconds for next cron task
            } catch (InterruptedException ignored) { }
        }
    }

    public void stop() {
        running = false;
    }
}
