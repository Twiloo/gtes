package fr.twiloo.iut.gtes.mvc;

import fr.twiloo.iut.gtes.common.Config;
import fr.twiloo.iut.gtes.common.EventType;
import fr.twiloo.iut.gtes.common.client.Client;
import fr.twiloo.iut.gtes.common.client.EventDispatcher;
import fr.twiloo.iut.gtes.common.model.Event;
import fr.twiloo.iut.gtes.common.model.Match;
import fr.twiloo.iut.gtes.common.model.Team;
import fr.twiloo.iut.gtes.common.model.dto.TeamDeleted;
import fr.twiloo.iut.gtes.common.model.dto.TeamUpdated;
import fr.twiloo.iut.gtes.mvc.controller.DefaultController;
import fr.twiloo.iut.gtes.mvc.controller.MatchController;
import fr.twiloo.iut.gtes.mvc.controller.TeamController;

import java.io.IOException;
import java.util.List;

public final class MVCApp implements EventDispatcher {
    private static MVCApp instance;
    private final Client client;

    public static MVCApp getInstance() throws IOException {
        if (instance == null) {
            instance = new MVCApp();
        }
        return instance;
    }

    private MVCApp() throws IOException {
        client = new Client(Config.EVENT_BUS, this);
        DefaultController.startDefaultActionAsync();
    }

    public Client getClient() {
        return client;
    }

    @Override
    public void dispatch(Event<?> event) {
        switch (event.type()) {
            case SHOW_TEAMS_LIST -> TeamController.showTeamsListAction((List<?>)  event.payload());
            case NEW_TEAM_CREATED -> TeamController.showNewTeamCreatedAction((Team) event.payload());
            case TEAM_UPDATED -> TeamController.showTeamUpdatedAction((TeamUpdated) event.payload());
            case TEAM_DELETED -> TeamController.showTeamDeletedAction((TeamDeleted) event.payload());
            case NEW_MATCH_CREATED -> MatchController.showNewMatchCreatedAction((Match) event.payload());
            case MATCH_FINISHED -> MatchController.showMatchFinishedAction((Match) event.payload());
            case MATCH_CANCELED -> MatchController.showMatchCanceledAction((Match) event.payload());
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

    @Override
    public void close() {
        Runtime.getRuntime().exit(1);
    }
}
