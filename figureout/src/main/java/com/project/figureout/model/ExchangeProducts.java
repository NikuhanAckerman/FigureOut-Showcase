package com.project.figureout.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "TrocasProdutosDevolvidos")
@Getter @Setter
public class ExchangeProducts {

    @EmbeddedId
    private ExchangeProductsKey id;

    @MapsId("exchangeId") // Map the exchange ID part of the composite key
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "trp_tro_id")
    @JsonIgnore
    private Exchange exchange;

     //Fix: Use @JoinColumns to map the composite key from CartsProducts
    @MapsId("cartsProductsKey") // Map the cart-product part of the composite key
    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "trp_cpr_car_id", referencedColumnName = "cpr_car_id"),
            @JoinColumn(name = "trp_cpr_pro_id", referencedColumnName = "cpr_pro_id")
    })
    private CartsProducts cartProduct;

    @Column(name = "trp_valor_final")
    private BigDecimal finalAmount;

    @Column(name = "trp_quantidade")
    private int quantityReturned;

}
