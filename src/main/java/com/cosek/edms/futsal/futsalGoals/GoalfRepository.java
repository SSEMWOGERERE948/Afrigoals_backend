package com.cosek.edms.futsal.futsalGoals;

import com.cosek.edms.futsal.entity.Goals;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GoalfRepository extends JpaRepository<Goals, Long> {
    List<Goals> findByMatchId(Long matchId);
    void deleteByMatchId(Long matchId);
}