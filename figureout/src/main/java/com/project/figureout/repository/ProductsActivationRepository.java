package com.project.figureout.repository;

import com.project.figureout.model.ProductsActivation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductsActivationRepository extends JpaRepository<ProductsActivation, Long> {
}
