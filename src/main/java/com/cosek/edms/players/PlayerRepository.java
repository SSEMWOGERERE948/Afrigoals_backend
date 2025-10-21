package com.cosek.edms.players;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlayerRepository extends JpaRepository<Player, Long> {
    List<Player> findByClubTeamIdOrNationalTeamId(Long clubTeamId, Long nationalTeamId);

}
