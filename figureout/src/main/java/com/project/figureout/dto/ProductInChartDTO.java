package com.project.figureout.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter @Setter
public class ProductInChartDTO {

    private String name;

    private BigDecimal valuePurchased;

    private Integer volumePurchased;

    private LocalDateTime datePurchased;

}
