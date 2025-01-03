package fr.twiloo.iut.gtes;

import fr.twiloo.iut.gtes.common.Config;
import fr.twiloo.iut.gtes.eventbus.EventBus;
import fr.twiloo.iut.gtes.microservices.team.TeamService;
import fr.twiloo.iut.gtes.mvc.MVCApp;
import fr.twiloo.iut.gtes.mvc.controller.DefaultController;

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
                     5 : Service Notifications.
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
                        try {
                            eventBus.stop();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }));
                    return;
                case 3:
                    TeamService teamService = new TeamService();
                    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                        out.println("Closing TeamService...");
                        running = false;
                        teamService.stop();
                    }));
                    return;
                case 4:
                    // MatchService
                    return;
                case 5:
                    // NotificationService
                    return;
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

//import fr.twiloo.iut.gtes.microservices.service.match.MatchService;
//
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Scanner;
//
//import static java.lang.System.out;
//
//public class App {
//    public final static Scanner sc = new Scanner(System.in);
//    private static final MatchService matchService = new MatchService();
//    private static final Map<String, Integer> teamScores = new HashMap<>();
//
//    public static void main(String[] args) throws IOException {
//        while (true) {
//            out.println("""
//                    === Tournament Manager ===
//                    1. Add a team
//                    2. Organize a match
//                    3. Display ranking
//                    4. Quit
//                    """);
//            int option;
//            try {
//                option = sc.nextInt();
//                sc.nextLine(); // Consume newline
//            } catch (Exception e) {
//                out.println("Invalid input! Try again.");
//                sc.nextLine();
//                continue;
//            }
//
//            switch (option) {
//                case 1:
//                    addTeam();
//                    break;
//                case 2:
//                    organizeMatch();
//                    break;
//                case 3:
//                    displayRanking();
//                    break;
//                case 4:
//                    out.println("Exiting...");
//                    return;
//                default:
//                    out.println("Invalid option. Try again.");
//            }
//        }
//    }
//
//    private static void addTeam() {
//        out.println("Enter team getName:");
//        String teamName = sc.nextLine();
//        teamScores.putIfAbsent(teamName, 0);
//        out.println("Team \"" + teamName + "\" added successfully!");
//    }
//
//    private static void organizeMatch() {
//        out.println("Enter the getName of Team 1:");
//        String team1 = sc.nextLine();
//        out.println("Enter the getName of Team 2:");
//        String team2 = sc.nextLine();
//
//        if (!teamScores.containsKey(team1) || !teamScores.containsKey(team2)) {
//            out.println("Both teams must exist. Please add the teams first.");
//            return;
//        }
//
//        out.println("Enter score for " + team1 + ":");
//        int score1 = sc.nextInt();
//        out.println("Enter score for " + team2 + ":");
//        int score2 = sc.nextInt();
//        sc.nextLine(); // Consume newline
//
//        // Calculate points and publish events
//        matchService.organizeMatch(team1, score1, team2, score2, teamScores);
//
//        // Display updated ranking
//        displayRanking();
//    }
//
//    private static void displayRanking() {
//        out.println("\n=== Team Rankings ===");
//        teamScores.forEach((team, points) -> out.println("Team: " + team + " | Points: " + points));
//        out.println();
//    }
//}
