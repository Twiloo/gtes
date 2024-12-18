package fr.twiloo.iut.gtes.common;

import fr.twiloo.iut.gtes.microservices.MatchService;
import fr.twiloo.iut.gtes.microservices.NotificationService;
import fr.twiloo.iut.gtes.microservices.TeamService;

public enum ServiceConfig {
    TEAM(10001, null,  TeamService.class),
    MATCH(10002, null, MatchService.class),
    NOTIFICATION(10003, 50001, NotificationService.class);
    private final int requestPort;
    private final Integer subscriptionPort;
    private final Class<?> serviceClass;

    ServiceConfig(int port, Integer subscribingPort, Class<?> serviceClass) {
        this.requestPort = port;
        this.subscriptionPort = subscribingPort;
        this.serviceClass = serviceClass;
    }

    public int getRequestPort() {
        return requestPort;
    }

    public Integer getSubscriptionPort() {
        return subscriptionPort;
    }

    public Class<?> getServiceClass() {
        return serviceClass;
    }

    public String getAddress() {
        return "127.0.0.1";
    }
}
