package fr.twiloo.iut.gtes.mvc.controller;

import fr.twiloo.iut.gtes.common.EventType;
import fr.twiloo.iut.gtes.common.model.Event;
import fr.twiloo.iut.gtes.common.model.Team;
import fr.twiloo.iut.gtes.mvc.MVCApp;

import java.io.IOException;
import java.util.ArrayList;

import static java.lang.System.out;

public final class TeamController {

    public static void getTeamsListAction() throws IOException {
        MVCApp.getInstance().getClient().sendEvent(new Event<>(EventType.GET_TEAMS_LIST, null));
    }

    public static void showTeamsListAction(Event<?> event) {
        out.println("Liste des équipes du tournoi :");
        if (event.payload() instanceof ArrayList<?> && !((ArrayList<?>) event.payload()).isEmpty()) {
            for (Object team : (ArrayList<?>) event.payload()) {
                if (team instanceof Team) {
                    out.println(team);
                }
            }
        }
    }

    public static void addTeamAction() {

    }
}
