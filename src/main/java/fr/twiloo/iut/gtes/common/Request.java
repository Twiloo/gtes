package fr.twiloo.iut.gtes.common;

public record Request<P>(
    ClientAction action,
    P payload) {
}
