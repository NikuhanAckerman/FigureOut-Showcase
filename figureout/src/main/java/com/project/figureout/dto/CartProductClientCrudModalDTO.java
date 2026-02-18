package com.project.figureout.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter @Setter
public class CartProductClientCrudModalDTO {

    String productName;

    int productQuantity;

    BigDecimal unitaryPrice;

    BigDecimal finalPrice;

}
