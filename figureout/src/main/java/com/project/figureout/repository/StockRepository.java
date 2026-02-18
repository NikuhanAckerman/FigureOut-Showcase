package com.project.figureout.repository;

import com.project.figureout.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {

    public ArrayList<Stock> findAllByProductId(long productId);

}
