package fr.twiloo.iut.gtes.common;

import fr.twiloo.iut.gtes.common.dto.request.match.CancelMatchRequest;
import fr.twiloo.iut.gtes.common.dto.request.match.CreateMatchRequest;
import fr.twiloo.iut.gtes.common.dto.request.match.EndMatchRequest;
import fr.twiloo.iut.gtes.common.dto.request.team.CreateTeamRequest;
import fr.twiloo.iut.gtes.common.dto.request.team.DeleteTeamRequest;
import fr.twiloo.iut.gtes.common.dto.request.team.ListTeamsRequest;
import fr.twiloo.iut.gtes.common.dto.request.team.UpdateTeamRequest;
import fr.twiloo.iut.gtes.common.dto.response.match.CreateMatchResponse;

public enum Action {
    LIST_TEAMS(ListTeamsRequest.class, null),
    CREATE_TEAM(CreateTeamRequest.class, null),
    UPDATE_TEAM(UpdateTeamRequest.class, null),
    DELETE_TEAM(DeleteTeamRequest.class, null),
    CREATE_MATCH(CreateMatchRequest.class, CreateMatchResponse.class),
    END_MATCH(EndMatchRequest.class, null),
    CANCEL_MATCH(CancelMatchRequest.class, null),
    NOTIFY_NEW_TEAM(null, null),
    NOTIFY_MATCH_RESULTS(null, null),;

    private final Class<? extends Request<?>> requestClass;
    private final Class<? extends Response<?>> responseClass;

    Action(Class<? extends Request<?>> requestClass, Class<? extends Response<?>> responseClass) {
        this.requestClass = requestClass;
        this.responseClass = responseClass;
    }

    public static Action actionOf(Request<?> request) {
        for (Action action : values()) {
            if (action.requestClass.isAssignableFrom(request.getClass())) {
                return action;
            }
        }
        return null;
    }

    public Class<? extends Response<?>> getResponseClass() {
        return responseClass;
    }
}
