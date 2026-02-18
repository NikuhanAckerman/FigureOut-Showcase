package com.project.figureout.repository;

import com.project.figureout.model.StockHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;

@Repository
public interface StockHistoryRepository extends JpaRepository<StockHistory, Long> {
}
