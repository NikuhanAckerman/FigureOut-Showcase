package com.project.figureout.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Trocas")
@Getter @Setter
public class Exchange {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tro_id")
    private long id;

    @Column(name = "tro_codigo_de_troca")
    private String exchangeCode;

    @ManyToOne
    @JoinColumn(name = "tro_cli_id")
    @JsonIgnore
    private Client client;

    @ManyToOne
    @JoinColumn(name = "tro_ven_id")
    @JsonIgnore
    private Sale sale;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "tro_trp_id")
    private List<ExchangeProducts> returnedProducts = new ArrayList<>();

    @Column(name = "tro_quantia_final")
    private BigDecimal finalAmount;

    @Column(name = "tro_venda_atual")
    private boolean isCurrentExchange;

    @Column(name = "tro_data_hora_requisicao")
    private LocalDateTime exchangeRequestTime;

    @Column(name = "tro_data_hora_aceita")
    private LocalDateTime exchangeAcceptedTime;

    @Column(name = "tro_data_hora_finalizada")
    private LocalDateTime exchangeFinishTime;

    @Column(name = "tro_status")
    @Enumerated(EnumType.ORDINAL)
    private ExchangeStatusEnum status;

    @OneToOne
    @JoinColumn(name = "tro_cupom_troca")
    private ExchangeCoupon coupon;

}
