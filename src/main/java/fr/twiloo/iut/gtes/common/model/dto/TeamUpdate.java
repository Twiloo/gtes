package fr.twiloo.iut.gtes.common.model.dto;

import fr.twiloo.iut.gtes.common.model.Team;

public record TeamUpdate(
        String key,
        Team value) {
}
