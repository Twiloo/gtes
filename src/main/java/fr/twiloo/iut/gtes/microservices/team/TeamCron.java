package fr.twiloo.iut.gtes.microservices.team;

import fr.twiloo.iut.gtes.common.EventType;
import fr.twiloo.iut.gtes.common.client.Client;
import fr.twiloo.iut.gtes.common.model.Event;
import fr.twiloo.iut.gtes.common.model.Team;

import java.util.List;

import static java.lang.System.err;
import static java.lang.System.out;

public final class TeamCron implements Runnable {
    private final List<Team> teams;
    private final Client client;
    private boolean running = true;

    public TeamCron(List<Team> teams, Client client) {
        this.teams = teams;
        this.client = client;
    }

    @SuppressWarnings("BusyWait")
    @Override
    public void run() {
        while (running) {
            try {
                // order teams by elo, then update their ranking from 1 to n
                synchronized (teams) {
                    teams.sort((team1, team2) -> Integer.compare(team2.getElo(), team1.getElo()));

                    int rank = 1;
                    for (Team team : teams) {
                        if (!team.isActive()) {
                            team.setRanking(-1);
                            break;
                        }
                        int previousRank = team.getRanking();
                        team.setRanking(rank++);
                        if (previousRank != team.getRanking()) {
                            out.println("Envoi d'un évènement de mise à jour du classement de l'équipe");
                            client.sendEvent(new Event<>(EventType.RANKING_UPDATED, team));
                        }
                    }
                }
                Thread.sleep(10000); // wait 10 seconds for next cron task
            } catch (InterruptedException exception) {
                err.println(exception.getMessage());
                err.println("La mise à jour du classement des équipes ne fonctionne plus.");
                Thread.currentThread().interrupt();
            }
        }
    }

    public void stop() {
        running = false;
    }
}
