package com.cosek.edms.matches;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MatchEventRepository extends JpaRepository<MatchEvent, Long> {
    List<MatchEvent> findByMatchId(Long matchId);
    List<MatchEvent> findByMatchIdAndType(Long matchId, String type);
    List<MatchEvent> findByMatchIdAndDescriptionContainingIgnoreCase(Long matchId, String keyword);
    void deleteByMatchId(Long matchId);

}
