package fr.twiloo.iut.gtes.microservices;

import fr.twiloo.iut.gtes.common.Request;
import fr.twiloo.iut.gtes.common.ServiceConfig;
import fr.twiloo.iut.gtes.common.dto.request.team.TeamRequest;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public final class TeamService extends CallableService<TeamRequest> {

    private final Map<String, String> teams = new HashMap<>();

    public TeamService() throws IOException {
        super(ServiceConfig.TEAM.port);
    }

    @Override
    public Object run(Request<TeamRequest> request) throws Exception {
        return switch (request.action()) {
            case EQUIPE_MODIFIEE -> updateTeam(request.payload());
            case EQUIPE_SUPPRIMEE -> deleteTeam(request.payload());
            case NOUVELLE_EQUIPE_CREE -> createTeam(request.payload());
            default -> throw new Exception("Service is unable to process this action : " + request.action());
        };
    }

    private Object createTeam(Object payload) {
        // Payload est supposé être une Map avec les détails de l'équipe
        if (!(payload instanceof Map<?, ?>)) {
            return "Invalid payload format for team creation.";
        }

        Map<?, ?> teamData = (Map<?, ?>) payload;
        String teamId = (String) teamData.get("id");
        String teamName = (String) teamData.get("name");

        if (teamId == null || teamName == null) {
            return "Team ID or Name is missing.";
        }

        teams.put(teamId, teamName);

        // Publier un événement "NouvelleEquipeCree"
        System.out.println("Event Published: NouvelleEquipeCree for Team ID " + teamId);

        return "Team created successfully: " + teamName;
    }

    private Object updateTeam(Object payload) {
        // Payload est supposé être une Map avec les détails de l'équipe à mettre à jour
        if (!(payload instanceof Map<?, ?>)) {
            return "Invalid payload format for team update.";
        }

        Map<?, ?> teamData = (Map<?, ?>) payload;
        String teamId = (String) teamData.get("id");
        String newTeamName = (String) teamData.get("name");

        if (teamId == null || newTeamName == null) {
            return "Team ID or new Name is missing.";
        }

        if (!teams.containsKey(teamId)) {
            return "Team not found.";
        }

        teams.put(teamId, newTeamName);

        // Publier un événement "EquipeModifiee"
        System.out.println("Event Published: EquipeModifiee for Team ID " + teamId);

        return "Team updated successfully: " + newTeamName;
    }

    private Object deleteTeam(Object payload) {
        // Payload est supposé être l'ID de l'équipe à supprimer
        if (!(payload instanceof String)) {
            return "Invalid payload format for team deletion.";
        }

        String teamId = (String) payload;

        if (!teams.containsKey(teamId)) {
            return "Team not found.";
        }

        teams.remove(teamId);

        // Publier un événement "EquipeSupprimee"
        System.out.println("Event Published: EquipeSupprimee for Team ID " + teamId);

        return "Team deleted successfully.";
    }
}
