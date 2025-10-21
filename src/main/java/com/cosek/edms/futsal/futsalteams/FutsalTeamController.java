package com.cosek.edms.futsal.futsalteams;

import com.cosek.edms.futsal.dto.FutsalPlayerDTO;
import com.cosek.edms.futsal.dto.FutsalTeamDTO;
import com.cosek.edms.futsal.entity.FutsalTeam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/futsalteams")
@CrossOrigin
public class FutsalTeamController {
    @Autowired
    private FutsalTeamService teamService;

    @GetMapping("/")
    public List<FutsalTeamDTO> getAll() {
        return teamService.getAll().stream()
                .map(this::mapToDto)
                .toList();
    }

    private FutsalTeamDTO mapToDto(FutsalTeam team) {
        FutsalTeamDTO dto = new FutsalTeamDTO();
        dto.setId(team.getId());
        dto.setTeamName(team.getTeamName());
        dto.setInstitution(team.getInstitution());
        dto.setCategory(team.getCategory());
        dto.setPreferredTeamSize(team.getPreferredTeamSize());
        dto.setHomeVenue(team.getHomeVenue());
        dto.setFounded(team.getFounded());
        dto.setManager(team.getManager());
        dto.setLogo(team.getLogo());
        dto.setDescription(team.getDescription());
        dto.setContactEmail(team.getContactEmail());
        dto.setContactPhone(team.getContactPhone());
        dto.setStats(team.getStats());

        // ✅ Map players
        dto.setPlayers(team.getPlayers().stream().map(player -> {
            FutsalPlayerDTO pdto = new FutsalPlayerDTO();
            pdto.setId(player.getId());
            pdto.setName(player.getName());
            pdto.setNumber(player.getNumber());
            pdto.setNationality(player.getNationality());
            pdto.setAge(player.getAge());
            return pdto;
        }).toList());

        return dto;
    }

    @GetMapping("/{id}") public FutsalTeam getById(@PathVariable Long id) { return teamService.getById(id); }

    @PostMapping("/create")
    public FutsalTeam create(@RequestBody FutsalTeam team) { return teamService.create(team); }

    @PutMapping("/{id}") public FutsalTeam update(@PathVariable Long id, @RequestBody FutsalTeam team) {
        return teamService.update(id, team);
    }

    @DeleteMapping("/{id}") public void delete(@PathVariable Long id) { teamService.delete(id); }
}
