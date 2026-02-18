package com.project.figureout.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "ProdutosAtivacao")
@Getter @Setter
public class ProductsActivation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pat_id")
    private long id;

    @ManyToOne
    @JoinColumn(name = "pat_pro_id")
    @JsonIgnore
    private Product product;

    @Column(name = "pat_ativado_ou_inativado")
    private boolean isActive;

    @Column(name = "pat_razao")
    private String reason;

    @Column(name = "pat_data")
    private LocalDateTime dateTime;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "pat_categoria")
    private ProductActivationEnum category;

}
