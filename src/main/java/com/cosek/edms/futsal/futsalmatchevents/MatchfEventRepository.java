package com.cosek.edms.futsal.futsalmatchevents;

import com.cosek.edms.futsal.entity.MatchfEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchfEventRepository extends JpaRepository<MatchfEvent, Long> {
    List<MatchfEvent> findByMatchId(Long matchId);
    void deleteByMatchId(Long matchId);
}