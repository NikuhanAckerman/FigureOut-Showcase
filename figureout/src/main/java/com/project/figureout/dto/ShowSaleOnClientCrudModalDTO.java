package com.project.figureout.dto;

import com.project.figureout.model.SaleStatusEnum;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class ShowSaleOnClientCrudModalDTO {

    private long saleId;

    private String saleCode;

    private SaleStatusEnum saleStatusEnum;

    private BigDecimal saleFinalPrice;

    private String promotionalCouponApplied;

    private String deliveryAddressNickname;

    private BigDecimal freight;

    private LocalDateTime dateTimeSale;

    private List<SalesCardsClientCrudModalDTO> salesCardsList = new ArrayList<>();

    private List<CartProductClientCrudModalDTO> cartProductList = new ArrayList<>();

    private List<ExchangeCouponClientCrudModalDTO> exchangeCouponList = new ArrayList<>();

}
