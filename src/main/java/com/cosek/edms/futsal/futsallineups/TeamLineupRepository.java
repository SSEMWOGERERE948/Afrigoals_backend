package com.cosek.edms.futsal.futsallineups;

import com.cosek.edms.futsal.entity.TeamLineup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TeamLineupRepository extends JpaRepository<TeamLineup, Long> {
    Optional<TeamLineup> findByMatchIdAndSide(Long matchId, TeamLineup.Side side);
}
