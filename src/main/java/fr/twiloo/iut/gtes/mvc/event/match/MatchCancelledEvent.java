package fr.twiloo.iut.gtes.mvc.event.match;

import fr.twiloo.iut.gtes.mvc.event.Event;

public class MatchCancelledEvent extends Event<String, Void> {

    public MatchCancelledEvent(String payload) {
        super(payload);
    }

    @Override
    public String getName() {
        return "MatchCancelledEvent";
    }
}