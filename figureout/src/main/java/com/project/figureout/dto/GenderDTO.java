package com.project.figureout.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class GenderDTO {

    @NotNull(message = "O id do gênero não pode estar vazio.")
    private long id;

}
