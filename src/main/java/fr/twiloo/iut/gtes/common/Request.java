package fr.twiloo.iut.gtes.common;

public record Request(
    Action action,
    Object payload) {
}
