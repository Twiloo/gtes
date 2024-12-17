package fr.twiloo.iut.gtes.mvc.event.team;

import fr.twiloo.iut.gtes.mvc.event.Event;

public class NewTeamCreatedEvent extends Event<String, Void> {

    public NewTeamCreatedEvent(String payload) {
        super(payload);
    }

    @Override
    public String getName() {
        return "NewTeamCreatedEvent";
    }
}
