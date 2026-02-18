package com.project.figureout.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "CarrinhosProdutos")
@Getter @Setter
public class CartsProducts {

    @EmbeddedId
    private CartsProductsKey id;

    @ManyToOne
    @MapsId("cartId")
    @JoinColumn(name = "cpr_car_id")
    @JsonIgnore
    private Cart cart;

    @ManyToOne
    @MapsId("productId")
    @JoinColumn(name = "cpr_pro_id")
    private Product product;

    @Column(name = "cpr_quantidade_produto")
    private int productQuantity;

    @Column(name = "cpr_datahora_produto_adicionado")
    private LocalDateTime productAddedTime;

    @Column(name = "cpr_produto_unitario")
    private BigDecimal unitaryPrice;

    @Column(name = "cpr_produto_preco_final")
    private BigDecimal finalPrice;

    @Column(name = "cpr_quantidade_trocavel")
    private int exchangeableQuantity;

}
