package fr.twiloo.iut.gtes;

import fr.twiloo.iut.gtes.microservices.TeamService;
import fr.twiloo.iut.gtes.mvc.MVCApp;

import java.io.IOException;
import java.util.Scanner;

import static java.lang.System.out;

public class App {
    public final static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) throws IOException {
        while (true) {
            out.println("""
                    Quelle application/service démarrer ?
                    
                     1 : MVC ;
                     2 : Service Equipes ;
                     3 : Service Matchs ;
                     4 : Service Notifications.
                    """);
            int option;
            try {
                option = sc.nextInt();
            } catch (Exception e) {
                continue;
            }
            switch (option) {
                case 1:
                    new MVCApp();
                    return;
                case 2:
                    new TeamService();
                    return;
                case 3:
                    // MatchService
                    return;
                case 4:
                    // NotificationService
                    return;
            }
        }
    }
}
