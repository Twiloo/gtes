package fr.twiloo.iut.gtes.common;

public enum Config {
    EVENT_BUS_PORT(1234),
    EVENT_BUS_IP("127.0.0.1"),
    BASE_ELO(1200),
    ELO_DIFFERENCE_FACTOR(400.0);

    public final Object value;

    Config(Object value) {
        this.value = value;
    }
}
