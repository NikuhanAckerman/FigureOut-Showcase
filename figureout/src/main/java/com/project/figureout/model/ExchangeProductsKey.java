package com.project.figureout.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Embeddable
@Getter @Setter
public class ExchangeProductsKey implements Serializable {

    @Column(name = "trp_tro_id")
    private long exchangeId;

    @Column(name = "trp_crp_id")
    private CartsProductsKey cartsProductsKey;

    public ExchangeProductsKey(long exchangeId, long productId, long cartId) {
        this.setExchangeId(exchangeId);
        this.setCartsProductsKey(new CartsProductsKey(cartId, productId));
    }

    public ExchangeProductsKey() {}

}
