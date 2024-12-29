package fr.twiloo.iut.gtes.mvc.controller;

import fr.twiloo.iut.gtes.App;
import fr.twiloo.iut.gtes.mvc.MVCApp;
import fr.twiloo.iut.gtes.mvc.view.View;

import java.io.IOException;

import static java.lang.System.out;

public final class DefaultController {

    public static void defaultAction() throws IOException {
        while (true) {
            out.println(View.DEFAULT_MENU);
            int option;
            try {
                option = App.sc.nextInt();
            } catch (Exception e) {
                App.sc.nextLine();
                continue;
            }
            switch (option) {
                case 1:
                    try {
                        TeamController.showTeamsListAction();
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
                    closeApp();
                    return;
            }
            App.sc.nextLine();
            out.println();
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                closeApp();
            }
        }
    }

    public static void closeApp() {
        out.println("Closing application...");
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
                Thread.currentThread().interrupt();
            }
        }
    }
}
