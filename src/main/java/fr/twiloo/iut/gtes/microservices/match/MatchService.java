package fr.twiloo.iut.gtes.microservices.match;

import fr.twiloo.iut.gtes.common.EventType;
import fr.twiloo.iut.gtes.common.MatchStatus;
import fr.twiloo.iut.gtes.common.model.Event;
import fr.twiloo.iut.gtes.common.model.Match;
import fr.twiloo.iut.gtes.microservices.CachedTeamsService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class MatchService extends CachedTeamsService {
    private final List<Match> matches = new ArrayList<>();

    public MatchService() throws IOException {
        super();
    }

    @Override
    public void dispatch(Event<?> event) {
        super.dispatch(event);
        switch (event.type()) {
            case CREATE_MATCH -> createMatch((Match) event.payload());
            case SET_MATCH_RESULTS -> finishMatch((Match) event.payload());
            case CANCEL_MATCH -> cancelMatch((Match) event.payload());
        }
    }

    @Override
    public List<EventType> supportedEventTypes() {
        List<EventType> eventTypes = new ArrayList<>(super.supportedEventTypes());
        eventTypes.addAll(List.of(
                EventType.CREATE_MATCH,
                EventType.CANCEL_MATCH,
                EventType.SET_MATCH_RESULTS
        ));
        return eventTypes;
    }

    @Override
    protected void updateTeamNameLinkedElements(String oldTeamName, String newTeamName) {
        synchronized (matches) {
            for (Match match : matches) {
                if (match.getTeamAName().equals(oldTeamName)) {
                    match.setTeamAName(newTeamName);
                } else if (match.getTeamBName().equals(oldTeamName)) {
                    match.setTeamBName(newTeamName);
                }
            }
        }
    }

    @Override
    protected void deleteTeamNameLinkedElements(String teamName) {
        synchronized (matches) {
            for (Match match : matches) {
                if (match.getStatus() == MatchStatus.PENDING &&
                        (match.getTeamAName().equals(teamName)) ||
                        (match.getTeamBName().equals(teamName))) {
                    match.setStatus(MatchStatus.CANCELLED);
                }
            }
        }
    }

    private void createMatch(Match payload) {
        if (cannotIdentifyTeams(payload) || findPendingMatch(payload) != null) {
            sendEvent(new Event<>(EventType.NEW_MATCH_CREATED, null), true);
            return;
        }

        // Match validé et organisé
        Match match = new Match(payload.getTeamAName(), payload.getTeamBName(), MatchStatus.PENDING, 0, 0);
        synchronized (matches) {
            matches.add(match);
        }
        sendEvent(new Event<>(EventType.NEW_MATCH_CREATED, match), true);
    }

    private void finishMatch(Match payload) {
        if (cannotIdentifyTeams(payload) || findPendingMatch(payload) == null) {
            sendEvent(new Event<>(EventType.MATCH_FINISHED, null), true);
            return;
        }

        Match match = findPendingMatch(payload);
        synchronized (matches) {
            Objects.requireNonNull(match).setStatus(MatchStatus.FINISHED);
            if (match.getTeamAName().equals(payload.getTeamAName())) { // Put scores the way the user sent it (event if the match was created with inverted team names)
                match.setScoreA(payload.getScoreA());
                match.setScoreB(payload.getScoreB());
            } else {
                match.setScoreA(payload.getScoreB());
                match.setScoreB(payload.getScoreA());
            }
        }

        // Envoyer un événement signalant que le match est terminé
        sendEvent(new Event<>(EventType.MATCH_FINISHED, match), true);
    }

    private void cancelMatch(Match payload) {
        if (cannotIdentifyTeams(payload) || findPendingMatch(payload) == null) {
            sendEvent(new Event<>(EventType.MATCH_CANCELED, null), true);
            return;
        }

        Match match = findPendingMatch(payload);
        synchronized (matches) {
            Objects.requireNonNull(match).setStatus(MatchStatus.CANCELLED);
        }
        sendEvent(new Event<>(EventType.MATCH_CANCELED, match), true);
    }

    private boolean cannotIdentifyTeams(Match match) {
        return match == null ||
                match.getTeamAName() == null ||
                match.getTeamBName() == null ||
                !teamExist(match.getTeamAName()) ||
                !teamExist(match.getTeamBName());
    }

    private Match findPendingMatch(Match payload) {
        for (Match match : matches.stream().filter(match -> match.getStatus() == MatchStatus.PENDING).toList()) {
            if (match.getTeamAName().equals(payload.getTeamAName())
            && match.getTeamBName().equals(payload.getTeamBName())) {
                return match;
            } else if (match.getTeamAName().equals(payload.getTeamBName())
                    && match.getTeamBName().equals(payload.getTeamAName())) {
                return match;
            }
        }
        return null;
    }
}
