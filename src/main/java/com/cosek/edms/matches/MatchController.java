package com.cosek.edms.matches;

import com.cosek.edms.matches.dto.MatchStateDTO;
import com.cosek.edms.matches.dto.StatIncrementRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/matches")
@CrossOrigin(origins = "*")
public class MatchController {

    @Autowired
    private MatchService matchService;

    @Autowired
    private GoalRepository goalRepository;

    @Autowired
    private MatchEventRepository matchEventRepository;

    @PostMapping
    public Match create(@RequestBody Match match) {
        match.setStatus("Scheduled");
        match.setCurrentMinute(0);
        return matchService.saveMatch(match);
    }

    @GetMapping
    public List<MatchResponse> getAllMatches() {
        return matchService.getAllMatchResponses();
    }

    @GetMapping("/{id}")
    public Match get(@PathVariable Long id) {
        return matchService.getMatch(id);
    }

    @PutMapping("/{id}")
    public Match updateMatch(@PathVariable Long id, @RequestBody MatchUpdateRequest request) {
        return matchService.updateLineups(id, request);
    }

    @GetMapping("/lineups/{id}")
    public MatchResponse getMatchWithLineups(@PathVariable Long id) {
        return matchService.getMatchResponse(id);
    }

    @PutMapping("/{id}/update-lineups")
    public Match updateLineups(@PathVariable Long id, @RequestBody MatchUpdateRequest request) {
        return matchService.updateLineups(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        matchService.deleteMatch(id);
    }

    // ===== GOALS =====
    @GetMapping("/{matchId}/goals")
    public ResponseEntity<List<Goal>> getGoals(@PathVariable Long matchId) {
        return ResponseEntity.ok(matchService.getGoalsByMatchId(matchId));
    }

    @PostMapping("/{matchId}/goals")
    public ResponseEntity<Match> addGoal(@PathVariable Long matchId, @RequestBody Goal goal) {
        Match updatedMatch = matchService.addGoal(matchId, goal);
        return ResponseEntity.ok(updatedMatch);
    }

    @PutMapping("/{matchId}/goals/{goalId}")
    public ResponseEntity<Match> updateGoal(@PathVariable Long matchId, @PathVariable Long goalId, @RequestBody Goal updated) {
        Match match = matchService.updateGoal(matchId, goalId, updated);
        return ResponseEntity.ok(match);
    }

    @DeleteMapping("/{matchId}/goals/{goalId}")
    public ResponseEntity<Match> deleteGoal(@PathVariable Long matchId, @PathVariable Long goalId) {
        Match match = matchService.deleteGoal(matchId, goalId);
        return ResponseEntity.ok(match);
    }

    // ===== EVENTS =====
    @PostMapping("/{matchId}/events")
    public ResponseEntity<MatchEvent> addEvent(@PathVariable Long matchId, @RequestBody MatchEvent event) {
        return ResponseEntity.ok(matchService.addMatchEvent(matchId, event));
    }

    @GetMapping("/{matchId}/events")
    public ResponseEntity<List<MatchEvent>> getEvents(@PathVariable Long matchId,
                                                      @RequestParam(value = "type", required = false) String type,
                                                      @RequestParam(value = "search", required = false) String search) {
        return ResponseEntity.ok(matchService.getMatchEvents(matchId, type, search));
    }

    @GetMapping("/{matchId}/events/{eventId}")
    public ResponseEntity<MatchEvent> getEvent(@PathVariable Long matchId, @PathVariable Long eventId) {
        return ResponseEntity.ok(matchEventRepository.findById(eventId).orElseThrow());
    }

    @PutMapping("/{matchId}/events/{eventId}")
    public ResponseEntity<MatchEvent> updateEvent(@PathVariable Long matchId, @PathVariable Long eventId, @RequestBody MatchEvent updated) {
        return ResponseEntity.ok(matchService.updateMatchEvent(matchId, eventId, updated));
    }

    @DeleteMapping("/{matchId}/events/{eventId}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long matchId, @PathVariable Long eventId) {
        matchEventRepository.deleteById(eventId);
        return ResponseEntity.noContent().build();
    }

    // ===== STATE =====
    @GetMapping("/{matchId}/state")
    public ResponseEntity<MatchStateDTO> getMatchState(@PathVariable Long matchId) {
        return ResponseEntity.ok(matchService.getMatchState(matchId));
    }

    @PutMapping("/{matchId}/state")
    public ResponseEntity<Match> updateState(@PathVariable Long matchId, @RequestBody MatchStateDTO dto) {
        // Only allow limited manual updates (pause/resume, added time adjustments)
        return ResponseEntity.ok(matchService.updateMatchState(matchId, dto));
    }

    @DeleteMapping("/{matchId}/state")
    public ResponseEntity<Match> resetState(@PathVariable Long matchId) {
        return ResponseEntity.ok(matchService.resetMatchState(matchId));
    }

    // ===== STATS =====
    @GetMapping("/{matchId}/stats")
    public ResponseEntity<MatchStats> getStats(@PathVariable Long matchId) {
        return ResponseEntity.ok(matchService.getStats(matchId));
    }

    @PutMapping("/{matchId}/stats")
    public ResponseEntity<MatchStats> updateStats(@PathVariable Long matchId, @RequestBody MatchStats stats) {
        return ResponseEntity.ok(matchService.updateStats(matchId, stats));
    }

    @PostMapping("/{matchId}/stats")
    public ResponseEntity<MatchStats> incrementStat(@PathVariable Long matchId, @RequestBody StatIncrementRequest request) {
        return ResponseEntity.ok(matchService.incrementStat(matchId, request));
    }

    // ===== LIVE STATE (Real-time with automatic timing) =====
    @GetMapping("/{matchId}/live-state")
    public ResponseEntity<MatchStateDTO> getLiveState(@PathVariable Long matchId) {
        // This returns current state based on scheduled time, not manual timer
        return ResponseEntity.ok(matchService.getMatchState(matchId));
    }

    // REMOVED ENDPOINTS (no longer needed with automatic timing):
    // - POST /{matchId}/adjust-time (matches follow scheduled time)
    // - PUT /{matchId}/timer (deprecated - timing is automatic)

    // Optional: Keep for manual time adjustments in exceptional cases
    @PostMapping("/{matchId}/pause")
    public ResponseEntity<Match> pauseMatch(@PathVariable Long matchId) {
        MatchStateDTO dto = new MatchStateDTO();
        dto.setRunning(false);
        Match match = matchService.updateMatchState(matchId, dto);
        return ResponseEntity.ok(match);
    }

    @PostMapping("/{matchId}/resume")
    public ResponseEntity<Match> resumeMatch(@PathVariable Long matchId) {
        MatchStateDTO dto = new MatchStateDTO();
        dto.setRunning(true);
        Match match = matchService.updateMatchState(matchId, dto);
        return ResponseEntity.ok(match);
    }
}