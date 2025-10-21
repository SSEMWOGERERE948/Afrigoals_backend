package com.cosek.edms.futsal.futsalmatches;

import com.cosek.edms.futsal.dto.FutsalMatchDTO;
import com.cosek.edms.futsal.dto.GoalDTO;
import com.cosek.edms.futsal.entity.*;
import com.cosek.edms.futsal.futsalmatches.FutsalMatchService;
import com.cosek.edms.futsal.util.PeriodStateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/futsalmatches")
@CrossOrigin(origins = "*")
public class FutsalMatchController {

    @Autowired
    private FutsalMatchService matchService;

    // Get all matches
    @GetMapping("/")
    public List<FutsalMatchDTO> getAll() {
        return matchService.getAll().stream().map(this::mapToDto).toList();
    }

    private FutsalMatchDTO mapToDto(FutsalMatch match) {
        FutsalMatchDTO dto = new FutsalMatchDTO();
        dto.setId(match.getId());
        dto.setHomeTeam(match.getHomeTeam());
        dto.setAwayTeam(match.getAwayTeam());
        dto.setVenue(match.getVenue());
        dto.setReferee(match.getReferee());
        dto.setLeague(match.getLeague());
        dto.setDate(match.getDate());
        dto.setTime(match.getTime());
        dto.setTeamSize(match.getTeamSize());
        dto.setMatchDuration(match.getMatchDuration());
        dto.setMaxSubstitutions(match.getMaxSubstitutions());
        dto.setStatus(match.getStatus());
        dto.setHomeScore(match.getHomeScore());
        dto.setAwayScore(match.getAwayScore());
        dto.setStartTime(match.getStartTime());

        // Include computed or stored periodState
        if ("Live".equalsIgnoreCase(match.getStatus())) {
            PeriodState currentState = matchService.computePeriodState(match);
            dto.setPeriodState(currentState);
        } else if (match.getPeriodStateJson() != null) {
            PeriodState restoredState = PeriodStateUtils.fromJson(match.getPeriodStateJson());
            dto.setPeriodState(restoredState);
        }

        // Map goals
        List<GoalDTO> goalDTOs = match.getGoals().stream().map(goal -> {
            GoalDTO g = new GoalDTO();
            g.setId(goal.getId());
            g.setPlayerId(goal.getPlayerId());
            g.setPlayerName(goal.getPlayerName());
            g.setAssistPlayerId(goal.getAssistPlayerId());
            g.setAssistPlayerName(goal.getAssistPlayerName());
            g.setTeam(goal.getTeam());
            g.setMinute(goal.getMinute());
            g.setType(goal.getType());
            return g;
        }).toList();

        dto.setGoals(goalDTOs);

        // Optional: include periods if needed
        dto.setPeriods(match.getPeriods());

        return dto;
    }




    // Get a specific match
    @GetMapping("/{id}")
    public ResponseEntity<FutsalMatch> getMatch(@PathVariable Long id) {
        return ResponseEntity.ok(matchService.getById(id));
    }

    // Create a new match
    @PostMapping("/create")
//    public ResponseEntity<FutsalMatch> createMatch(@RequestBody FutsalMatch match) {
//        return ResponseEntity.ok(matchService.create(match));
//    }

    // Update a match
    @PutMapping("/{id}")
    public ResponseEntity<FutsalMatch> updateMatch(@PathVariable Long id, @RequestBody FutsalMatch match) {
        return ResponseEntity.ok(matchService.update(id, match));
    }

    // Delete a match
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMatch(@PathVariable Long id) {
        matchService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // Add a goal to a match
    @PostMapping("/{matchId}/goals")
    public ResponseEntity<Goals> addGoal(@PathVariable Long matchId, @RequestBody Goals goal) {
        return ResponseEntity.ok(matchService.addGoalToMatch(matchId, goal));
    }

    // Add an event to a match
    @PostMapping("/{matchId}/futsal-events")
    public ResponseEntity<MatchfEvent> addEvent(@PathVariable Long matchId, @RequestBody MatchfEvent event) {
        return ResponseEntity.ok(matchService.addEventToMatch(matchId, event));
    }

    // Update match score
    @PutMapping("/{matchId}/score")
    public ResponseEntity<FutsalMatch> updateScore(@PathVariable Long matchId, @RequestBody Map<String, Integer> scoreUpdate) {
        Integer homeScore = scoreUpdate.get("homeScore");
        Integer awayScore = scoreUpdate.get("awayScore");
        return ResponseEntity.ok(matchService.updateMatchScore(matchId, homeScore, awayScore));
    }

    // Get match details with goals and events
    @GetMapping("/{matchId}/details")
    public ResponseEntity<FutsalMatch> getMatchDetails(@PathVariable Long matchId) {
        return ResponseEntity.ok(matchService.getMatchDetails(matchId));
    }

    // Get all goals for a match
    @GetMapping("/{matchId}/goals")
    public ResponseEntity<List<Goals>> getMatchGoals(@PathVariable Long matchId) {
        return ResponseEntity.ok(matchService.getMatchGoals(matchId));
    }

    // Get all events for a match
    @GetMapping("/{matchId}/events")
    public ResponseEntity<List<MatchfEvent>> getMatchEvents(@PathVariable Long matchId) {
        return ResponseEntity.ok(matchService.getMatchEvents(matchId));
    }

    // Update match status
    @PutMapping("/{matchId}/status")
    public ResponseEntity<FutsalMatch> updateStatus(@PathVariable Long matchId, @RequestBody Map<String, String> statusUpdate) {
        String status = statusUpdate.get("status");
        return ResponseEntity.ok(matchService.updateMatchStatus(matchId, status));
    }

    // Start a match
    @PostMapping("/{matchId}/start")
    public ResponseEntity<FutsalMatch> startMatch(@PathVariable Long matchId) {
        return ResponseEntity.ok(matchService.startMatch(matchId));
    }

    // End a match
    @PostMapping("/{matchId}/end")
    public ResponseEntity<FutsalMatch> endMatch(@PathVariable Long matchId) {
        return ResponseEntity.ok(matchService.endMatch(matchId));
    }

    // Reset a match
    @PostMapping("/{matchId}/reset")
    public ResponseEntity<FutsalMatch> resetMatch(@PathVariable Long matchId) {
        return ResponseEntity.ok(matchService.resetMatch(matchId));
    }

    @PutMapping("/{id}/periods")
    public ResponseEntity<FutsalMatch> updateMatchPeriods(
            @PathVariable Long id,
            @RequestBody List<MatchPeriod> periods) {
        try {
            FutsalMatch updated = matchService.updateMatchPeriods(id, periods);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}

