package fr.twiloo.iut.gtes.mvc.controller;

import fr.twiloo.iut.gtes.common.ServiceConfig;
import fr.twiloo.iut.gtes.common.client.ClientManager;
import fr.twiloo.iut.gtes.common.model.dto.request.team.ListTeamsRequest;

import java.io.IOException;

import static java.lang.System.out;

public final class TeamController {

    public static void listTeamsAction() {
        try {
            out.println(ClientManager.getInstance().getClient(ServiceConfig.TEAM).getClientSend().sendRequest(new ListTeamsRequest()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void addTeamAction() {

    }
}
