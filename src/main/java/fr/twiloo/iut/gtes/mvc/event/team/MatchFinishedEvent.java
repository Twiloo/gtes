package fr.twiloo.iut.gtes.mvc.event.team;

import fr.twiloo.iut.gtes.mvc.event.Event;

public class MatchFinishedEvent extends Event<String, Void> {

    public MatchFinishedEvent(String payload) {
        super(payload);
    }

    @Override
    public String getName() {
        return "MatchFinishedEvent";
    }
}
