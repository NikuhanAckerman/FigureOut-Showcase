package com.project.figureout.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "CuponsPromocionais")
@Getter @Setter
public class PromotionalCoupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cup_id")
    private long id;

    @Column(name = "cup_nome")
    private String couponName;

    @Column(name = "cup_desconto")
    private BigDecimal couponDiscount;

    @Column(name = "cup_data_expiracao")
    private LocalDate couponExpirationDate;

    public PromotionalCoupon(String couponName, BigDecimal couponDiscount, LocalDate couponExpirationDate) {
        this.setCouponName(couponName);
        this.setCouponDiscount(couponDiscount);
        this.setCouponExpirationDate(couponExpirationDate);
    }

    public PromotionalCoupon() {}

}
