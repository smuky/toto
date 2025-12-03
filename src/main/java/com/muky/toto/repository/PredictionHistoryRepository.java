package com.muky.toto.repository;

import com.muky.toto.repository_entities.PredictionHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PredictionHistoryRepository extends JpaRepository<PredictionHistory, Long> {

    List<PredictionHistory> findByEventDate(LocalDate eventDate);

    List<PredictionHistory> findByHomeTeamAndAwayTeam(String homeTeam, String awayTeam);

    List<PredictionHistory> findByEventDateBetween(LocalDate startDate, LocalDate endDate);
}
