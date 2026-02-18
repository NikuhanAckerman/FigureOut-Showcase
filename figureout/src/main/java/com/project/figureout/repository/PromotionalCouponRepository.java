package com.project.figureout.repository;

import com.project.figureout.model.PromotionalCoupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PromotionalCouponRepository extends JpaRepository<PromotionalCoupon, Long> {
}
