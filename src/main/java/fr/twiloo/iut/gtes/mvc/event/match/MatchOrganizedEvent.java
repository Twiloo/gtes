package fr.twiloo.iut.gtes.mvc.event.match;

import fr.twiloo.iut.gtes.mvc.event.Event;

public class MatchOrganizedEvent extends Event<String, Void> {

    public MatchOrganizedEvent(String payload) {
        super(payload);
    }

    @Override
    public String getName() {
        return "MatchOrganizedEvent";
    }
}
