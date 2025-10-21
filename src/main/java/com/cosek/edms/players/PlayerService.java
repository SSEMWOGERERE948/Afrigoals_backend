package com.cosek.edms.players;

import com.cosek.edms.position.Position;
import com.cosek.edms.position.PositionRepository;
import com.cosek.edms.teams.Team;
import com.cosek.edms.teams.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlayerService {

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private PositionRepository positionRepository;


    public Player savePlayer(Player player, Long clubTeamId, Long nationalTeamId, Long positionId) {
        if (clubTeamId != null) {
            Team clubTeam = teamRepository.findById(clubTeamId)
                    .orElseThrow(() -> new RuntimeException("Club team not found"));
            player.setClubTeam(clubTeam);
        }

        if (nationalTeamId != null) {
            Team nationalTeam = teamRepository.findById(nationalTeamId)
                    .orElseThrow(() -> new RuntimeException("National team not found"));
            player.setNationalTeam(nationalTeam);
        }

        if (positionId != null) {
            Position position = positionRepository.findById(positionId)
                    .orElseThrow(() -> new RuntimeException("Position not found"));
            player.setPosition(position);
        }

        return playerRepository.save(player);
    }


    public List<Player> getAllPlayers() {
        return playerRepository.findAll();
    }

    public void deletePlayer(Long id) {
        playerRepository.deleteById(id);
    }


    public Player updatePlayer(Long id, Player updatedPlayer, Long clubTeamId, Long nationalTeamId) {
        Player existingPlayer = playerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Player not found"));

        existingPlayer.setName(updatedPlayer.getName());
        existingPlayer.setNumber(updatedPlayer.getNumber());
        existingPlayer.setPosition(updatedPlayer.getPosition());
        existingPlayer.setNationality(updatedPlayer.getNationality());
        existingPlayer.setAge(updatedPlayer.getAge());
        existingPlayer.setImage(updatedPlayer.getImage());
        existingPlayer.setClubStats(updatedPlayer.getClubStats());
        existingPlayer.setNationalStats(updatedPlayer.getNationalStats());

        if (clubTeamId != null) {
            Team clubTeam = teamRepository.findById(clubTeamId)
                    .orElseThrow(() -> new RuntimeException("Club team not found"));
            existingPlayer.setClubTeam(clubTeam);
        } else {
            existingPlayer.setClubTeam(null);
        }

        if (nationalTeamId != null) {
            Team nationalTeam = teamRepository.findById(nationalTeamId)
                    .orElseThrow(() -> new RuntimeException("National team not found"));
            existingPlayer.setNationalTeam(nationalTeam);
        } else {
            existingPlayer.setNationalTeam(null);
        }

        return playerRepository.save(existingPlayer);
    }

    public List<Player> findPlayersByTeamId(Long teamId) {
        return playerRepository.findByClubTeamIdOrNationalTeamId(teamId, teamId);
    }
}
