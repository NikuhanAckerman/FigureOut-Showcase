package com.project.figureout.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CreditCardBrandDTO {

    @NotNull(message = "O id da bandeira do cartão de crédito não pode estar vazio.")
    private long id;

}
