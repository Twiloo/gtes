package fr.twiloo.iut.gtes.common.dto.request.notification;

import fr.twiloo.iut.gtes.common.Team;

public final class NotifyNewTeamRequest extends NotificationRequest<Team> {
    public NotifyNewTeamRequest(Team payload) {
        super(payload);
    }
}
