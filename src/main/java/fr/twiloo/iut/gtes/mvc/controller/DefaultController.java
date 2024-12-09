package fr.twiloo.iut.gtes.mvc.controller;

import fr.twiloo.iut.gtes.App;
import fr.twiloo.iut.gtes.mvc.view.View;

import static java.lang.System.out;

public final class DefaultController {

    public static void defaultAction() {
        while (true) {
            out.println(View.DEFAULT_MENU);
            int option;
            try {
                option = App.sc.nextInt();
            } catch (Exception e) {
                continue;
            }
            switch (option) {
                case 1:
                    // Voir les équipes
                    return;
                case 2:
                    // Créer une équipe
                    return;
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
            }
        }
    }
}
