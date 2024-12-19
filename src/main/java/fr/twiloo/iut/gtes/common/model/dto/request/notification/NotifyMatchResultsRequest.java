package fr.twiloo.iut.gtes.common.model.dto.request.notification;

import fr.twiloo.iut.gtes.common.model.Team;

public final class NotifyMatchResultsRequest extends NotificationRequest<NotifyMatchResultsRequest.Payload> {
    public NotifyMatchResultsRequest(Payload payload) {
        super(payload);
    }

    public record Payload(
            Team teamA,
            Team teamB,
            int scoreA,
            int scoreB) {
    }
}
