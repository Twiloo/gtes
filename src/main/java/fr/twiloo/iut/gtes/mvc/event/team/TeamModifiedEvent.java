package fr.twiloo.iut.gtes.mvc.event.team;

import fr.twiloo.iut.gtes.mvc.event.Event;

public class TeamModifiedEvent extends Event<String, Void> {

    public TeamModifiedEvent(String payload) {
        super(payload);
    }

    @Override
    public String getName() {
        return "TeamModifiedEvent";
    }
}
