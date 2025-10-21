package com.cosek.edms.players;

import com.cosek.edms.position.Position;
import com.cosek.edms.position.PositionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/players")
@CrossOrigin(origins = "*")
public class PlayerController {

    @Autowired
    private PlayerService playerService;

    @Autowired
    private PositionRepository positionRepository;


    @PostMapping("/create")
    public Player createPlayer(@RequestBody PlayerRequest playerRequest) {
        return playerService.savePlayer(
                playerRequest.getPlayer(),
                playerRequest.getClubTeamId(),
                playerRequest.getNationalTeamId(),
                playerRequest.getPositionId() // ✅ pass it here
        );
    }


    @GetMapping
    public List<Player> getPlayers() {
        return playerService.getAllPlayers();
    }

    @DeleteMapping("/{id}")
    public void deletePlayer(@PathVariable Long id) {
        playerService.deletePlayer(id);
    }

    @PutMapping("/{id}")
    public Player updatePlayer(@PathVariable Long id, @RequestBody PlayerRequest request) {
        return playerService.updatePlayer(id, request.getPlayer(), request.getClubTeamId(), request.getNationalTeamId());
    }

    @GetMapping("/teams/{teamId}")
    public List<Player> getPlayersByTeam(@PathVariable Long teamId) {
        return playerService.findPlayersByTeamId(teamId);
    }

    @GetMapping("/positions")
    public List<Position> getAllPositions() {
        return positionRepository.findAll();
    }
    
}
