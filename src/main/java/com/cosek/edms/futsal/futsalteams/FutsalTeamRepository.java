package com.cosek.edms.futsal.futsalteams;

import com.cosek.edms.futsal.entity.FutsalTeam;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FutsalTeamRepository extends JpaRepository<FutsalTeam, Long> {}

