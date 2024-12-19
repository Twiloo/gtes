package fr.twiloo.iut.gtes.common.model.dto.request.notification;

import fr.twiloo.iut.gtes.common.model.Team;

public final class NotifyNewTeamRequest extends NotificationRequest<Team> {
    public NotifyNewTeamRequest(Team payload) {
        super(payload);
    }
}
