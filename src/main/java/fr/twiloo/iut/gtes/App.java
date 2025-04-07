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

    public static void main(String[] args) throws IOException {
        String role = getRoleArg(args);
        if (role != null) {
            launchByRole(role.toLowerCase());
        } else {
            Input.start();
            interactiveMenu();
        }
        shutdownGracefully();
    }

    private static void shutdownGracefully() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.exit(0);
    }

    private static String getRoleArg(String[] args) {
        for (String arg : args) {
            if (arg.startsWith("--role=")) {
                return arg.substring("--role=".length());
            }
        }
        return null;
    }

    private static void launchByRole(String role) throws IOException {
        switch (role) {
            case "mvc" -> launchMVC();
            case "bus" -> startEventBus();
            case "team" -> startTeamService();
            case "match" -> startMatchService();
            case "notif" -> startNotificationService();
            default -> {
                out.println("Rôle inconnu : " + role);
                interactiveMenu();
            }
        }
    }

    private static void launchMVC() throws IOException {
        Input.start();
        MVCApp.getInstance();
        letItRun();
    }

    private static void startNotificationService() throws IOException {
        NotificationService notificationService = new NotificationService();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            out.println("Closing NotificationService...");
            running = false;
            notificationService.close();
        }));
        notificationTimeoutDetection(notificationService);
        letItRun();
    }

    private static void startMatchService() throws IOException {
        MatchService matchService = new MatchService();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            out.println("Closing MatchService...");
            running = false;
            matchService.close();
        }));
        letItRun();
    }

    private static void notificationTimeoutDetection(NotificationService notificationService) {
        while (running) {
            try {
                Thread.sleep(30000); // Toutes les 30 secondes, vérifier la connexion au bus
                notificationService.sendEvent(new Event<>(EventType.CONNECTION_TEST, null), false);
            } catch (RuntimeException | InterruptedException ignored) {
                out.println("Le bus n'est plus accessible");
                System.exit(1);
            }
        }
    }

    private static void startEventBus() throws IOException {
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
        letItRun();
    }

    private static void startTeamService() throws IOException {
        TeamService teamService = new TeamService();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            out.println("Closing TeamService...");
            running = false;
            teamService.close();
        }));
        letItRun();
    }

    private static void interactiveMenu() throws IOException {
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
                case 1 -> launchMVC();
                case 2 -> startEventBus();
                case 3 -> startTeamService();
                case 4 -> startMatchService();
                case 5 -> startNotificationService();
                case 6 -> {
                    out.println("Exiting...");
                    running = false;
                    Input.stop();
                }
                default -> out.println("Option invalide. Essayez de nouveau.");
            }
        }
    }

    private static void letItRun() {
        while (running) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}
