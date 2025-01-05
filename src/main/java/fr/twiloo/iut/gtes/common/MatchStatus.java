package fr.twiloo.iut.gtes.common;

public enum MatchStatus {
    PENDING("Plannifié"),
    FINISHED("Terminé"),
    CANCELLED("Annulé");

    private final String text;

    MatchStatus(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
