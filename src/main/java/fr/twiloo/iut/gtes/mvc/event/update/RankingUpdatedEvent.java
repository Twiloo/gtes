package fr.twiloo.iut.gtes.mvc.event.update;

import fr.twiloo.iut.gtes.mvc.event.Event;

import java.util.Map;

public class RankingUpdatedEvent extends Event<Map<String, Integer>, Void> {
    public RankingUpdatedEvent(Map<String, Integer> updatedRankings) {
        super(updatedRankings);
    }

    @Override
    public String getName() {
        return "RankingUpdatedEvent";
    }
}
