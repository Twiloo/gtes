package fr.twiloo.iut.gtes.mvc;

import fr.twiloo.iut.gtes.common.Config;
import fr.twiloo.iut.gtes.common.EventType;
import fr.twiloo.iut.gtes.common.client.Client;
import fr.twiloo.iut.gtes.common.client.EventDispatcher;
import fr.twiloo.iut.gtes.common.model.Event;
import fr.twiloo.iut.gtes.mvc.controller.DefaultController;
import fr.twiloo.iut.gtes.mvc.controller.TeamController;

import java.io.IOException;
import java.util.List;

public final class MVCApp implements EventDispatcher {
    private static MVCApp instance;
    private final Client client;

    public static MVCApp getInstance() throws IOException {
        if (instance == null)
            instance = new MVCApp();
        return instance;
    }

    private MVCApp() throws IOException {
        client = new Client(Config.EVENT_BUS, this);
        DefaultController.defaultAction();
    }

    public Client getClient() {
        return client;
    }

    @Override
    public void dispatch(Event<?> event) {
        switch (event.type()) {
            case SHOW_TEAMS_LIST -> TeamController.showTeamsListAction(event);
        }
    }

    @Override
    public List<EventType> supportedEventTypes() {
        return List.of(EventType.SHOW_TEAMS_LIST,
                EventType.NEW_TEAM_CREATED,
                EventType.TEAM_UPDATED,
                EventType.TEAM_DELETED,
                EventType.NEW_MATCH_CREATED,
                EventType.MATCH_CANCELED,
                EventType.MATCH_FINISHED,
                EventType.RANKING_UPDATED);
    }
}
