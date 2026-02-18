package com.project.figureout.repository;

import com.project.figureout.model.Exchange;
import com.project.figureout.model.Sale;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ExchangeRepository extends JpaRepository<Exchange, Long> {
    List<Sale> findByExchangeRequestTime(LocalDateTime date);
}
