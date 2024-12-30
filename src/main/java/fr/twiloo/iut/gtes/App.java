package fr.twiloo.iut.gtes;

import fr.twiloo.iut.gtes.common.Config;
import fr.twiloo.iut.gtes.eventbus.EventBus;
import fr.twiloo.iut.gtes.microservices.team.TeamService;
import fr.twiloo.iut.gtes.mvc.MVCApp;
import fr.twiloo.iut.gtes.mvc.controller.DefaultController;
import fr.twiloo.iut.gtes.mvc.controller.TeamController;

import java.io.IOException;
import java.util.Scanner;

import static java.lang.System.out;

public final class App {
    public final static Scanner sc = new Scanner(System.in);
    private static boolean running = true;

    public static void main(String[] args) throws IOException {
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
            int option;
            try {
                option = sc.nextInt();
            } catch (Exception e) {
                sc.nextLine();
                continue;
            }
            switch (option) {
                case 1:
                    MVCApp.getInstance();
                    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                        out.println("Closing MVCApp...");
                        running = false;
                        try {
                            DefaultController.closeApp();
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }));
                    return;
                case 2:
                    EventBus eventBus = new EventBus(Config.EVENT_BUS.port);
                    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                        out.println("Closing EventBus...");
                        running = false;
                    }));
                    return;
                case 3:
                    // Lancer l'application de gestion d'équipes
                    TeamService teamService = new TeamService();
                    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                        out.println("Closing TeamService...");
                        running = false;
                        teamService.stop();
                    }));
                    return;
                case 4:
                    // Lancer le Service Matchs
                    out.println("Match service (non implémenté pour le moment).");
                    break;
                case 5:
                    // Lancer NotificationService
                    out.println("Notification service (non implémenté pour le moment).");
                    break;
                case 6:
                    out.println("Exiting...");
                    running = false;
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
