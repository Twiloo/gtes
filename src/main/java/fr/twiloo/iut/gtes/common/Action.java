package fr.twiloo.iut.gtes.common;

import fr.twiloo.iut.gtes.common.model.dto.Request;
import fr.twiloo.iut.gtes.common.model.dto.request.match.CancelMatchRequest;
import fr.twiloo.iut.gtes.common.model.dto.request.match.CreateMatchRequest;
import fr.twiloo.iut.gtes.common.model.dto.request.match.EndMatchRequest;
import fr.twiloo.iut.gtes.common.model.dto.request.team.CreateTeamRequest;
import fr.twiloo.iut.gtes.common.model.dto.request.team.DeleteTeamRequest;
import fr.twiloo.iut.gtes.common.model.dto.request.team.ListTeamsRequest;
import fr.twiloo.iut.gtes.common.model.dto.request.team.UpdateTeamRequest;

public enum Action {
    LIST_TEAMS(ListTeamsRequest.class),
    CREATE_TEAM(CreateTeamRequest.class),
    UPDATE_TEAM(UpdateTeamRequest.class),
    DELETE_TEAM(DeleteTeamRequest.class),
    CREATE_MATCH(CreateMatchRequest.class),
    END_MATCH(EndMatchRequest.class),
    CANCEL_MATCH(CancelMatchRequest.class),
    NOTIFY_NEW_TEAM(null),
    NOTIFY_MATCH_RESULTS(null),;

    private final Class<? extends Request<?>> requestClass;

    Action(Class<? extends Request<?>> requestClass) {
        this.requestClass = requestClass;
    }

    public static Action actionOf(Request<?> request) {
        for (Action action : values()) {
            if (action.requestClass.isAssignableFrom(request.getClass())) {
                return action;
            }
        }
        return null;
    }
}
