package fr.twiloo.iut.gtes.microservices.notification;

import fr.twiloo.iut.gtes.common.Config;
import fr.twiloo.iut.gtes.common.EventType;
import fr.twiloo.iut.gtes.common.model.Event;
import fr.twiloo.iut.gtes.common.model.Match;
import fr.twiloo.iut.gtes.microservices.Service;

import java.io.IOException;
import java.util.List;

public final class NotificationService extends Service {

    public NotificationService() throws IOException {
        super();
    }

    @Override
    protected Config getConfig() {
        return Config.EVENT_BUS;
    }

    @Override
    public void dispatch(Event<?> event) {
        switch (event.type()) {
            case NEW_MATCH_CREATED -> sendMatchCreatedNotification((Match) event.payload());
            case MATCH_FINISHED -> sendMatchFinishedNotification((Match) event.payload());
            case RANKING_UPDATED -> sendRankingUpdatedNotification();
        }
    }

    @Override
    public List<EventType> supportedEventTypes() {
        return List.of(
                EventType.NEW_MATCH_CREATED,
                EventType.MATCH_FINISHED,
                EventType.RANKING_UPDATED
        );
    }

    private void sendMatchCreatedNotification(Match match) {
        if (match != null) {
            System.out.println("Notification: Nouveau match créé entre "
                    + match.getTeamAName() + " et " + match.getTeamBName() + ".");
        }
    }

    private void sendMatchFinishedNotification(Match match) {
        if (match != null) {
            System.out.println("Notification: Match terminé ! Résultat - "
                    + match.getTeamAName() + ": " + match.getScoreA() + " vs "
                    + match.getTeamBName() + ": " + match.getScoreB());
        }
    }

    private void sendRankingUpdatedNotification() {
        System.out.println("Notification: Le classement des équipes a été mis à jour !");
    }
}
