package com.project.figureout.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ChangeCartProductQuantityDTO {

    @NotNull(message = "A quantidade alterada não pode ser nula.")
    @PositiveOrZero(message = "A quantidade alterada não pode ser menor que zero.")
    private Integer quantity;

}
