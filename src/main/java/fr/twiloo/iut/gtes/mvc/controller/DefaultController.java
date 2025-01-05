package fr.twiloo.iut.gtes.mvc.controller;

import fr.twiloo.iut.gtes.mvc.MVCApp;
import fr.twiloo.iut.gtes.mvc.view.View;

import java.io.IOException;

import static fr.twiloo.iut.gtes.common.utils.Input.next;
import static fr.twiloo.iut.gtes.common.utils.Input.stop;
import static java.lang.System.*;

public final class DefaultController {
    private static boolean running = true;

    public static void startDefaultActionAsync() {
        Thread defaultActionThread = new Thread(DefaultController::defaultAction);
        defaultActionThread.setDaemon(true); // Ensures the thread doesn’t prevent JVM shutdown
        defaultActionThread.start();
    }

    public static void defaultAction() {
        while (running) {
            try {
                out.println(View.DEFAULT_MENU);
                int option = Integer.parseInt(next());
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
                        TeamController.updateTeamAction();
                        break;
                    case 4:
                        TeamController.deleteTeamAction();
                        break;
                    case 5:
                        MatchController.scheduleMatchAction();
                        break;
                    case 6:
                        MatchController.playMatchAction();
                        break;
                    case 7:
                        MatchController.cancelMatchAction();
                        break;
                    case 8:
                        closeApplicationAction();
                        exit(0);
                    default:
                        err.println(option + " n'est pas valide");
                }
                out.println();
                Thread.sleep(1000);
            } catch (Exception e) {
                err.println("La saisie n'est pas valide");
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
                stop();
            }
        }
    }
}
