package com.cosek.edms.league;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LeagueRepository extends JpaRepository<League, UUID> {
}
