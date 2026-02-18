package com.project.figureout.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Embeddable
@Getter @Setter
public class CartsProductsKey implements Serializable {

    @Column(name = "cpr_car_id")
    private long cartId;

    @Column(name = "cpr_pro_id")
    private long productId;

    public CartsProductsKey(long cartId, long productId) {
        this.setCartId(cartId);
        this.setProductId(productId);
    }

    public CartsProductsKey() {}

}
