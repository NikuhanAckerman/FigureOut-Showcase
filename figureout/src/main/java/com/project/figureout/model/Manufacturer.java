package com.project.figureout.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "Fabricantes")
@Getter @Setter
public class Manufacturer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fab_id")
    private long id;

    @Column(name = "fab_nome")
    private String name;

    public Manufacturer(String name) {
        this.setName(name);
    }

    public Manufacturer() {}

}
