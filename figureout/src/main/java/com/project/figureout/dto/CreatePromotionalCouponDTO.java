package com.project.figureout.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter @Setter
public class CreatePromotionalCouponDTO {

    private String couponName;

    private BigDecimal couponDiscount;

    private LocalDate couponExpirationDate;

}
