package com.project.figureout.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "CuponsDeTroca")
@Getter @Setter
public class ExchangeCoupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cdt_id")
    private long id;

    @Column(name = "cdt_codigo")
    private String exchangeCouponCode;

    @ManyToOne
    @JoinColumn(name = "cdt_cli_id")
    @JsonIgnore
    private Client client;

    @ManyToOne
    @JoinColumn(name = "cdt_ven_id")
    @JsonIgnore
    private Sale sale;

    @Column(name = "cdt_quantia")
    private BigDecimal amountWorth;

    @Column(name = "cdt_usado")
    private boolean used;

}
