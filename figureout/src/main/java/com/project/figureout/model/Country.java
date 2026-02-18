package com.project.figureout.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "Paises")
@Getter @Setter
public class Country {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pai_id")
    private long id;

    @Column(name = "pai_nome", length = 6)
    private String name;

    public Country(String name) {
        this.name = name;
    }

    public Country() {}

}
