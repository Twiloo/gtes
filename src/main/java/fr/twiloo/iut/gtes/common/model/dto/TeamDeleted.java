package fr.twiloo.iut.gtes.common.model.dto;

import java.io.Serializable;

public record TeamDeleted(String teamName, boolean success) implements Serializable {
}
