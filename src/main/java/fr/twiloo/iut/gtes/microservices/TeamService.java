package fr.twiloo.iut.gtes.microservices;

import fr.twiloo.iut.gtes.common.Request;

import java.io.IOException;

public class TeamService extends CallableService {

    public TeamService() throws IOException {
        super(1234);
    }

    @Override
    Object run(Request request) throws Exception {
        return switch (request.action()) {
            case EQUIPE_MODIFIEE -> updateTeam(request.payload());
            case EQUIPE_SUPPRIMEE -> deleteTeam(request.payload());
            case NOUVELLE_EQUIPE_CREE -> createTeam(request.payload());
            default -> throw new Exception("Service is unable to process this action : " + request.action());
        };
    }

    private Object updateTeam(Object payload) {
        return null;
    }

    private Object deleteTeam(Object payload) {
        return null;
    }

    private Object createTeam(Object payload) {
        return null;
    }
}
