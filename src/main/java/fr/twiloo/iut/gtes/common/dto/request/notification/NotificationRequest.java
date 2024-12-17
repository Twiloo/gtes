package fr.twiloo.iut.gtes.common.dto.request.notification;

import fr.twiloo.iut.gtes.common.Request;

public abstract class NotificationRequest<P> extends Request<P> {
    public NotificationRequest(P payload) {
        super(payload);
    }
}
