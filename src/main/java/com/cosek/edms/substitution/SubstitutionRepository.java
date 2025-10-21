package com.cosek.edms.substitution;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubstitutionRepository extends JpaRepository<Substitution, Long> {
    List<Substitution> findByMatchId(Long matchId);
}

