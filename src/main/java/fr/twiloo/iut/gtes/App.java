package fr.twiloo.iut.gtes;

import fr.twiloo.iut.gtes.common.Config;
import fr.twiloo.iut.gtes.common.EventType;
import fr.twiloo.iut.gtes.common.model.Event;
import fr.twiloo.iut.gtes.common.utils.Input;
import fr.twiloo.iut.gtes.eventbus.EventBus;
import fr.twiloo.iut.gtes.microservices.match.MatchService;
import fr.twiloo.iut.gtes.microservices.notification.NotificationService;
import fr.twiloo.iut.gtes.microservices.team.TeamService;
import fr.twiloo.iut.gtes.mvc.MVCApp;

import java.io.IOException;

import static fr.twiloo.iut.gtes.common.utils.Input.next;
import static java.lang.System.out;

public final class App {
    private static boolean running = true;

    @SuppressWarnings("BusyWait")
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
                    // Lancer l'application client
                    MVCApp.getInstance();
                    return;
                case 2:
                    // Lancer le bus d'évènements
                    EventBus eventBus = new EventBus((Integer) Config.EVENT_BUS_PORT.value);
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
                    // Lancer le service de gestion des équipes
                    TeamService teamService = new TeamService();
                    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                        out.println("Closing TeamService...");
                        running = false;
                        teamService.close();
                    }));
                    return;
                case 4:
                    // Lancer le service des matchs
                    MatchService matchService = new MatchService();
                    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                        out.println("Closing MatchService...");
                        running = false;
                        matchService.close();
                    }));
                    return;
                case 5:
                    // Lancer le service des notifications
                    NotificationService notificationService = new NotificationService();
                    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                        out.println("Closing NotificationService...");
                        running = false;
                        notificationService.close();
                    }));
                    while (running) {
                        try {
                            Thread.sleep(30000); // Toutes les 30 secondes, vérifier la connexion au bus
                            notificationService.sendEvent(new Event<>(EventType.CONNECTION_TEST, null), false);
                        } catch (RuntimeException | InterruptedException ignored) {
                            out.println("Le bus n'est plus accessible");
                            System.exit(1);
                        }
                    }
                    return;
                case 6:
                    // Arrêter l'application
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
