package com.project.figureout.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "Estados")
@Getter @Setter
public class State {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "est_id")
    private long id;

    @Column(name = "est_nome", length = 25)
    private String name;

    @Column(name = "est_frete")
    private BigDecimal freight;

    public State(String name, BigDecimal freight) {
        this.setName(name);
        this.setFreight(freight);
    }

    public State() {}

}
