package fr.twiloo.iut.gtes.common;

import java.util.ArrayList;

public record Team(
        ArrayList<String> players,
        String name,
        String ranking) {
}
