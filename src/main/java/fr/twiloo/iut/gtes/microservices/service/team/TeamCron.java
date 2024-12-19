package fr.twiloo.iut.gtes.microservices.service.team;

import fr.twiloo.iut.gtes.common.Team;

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

                    for (int i = 0; i < teams.size(); i++) {
                        teams.get(i).setRanking(i + 1);
                    }
                }

                Thread.sleep(300000); // wait 5 minutes for next cron task
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void stop() {
        running = false;
    }
}
