package com.cosek.edms.futsal.futsalmatches;

import com.cosek.edms.futsal.entity.FutsalMatch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FutsalMatchRepository extends JpaRepository<FutsalMatch, Long> {}

