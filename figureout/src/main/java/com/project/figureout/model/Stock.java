package com.project.figureout.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "Estoque")
@Getter @Setter
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "est_id")
    private long id;

    @OneToOne
    @JoinColumn(name = "est_pro_id")
    @JsonIgnore
    private Product product;

    @Column(name = "est_quantidade")
    private Integer productQuantityAvailable;

    @Column(name = "est_valor_custo")
    private BigDecimal productPurchaseAmount;

    @ManyToMany
    @JoinTable(
            name = "EstoqueFornecedores",
            joinColumns = @JoinColumn(name = "est_id"),
            inverseJoinColumns = @JoinColumn(name = "for_id")
    )
    private List<Supplier> supplier;

    @Column(name = "est_datahora_entrada_inicial")
    private LocalDate initialEntryDate;

    @Column(name = "est_datahora_entrada_ultimo_produto")
    private LocalDate latestEntryDate;

    @Column(name = "est_datahora_saida_ultimo_produto")
    private LocalDate latestDropDate;

}
