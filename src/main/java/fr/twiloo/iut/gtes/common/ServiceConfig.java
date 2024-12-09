package fr.twiloo.iut.gtes.common;

import fr.twiloo.iut.gtes.microservices.TeamService;

public enum ServiceConfig {
    TEAM(12345, TeamService.class)

    ;
    public final int port;
    public final Class<?> serviceClass;

    ServiceConfig(int port, Class<?> serviceClass) {
        this.port = port;
        this.serviceClass = serviceClass;
    }
}
