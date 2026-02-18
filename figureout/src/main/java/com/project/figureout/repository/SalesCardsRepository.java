package com.project.figureout.repository;

import com.project.figureout.model.SalesCards;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SalesCardsRepository extends JpaRepository<SalesCards, Long> {

    public List<SalesCards> findSalesCardsBySaleId(long saleId);

}
