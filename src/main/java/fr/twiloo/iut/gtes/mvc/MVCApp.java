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
import java.util.concurrent.atomic.AtomicReference;

public final class MVCApp implements EventDispatcher {
    private static final AtomicReference<MVCApp> instance = new AtomicReference<>();
    private final Client client;

    public static MVCApp getInstance() throws IOException {
        if (instance.get() == null) {  // First check (no synchronization)
            synchronized (MVCApp.class) {
                if (instance.get() == null) {  // Second check (with synchronization)
                    instance.set(new MVCApp());
                }
            }
        }
        return instance.get();
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
