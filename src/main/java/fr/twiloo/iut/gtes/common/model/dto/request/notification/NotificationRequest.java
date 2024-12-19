package fr.twiloo.iut.gtes.common.model.dto.request.notification;

import fr.twiloo.iut.gtes.common.model.dto.Request;

public abstract class NotificationRequest<P> extends Request<P> {
    public NotificationRequest(P payload) {
        super(payload);
    }
}
