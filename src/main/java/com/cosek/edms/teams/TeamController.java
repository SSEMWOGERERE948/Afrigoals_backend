package com.cosek.edms.teams;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teams")
@CrossOrigin(origins = "*") // For development
public class TeamController {

    @Autowired
    private TeamService teamService;

    @GetMapping
    public List<Team> getAllTeams() {
        return teamService.getAllTeams();
    }

    @GetMapping("/{id}")
    public Team getTeam(@PathVariable Long id) {
        return teamService.getTeam(id);
    }

    @PostMapping("/create")
    public Team createTeam(@RequestBody Team team) {
        validateTeamType(team.getTeamType());
        return teamService.saveTeam(team);
    }

    @PutMapping("/{id}")
    public Team updateTeam(@PathVariable Long id, @RequestBody Team teamDetails) {
        validateTeamType(teamDetails.getTeamType());
        Team team = teamService.getTeam(id);

        team.setName(teamDetails.getName());
        team.setLeague(teamDetails.getLeague());
        team.setManager(teamDetails.getManager());
        team.setStadium(teamDetails.getStadium());
        team.setStats(teamDetails.getStats());
        team.setFounded(teamDetails.getFounded());
        team.setLogo(teamDetails.getLogo());
        team.setTeamType(teamDetails.getTeamType());

        return teamService.saveTeam(team);
    }

    @DeleteMapping("/{id}")
    public void deleteTeam(@PathVariable Long id) {
        teamService.deleteTeam(id);
    }

    private void validateTeamType(String type) {
        if (type == null || (!type.equalsIgnoreCase("club") && !type.equalsIgnoreCase("national"))) {
            throw new IllegalArgumentException("Invalid team type. Must be 'club' or 'national'.");
        }
    }

    @GetMapping(params = "type")
    public List<Team> getTeamsByType(@RequestParam String type) {
        return teamService.getTeamsByType(type);
    }

}



