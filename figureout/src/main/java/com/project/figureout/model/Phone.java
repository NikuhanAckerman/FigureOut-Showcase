package com.project.figureout.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "Telefones")
@Getter @Setter
public class Phone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tel_id", nullable = false, unique = true)
    private long id;

    @Column(name = "tel_celular", nullable = false)
    private boolean cellphone;

    @Column(name = "tel_ddd", nullable = false, length = 2)
    private String ddd;

    @Column(name = "tel_numero", nullable = false, length = 15)
    private String phoneNumber;

}
