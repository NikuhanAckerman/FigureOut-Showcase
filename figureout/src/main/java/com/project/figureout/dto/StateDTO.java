package com.project.figureout.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class StateDTO {

    @NotNull(message = "O id do estado não pode estar vazio.")
    private long id;

}
