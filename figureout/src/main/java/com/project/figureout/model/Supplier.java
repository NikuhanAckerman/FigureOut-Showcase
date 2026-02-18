package com.project.figureout.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "Fornecedores")
@Getter @Setter
public class Supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "for_id")
    private long id;

    @Column(name = "for_nome")
    private String name;

    public Supplier(String name) {
        this.setName(name);
    }

    public Supplier() {}

}
