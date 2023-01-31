package org.telbot.telran.info.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.telbot.telran.info.model.Statistic;

import java.util.Optional;

@Repository
public interface StatisticRepository extends JpaRepository<Statistic, Integer> {

    Optional<Statistic> findTopByOrderByLastEventIdDesc();
    Optional<Statistic> findTopByOrderByIdDesc();
}
