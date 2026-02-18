package com.project.figureout.dto;

import com.project.figureout.model.CartsProductsKey;
import com.project.figureout.model.ExchangeStatusEnum;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ExchangeShowOnPurchasesDTO {

    private String exchangeCode;

    private ExchangeStatusEnum status;

    private CartsProductsKey cartsProductsKey;

    private int quantityReturned;

}
