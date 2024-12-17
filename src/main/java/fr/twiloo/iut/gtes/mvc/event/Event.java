package fr.twiloo.iut.gtes.mvc.event;

public abstract class Event<P, R> {
    P payload;
    R response;
}
