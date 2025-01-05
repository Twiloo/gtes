package fr.twiloo.iut.gtes.common.model.dto;

import fr.twiloo.iut.gtes.common.model.Team;

import java.io.*;

public final class TeamUpdated implements Serializable {
    private final String oldTeamName;
    private Team newTeam;

    public TeamUpdated( String oldTeamName, Team newTeam) {
        this.oldTeamName = oldTeamName;
        this.newTeam = newTeam;
    }

    public String oldTeamName() {
        return oldTeamName;
    }

    public Team newTeam() {
        return newTeam;
    }

    @Serial
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeUnshared(newTeam);
    }

    @Serial
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        newTeam = (Team) in.readUnshared();
    }
}
