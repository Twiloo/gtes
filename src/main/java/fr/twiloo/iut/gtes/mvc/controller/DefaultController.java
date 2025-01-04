package fr.twiloo.iut.gtes.mvc.controller;

import fr.twiloo.iut.gtes.App;
import fr.twiloo.iut.gtes.mvc.MVCApp;
import fr.twiloo.iut.gtes.mvc.view.View;

import java.io.IOException;

import static java.lang.System.exit;
import static java.lang.System.out;

public final class DefaultController {
    private static boolean running = true;

    public static void startDefaultActionAsync() {
        Thread defaultActionThread = new Thread(() -> {
            try {
                defaultAction();
            } catch (IOException e) {
                out.println("Erreur (tout est cassé) : " + e.getMessage());
            }
        });
        defaultActionThread.setDaemon(true); // Ensures the thread doesn’t prevent JVM shutdown
        defaultActionThread.start();
    }

    public static void defaultAction() throws IOException {
        while (running) {
            out.println(View.DEFAULT_MENU);
            int option;
            try {
                option = App.sc.nextInt();
            } catch (Exception e) {
                if (!running)
                    return;
                App.sc.nextLine();
                continue;
            }
            switch (option) {
                case 1:
                    try {
                        TeamController.getTeamListAction();
                    } catch (IOException e) {
                        out.println("Une erreur s'est produite lors de l'affichage de la liste des équipes : " + e.getMessage());
                    }
                    break;
                case 2:
                    TeamController.addTeamAction();
                    break;
                case 3:
                    // Modifier une équipe
                    return;
                case 4:
                    // Supprimer une équipe
                    return;
                case 5:
                    // Organiser un match
                    return;
                case 6:
                    // Jouer un match
                    return;
                case 7:
                    // Annuler un match
                    return;
                case 8:
                    // Arrêter l'application
                    closeApplicationAction();
                    exit(0);
            }
            App.sc.nextLine();
            out.println();
            try {
                Thread.sleep(1000);
                out.println();
            } catch (InterruptedException e) {
                closeApplicationAction();
            }
        }
    }

    public static void closeApplicationAction() {
        out.println("Closing application...");
        running = false;
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                MVCApp.getInstance().getClient().close();
            } catch (IOException ignored) { }
            finally {
                App.sc.close();
            }
        }
    }
}
