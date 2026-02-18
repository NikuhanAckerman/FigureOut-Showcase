package com.project.figureout.repository;

import com.project.figureout.model.PricingGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.lang.annotation.Retention;

@Repository
public interface PricingGroupRepository extends JpaRepository<PricingGroup, Long> {

}
