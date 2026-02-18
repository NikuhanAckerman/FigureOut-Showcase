package com.project.figureout.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Embeddable
@Getter @Setter
public class SalesCardsKey implements Serializable {

    @Column(name = "ven_id")
    private long saleId;

    @Column(name = "cre_id")
    private long creditCardId;

}
