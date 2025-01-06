package fr.twiloo.iut.gtes.microservices;

import fr.twiloo.iut.gtes.common.EventType;
import fr.twiloo.iut.gtes.common.model.Event;
import fr.twiloo.iut.gtes.common.model.Team;
import fr.twiloo.iut.gtes.common.model.dto.TeamUpdated;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class CachedTeamsService extends Service {
    protected final List<String> teams = new ArrayList<>(); // Cache local pour les équipes

    public CachedTeamsService() throws IOException {
        super();
    }

    @Override
    public void dispatch(Event<?> event) {
        switch (event.type()) {
            case NEW_TEAM_CREATED -> addTeam((Team) event.payload());
            case TEAM_UPDATED -> updateTeam((TeamUpdated) event.payload());
            case TEAM_DELETED -> deleteTeam((Team) event.payload());
        }
    }

    @Override
    public List<EventType> supportedEventTypes() {
        return List.of(EventType.NEW_TEAM_CREATED,
                EventType.TEAM_UPDATED,
                EventType.TEAM_DELETED
        );
    }

    protected void addTeam(Team team) {
        synchronized (teams) {
            teams.add(team.getName());
        }
    }

    protected void updateTeam(TeamUpdated payload) {
        if (payload != null &&
                payload.newTeam() != null &&
                !payload.newTeam().getName().equals(payload.oldTeamName())) {
            synchronized (teams) {
                String oldTeamName = payload.oldTeamName();
                String newTeamName = payload.newTeam().getName();
                teams.add(newTeamName);
                updateTeamNameLinkedElements(oldTeamName, newTeamName);
                teams.remove(oldTeamName);
            }
        }
    }

    abstract protected void updateTeamNameLinkedElements(String oldTeamName, String newTeamName);

    protected void deleteTeam(Team payload) {
        if (payload != null && payload.getName() != null) {
            synchronized (teams) {
                deleteTeamNameLinkedElements(payload.getName());
                teams.remove(payload.getName());
            }
        }
    }

    abstract protected void deleteTeamNameLinkedElements(String teamName);

    @SuppressWarnings("BooleanMethodIsAlwaysInverted") // This method shouldn't add the negation !
    protected boolean teamExist(String name) {
        synchronized (teams) {
            return teams.contains(name);
        }
    }
}
