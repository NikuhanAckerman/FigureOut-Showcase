package com.project.figureout.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CountryDTO {

    @NotNull(message = "O id do país não pode estar vazio.")
    private long id;

}
