package fr.twiloo.iut.gtes.common;

public enum Config {
    EVENT_BUS(1234);

    public final int port;

    private Config(int port) {
        this.port = port;
    }
}
