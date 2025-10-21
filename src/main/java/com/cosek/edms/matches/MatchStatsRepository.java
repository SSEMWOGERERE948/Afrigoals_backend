package com.cosek.edms.matches;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MatchStatsRepository extends JpaRepository<MatchStats, Long> {
    Optional<MatchStats> findByMatchId(Long matchId);
}
