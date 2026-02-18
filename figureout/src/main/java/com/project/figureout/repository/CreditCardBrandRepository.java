package com.project.figureout.repository;

import com.project.figureout.model.CreditCardBrand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CreditCardBrandRepository extends JpaRepository<CreditCardBrand, Long> {

}
