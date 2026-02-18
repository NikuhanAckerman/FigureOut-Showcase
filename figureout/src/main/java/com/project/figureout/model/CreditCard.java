package com.project.figureout.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "Cartoes")
@Getter @Setter
public class CreditCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cre_id", nullable = false, unique = true)
    private long id;

    @Column(name = "cre_preferido", nullable = false)
    private boolean preferential;

    @Column(name = "cre_apelido", nullable = false)
    private String nickname;

    @Column(name = "cre_numero", nullable = false, length = 20)
    private String cardNumber;

    @Column(name = "cre_nome_impresso", nullable = false, length = 30)
    private String printedName;

    @ManyToOne
    @JoinColumn(name = "cre_ban_id")
    private CreditCardBrand brand;

    @Column(name = "cre_cod_seguranca", nullable = false, length = 4)
    private String securityCode;

    @ManyToOne
    @JoinColumn(name = "cre_cli_id")
    @JsonIgnore // prevenir loop infinito
    private Client client;

    @Column(name = "cre_saldo")
    private BigDecimal balance;

}
