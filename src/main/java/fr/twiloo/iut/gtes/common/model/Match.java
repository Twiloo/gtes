package fr.twiloo.iut.gtes.common.model;

public record Match(
       String teamAName,
       String teamBName,
       boolean isFinished,
       Integer scoreA,
       Integer scoreB) {
}
