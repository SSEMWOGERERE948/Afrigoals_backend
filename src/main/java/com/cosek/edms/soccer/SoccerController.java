package com.cosek.edms.soccer;

import com.cosek.edms.soccer.fixtures.FixtureEntity;
import com.cosek.edms.soccer.player.PlayerEntity;
import com.cosek.edms.soccer.teams.TeamEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/soccer")
public class SoccerController {

    @Autowired
    private SoccerApiService soccerApiService;

    @GetMapping("/east-africa/leagues")
    public ResponseEntity<List<Map<String, Object>>> getEastAfricanLeagues() {
        return ResponseEntity.ok(soccerApiService.getEastAfricanLeagues());
    }

    @GetMapping("/teams/{leagueId}/{season}")
    public ResponseEntity<List<TeamEntity>> getTeams(
            @PathVariable int leagueId,
            @PathVariable int season) {
        return ResponseEntity.ok(soccerApiService.getTeamsCached(leagueId, season));
    }



    @GetMapping("/players/{teamId}/{season}")
    public ResponseEntity<String> getPlayers(@PathVariable int teamId, @PathVariable int season) {
        return ResponseEntity.ok(soccerApiService.getPlayersByTeam(teamId, season));
    }

    @GetMapping("/coach/{teamId}")
    public ResponseEntity<String> getCoach(@PathVariable int teamId) {
        return ResponseEntity.ok(soccerApiService.getCoachByTeam(teamId));
    }

    @GetMapping("/standings/{leagueId}/{season}")
    public ResponseEntity<String> getStandings(@PathVariable int leagueId, @PathVariable int season) {
        return ResponseEntity.ok(soccerApiService.getStandingsByLeague(leagueId, season));
    }

    // Get all fixtures for a league and season
    @GetMapping("/fixtures/{leagueId}/{season}")
    public ResponseEntity<String> getFixtures(@PathVariable int leagueId, @PathVariable int season) {
        return ResponseEntity.ok(soccerApiService.getFixtures(leagueId, season));
    }

    // Get top scorers
    @GetMapping("/topscorers/{leagueId}/{season}")
    public ResponseEntity<String> getTopScorers(@PathVariable int leagueId, @PathVariable int season) {
        return ResponseEntity.ok(soccerApiService.getTopScorers(leagueId, season));
    }

    // Get all venues
    @GetMapping("/venues")
    public ResponseEntity<String> getVenues() {
        return ResponseEntity.ok(soccerApiService.getVenues());
    }

    @GetMapping("/{teamId}/{season}")
    public ResponseEntity<List<PlayerEntity>> getPlayersByTeam(
            @PathVariable int teamId,
            @PathVariable int season,
            @RequestParam(defaultValue = "false") boolean forceRefresh) {
        List<PlayerEntity> players = soccerApiService.getPlayersByTeam(teamId, season, forceRefresh);
        return ResponseEntity.ok(players);
    }

    @GetMapping("/fixture/{leagueId}/{season}")
    public ResponseEntity<List<FixtureEntity>> getFixtures(
            @PathVariable int leagueId,
            @PathVariable int season,
            @RequestParam(defaultValue = "false") boolean forceRefresh) {

        List<FixtureEntity> fixtures = soccerApiService.getFixturesCached(leagueId, season, forceRefresh);
        return ResponseEntity.ok(fixtures);
    }

}
