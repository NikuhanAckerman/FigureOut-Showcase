package com.project.figureout.dto;

import com.project.figureout.model.Cart;
import com.project.figureout.model.Product;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter @Setter
public class CartProductDTO {

    @NotNull
    private Cart cart;

    @NotNull(message = "O produto do carrinho não pode ser nulo.")
    private Product product;

    @NotNull(message = "A quantidade de produtos não pode ser nula.")
    @PositiveOrZero(message = "A quantidade de produtos disponíveis não pode ser menor que zero.")
    private int productQuantity;

    @PastOrPresent(message = "O produto só pode ter sido adicionado no presente ou passado.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime productAddedTime;

    @NotNull(message = "O preço a pagar não pode ser nulo.")
    @PositiveOrZero(message = "O preço a pagar não pode ser menor que zero.")
    private BigDecimal priceToPay;

}
