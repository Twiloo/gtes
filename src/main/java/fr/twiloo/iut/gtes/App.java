package fr.twiloo.iut.gtes;

import fr.twiloo.iut.gtes.common.Config;
import fr.twiloo.iut.gtes.common.utils.Input;
import fr.twiloo.iut.gtes.eventbus.EventBus;
import fr.twiloo.iut.gtes.microservices.match.MatchService;
import fr.twiloo.iut.gtes.microservices.team.TeamService;
import fr.twiloo.iut.gtes.mvc.MVCApp;

import java.io.IOException;

import static fr.twiloo.iut.gtes.common.utils.Input.next;
import static java.lang.System.out;

public final class App {
    private static boolean running = true;

    public static void main(String[] args) throws IOException {
        Input.start();

        while (running) {
            out.println("""
                    Quelle application/service démarrer ?
                    
                     1 : MVC ;
                     2 : EventBus ;
                     3 : Service Equipes ;
                     4 : Service Matchs ;
                     5 : Service Notifications ;
                     6 : Quitter.
                    """);
            int option = -1;
            try {
                option = Integer.parseInt(next());
            } catch (Exception ignored) { }
            switch (option) {
                case 1:
                    MVCApp.getInstance();
                    return;
                case 2:
                    EventBus eventBus = new EventBus(Config.EVENT_BUS.port);
                    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                        out.println("Closing EventBus...");
                        running = false;
                        try {
                            eventBus.stop();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }));
                    return;
                case 3:
                    // Lancer l'application de gestion d'équipes
                    TeamService teamService = new TeamService();
                    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                        out.println("Closing TeamService...");
                        running = false;
                        teamService.close();
                    }));
                    return;
                case 4:
                    // Lancer le Service Matchs
                    MatchService matchService = new MatchService();
                    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                        out.println("Closing MatchService...");
                        running = false;
                        matchService.close();
                    }));
                    return;
                case 5:
                    // Lancer NotificationService
                    out.println("Notification service (non implémenté pour le moment).");
                    break;
                case 6:
                    out.println("Exiting...");
                    running = false;
                    Input.stop();
                    break;
                default:
                    out.println("Option invalide. Essayez de nouveau.");
            }
        }

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.exit(0);
    }
}
