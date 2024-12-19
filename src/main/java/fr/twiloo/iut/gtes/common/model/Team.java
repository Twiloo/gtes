package fr.twiloo.iut.gtes.common.model;

import java.util.ArrayList;
import java.util.Objects;

public final class Team {
    private ArrayList<String> players;
    private String name;
    private Integer elo;
    private Integer ranking;
    private Boolean active;

    public Team(ArrayList<String> players, String name, Integer elo, Integer ranking, Boolean active) {
        this.players = players;
        this.name = name;
        this.elo = elo;
        this.ranking = ranking;
        this.active = active;
    }

    public ArrayList<String> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<String> players) {
        this.players = players;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getElo() {
        return elo;
    }

    public void setElo(Integer elo) {
        this.elo = elo;
    }

    public Integer getRanking() {
        return ranking;
    }

    public void setRanking(Integer ranking) {
        this.ranking = ranking;
    }

    public Boolean isActive() {
        return active;
    }

    public void deactivate() {
        this.active = false;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Team) obj;
        return Objects.equals(this.players, that.players) &&
                Objects.equals(this.name, that.name) &&
                Objects.equals(this.elo, that.elo) &&
                Objects.equals(this.ranking, that.ranking) &&
                Objects.equals(this.active, that.active);
    }

    @Override
    public int hashCode() {
        return Objects.hash(players, name, elo, ranking, active);
    }

    @Override
    public String toString() {
        return "Équipe " + name + " : " + players + "; Classement : " + ranking + "; Elo : " + elo + "; Actif : " + active;
    }

}
