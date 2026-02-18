package com.project.figureout.repository;

import com.project.figureout.model.ExchangeProducts;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExchangeProductsRepository extends JpaRepository<ExchangeProducts, Long> {
}
