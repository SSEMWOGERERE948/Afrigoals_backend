package com.cosek.edms.futsal.futsallineups;

import com.cosek.edms.futsal.entity.LineupPlayer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LineupPlayerRepository extends JpaRepository<LineupPlayer, Long> {
    void deleteByLineupId(Long lineupId);
}
