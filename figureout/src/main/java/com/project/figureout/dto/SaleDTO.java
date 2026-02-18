package com.project.figureout.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class SaleDTO {

    @NotNull(message = "O(s) id(s) de cartões de crédito usado(s) não deve(m) ser nulo(s).")
    private List<Long> salesCardsIds;

    @NotNull(message = "O id do carrinho de compra não pode ser nulo.")
    private long saleCartId;

    @NotNull(message = "O id do endereço de entrega não deve ser nulo.")
    private long deliveryAddressId;

}
