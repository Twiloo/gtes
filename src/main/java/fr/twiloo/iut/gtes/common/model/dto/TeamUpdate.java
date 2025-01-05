package fr.twiloo.iut.gtes.common.model.dto;

import java.io.Serializable;

public record TeamUpdate(
        String teamName,
        String newName,
        String playerAName,
        String playerBName,
        String playerCName) implements Serializable {
}
