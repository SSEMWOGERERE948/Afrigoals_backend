package com.cosek.edms.soccer.fixtures;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ApiFixturesRepository extends JpaRepository<FixtureEntity, Long> {
    List<FixtureEntity> findAllByLeagueIdAndSeason(int leagueId, int season);
    void deleteAllByLeagueIdAndSeason(int leagueId, int season);
}
