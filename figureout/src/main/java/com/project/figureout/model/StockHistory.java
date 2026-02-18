package com.project.figureout.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "HistoricoEstoque")
@Getter @Setter
public class StockHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hes_id")
    private long id;

    @ManyToOne
    @JoinColumn(name = "hes_est_id")
    private Stock stock;

    @Column(name = "hes_quantidade")
    private int productQuantityAvailable;

    @Column(name = "hes_quantidade_anterior")
    private int productQuantityAvailablePreviously;

    @ManyToMany
    @JoinTable(
            name = "EstoqueHistoricoFornecedores",
            joinColumns = @JoinColumn(name = "hes_id"),
            inverseJoinColumns = @JoinColumn(name = "for_id")
    )
    private List<Supplier> supplier;

    @Column(name = "hes_datahora_mudanca_estoque")
    private LocalDate dateChangeOfStockQuantity;

}
