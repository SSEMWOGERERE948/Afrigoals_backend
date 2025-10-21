package com.cosek.edms.soccer.player;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ApiPlayersRepository extends JpaRepository<PlayerEntity, Long> {
    List<PlayerEntity> findAllByTeamIdAndSeason(int teamId, int season);
    void deleteAllByTeamIdAndSeason(int teamId, int season);
}
