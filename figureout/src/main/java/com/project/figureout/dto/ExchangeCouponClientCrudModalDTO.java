package com.project.figureout.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter @Setter
public class ExchangeCouponClientCrudModalDTO {

    private String exchangeCouponCode;

    private BigDecimal amountWorth;

}
