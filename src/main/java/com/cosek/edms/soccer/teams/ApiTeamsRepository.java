package com.cosek.edms.soccer.teams;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApiTeamsRepository extends JpaRepository<TeamEntity, Integer> {

    List<TeamEntity> findAllByLeagueIdAndSeason(int leagueId, int season);
}
