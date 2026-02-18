package com.project.figureout.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "VendasCartoesDeCredito")
@Getter @Setter
public class SalesCards {

    @EmbeddedId
    private SalesCardsKey id;

    @ManyToOne
    @MapsId("saleId")
    @JoinColumn(name = "vdc_ven_id")
    @JsonIgnore
    private Sale sale;

    @ManyToOne
    @MapsId("creditCardId")
    @JoinColumn(name = "vdc_cre_id")
    private CreditCard creditCard;

    @Column(name = "vdc_valor_pago")
    private BigDecimal amountPaid;

}
