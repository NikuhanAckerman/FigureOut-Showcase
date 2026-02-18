package com.project.figureout.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "TamanhoDeProduto")
@Getter @Setter
public class Size {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tam_id")
    private long id;

    @Column(name = "tam_nome")
    private String name;

    public Size(String name) {
        this.setName(name);
    }

    public Size() {}

}
