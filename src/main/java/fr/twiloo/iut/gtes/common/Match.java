package fr.twiloo.iut.gtes.common;

public record Match(
       Team teamA,
       Team teamB,
       boolean isFinished,
       Integer scoreA,
       Integer scoreB) {
}
