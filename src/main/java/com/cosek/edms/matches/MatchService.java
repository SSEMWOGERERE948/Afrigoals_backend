package com.cosek.edms.matches;

import com.cosek.edms.matches.dto.MatchStateDTO;
import com.cosek.edms.matches.dto.StatIncrementRequest;
import com.cosek.edms.players.Player;
import com.cosek.edms.players.PlayerRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class MatchService {

    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private GoalRepository goalRepository;

    @Autowired
    private MatchEventRepository matchEventRepository;

    @Autowired
    private MatchStatsRepository matchStatsRepository;

    public Match saveMatch(Match match) {
        return matchRepository.save(match);
    }

    public List<MatchResponse> getAllMatchResponses() {
        List<Match> matches = matchRepository.findAll();
        List<MatchResponse> responses = new ArrayList<>();

        for (Match match : matches) {
            updateMatchStateBasedOnScheduledTime(match);

            MatchResponse response = new MatchResponse();
            BeanUtils.copyProperties(match, response);
            response.setHomeLineup(resolveLineup(match.getHomeLineup()));
            response.setAwayLineup(resolveLineup(match.getAwayLineup()));
            responses.add(response);
        }

        return responses;
    }

    public Match getMatch(Long id) {
        Match match = matchRepository.findById(id).orElseThrow(() -> new RuntimeException("Match not found"));
        updateMatchStateBasedOnScheduledTime(match);
        return match;
    }

    public void deleteMatch(Long id) {
        matchRepository.deleteById(id);
    }

    public MatchResponse getMatchResponse(Long id) {
        Match match = matchRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Match not found"));

        updateMatchStateBasedOnScheduledTime(match);

        MatchResponse response = new MatchResponse();
        BeanUtils.copyProperties(match, response);
        response.setHomeLineup(resolveLineup(match.getHomeLineup()));
        response.setAwayLineup(resolveLineup(match.getAwayLineup()));

        return response;
    }

    /**
     * Updates match state based on scheduled time rather than manual start
     */
    private void updateMatchStateBasedOnScheduledTime(Match match) {
        if (match.getDate() == null || match.getTime() == null) {
            return; // Can't determine time without scheduled date/time
        }

        // Convert scheduled date/time to system timezone
        LocalDateTime scheduledDateTime = LocalDateTime.of(match.getDate(), match.getTime());
        ZonedDateTime scheduledZoned = scheduledDateTime.atZone(ZoneId.systemDefault());
        Date scheduledTime = Date.from(scheduledZoned.toInstant());

        Date now = new Date();
        long currentMillis = now.getTime();
        long scheduledMillis = scheduledTime.getTime();

        // If current time hasn't reached scheduled time, keep as scheduled
        if (currentMillis < scheduledMillis) {
            if (!"Scheduled".equals(match.getStatus())) {
                match.setStatus("Scheduled");
                match.setPeriod("Pre-Match");
                match.setRunning(false);
                match.setCurrentMinute(0);
                match.setElapsedSeconds(0);
                matchRepository.save(match);
            }
            return;
        }

        // Calculate elapsed time since scheduled start
        long elapsedMillis = currentMillis - scheduledMillis;

        // Account for pauses if any
        long totalPausedTime = match.getTotalPausedMillis() != null ? match.getTotalPausedMillis() : 0L;
        if (Boolean.FALSE.equals(match.getRunning()) && match.getLastPauseTime() != null) {
            totalPausedTime += (currentMillis - match.getLastPauseTime().getTime());
        }

        long actualPlayingMillis = elapsedMillis - totalPausedTime;
        int playingSeconds = Math.max(0, (int) (actualPlayingMillis / 1000L));
        int playingMinutes = playingSeconds / 60;

        // Set the actual start time to scheduled time (not manual start)
        if (match.getStartTime() == null) {
            match.setStartTime(scheduledTime);
        }

        // Update elapsed seconds (transient field for frontend)
        match.setElapsedSeconds(playingSeconds);
        match.setCurrentMinute(playingMinutes);

        // Determine match state based on elapsed playing time
        String previousStatus = match.getStatus();
        String previousPeriod = match.getPeriod();
        Boolean previousRunning = match.getRunning();

        if (playingMinutes < 45) {
            // First half
            match.setStatus("Live");
            match.setPeriod("First Half");
            match.setRunning(true);
        } else if (playingMinutes < 45 + getAddedTimeMinutes(match, "firstHalf")) {
            // First half with added time
            match.setStatus("Live");
            match.setPeriod("First Half");
            match.setRunning(true);
        } else if (playingMinutes < 60) { // 15 minute break
            // Half time
            match.setStatus("Live");
            match.setPeriod("Half Time");
            match.setRunning(false);
            if (match.getHalfTimeStart() == null) {
                match.setHalfTimeStart(new Date(scheduledMillis + 45 * 60 * 1000L));
            }
        } else if (playingMinutes < 90) {
            // Second half
            match.setStatus("Live");
            match.setPeriod("Second Half");
            match.setRunning(true);
            if (match.getSecondHalfStart() == null) {
                match.setSecondHalfStart(new Date(scheduledMillis + 60 * 60 * 1000L));
            }
        } else if (playingMinutes < 90 + getAddedTimeMinutes(match, "secondHalf")) {
            // Second half with added time
            match.setStatus("Live");
            match.setPeriod("Second Half");
            match.setRunning(true);
        } else {
            // Match finished
            match.setStatus("Finished");
            match.setPeriod("Full Time");
            match.setRunning(false);
        }

        // Save if state changed
        if (!match.getStatus().equals(previousStatus) ||
                !match.getPeriod().equals(previousPeriod) ||
                !match.getRunning().equals(previousRunning)) {

            // Create automatic events for state transitions
            createAutomaticEvents(match, previousPeriod, match.getPeriod(), playingSeconds);

            matchRepository.save(match);
        }
    }

    private void createAutomaticEvents(Match match, String fromPeriod, String toPeriod, int elapsedSeconds) {
        try {
            if ("Pre-Match".equals(fromPeriod) && "First Half".equals(toPeriod)) {
                MatchEvent kickOff = new MatchEvent();
                kickOff.setMatch(match);
                kickOff.setType("kick_off");
                kickOff.setMinute(elapsedSeconds);
                kickOff.setDescription("Match started - Kick-off");
                matchEventRepository.save(kickOff);
            } else if ("First Half".equals(fromPeriod) && "Half Time".equals(toPeriod)) {
                MatchEvent halfTime = new MatchEvent();
                halfTime.setMatch(match);
                halfTime.setType("half_time");
                halfTime.setMinute(elapsedSeconds);
                halfTime.setDescription("Half Time");
                matchEventRepository.save(halfTime);
            } else if ("Half Time".equals(fromPeriod) && "Second Half".equals(toPeriod)) {
                MatchEvent secondHalf = new MatchEvent();
                secondHalf.setMatch(match);
                secondHalf.setType("second_half_start");
                secondHalf.setMinute(elapsedSeconds);
                secondHalf.setDescription("Second Half started");
                matchEventRepository.save(secondHalf);
            } else if ("Second Half".equals(fromPeriod) && "Full Time".equals(toPeriod)) {
                MatchEvent fullTime = new MatchEvent();
                fullTime.setMatch(match);
                fullTime.setType("full_time");
                fullTime.setMinute(elapsedSeconds);
                fullTime.setDescription("Full Time - Match ended");
                matchEventRepository.save(fullTime);
            }
        } catch (Exception e) {
            // Log error but don't fail the state update
            System.err.println("Failed to create automatic event: " + e.getMessage());
        }
    }

    private int getAddedTimeMinutes(Match match, String period) {
        if (match.getAddedTime() == null) return 0;

        switch (period) {
            case "firstHalf": return match.getAddedTime().getFirstHalf();
            case "secondHalf": return match.getAddedTime().getSecondHalf();
            default: return 0;
        }
    }

    public MatchStateDTO getMatchState(Long matchId) {
        Match match = getMatch(matchId); // This will update the state

        MatchStateDTO dto = new MatchStateDTO();
        dto.setStatus(match.getStatus());
        dto.setElapsedSeconds(match.getElapsedSeconds() != null ? match.getElapsedSeconds() : 0);
        dto.setCurrentMinute(match.getCurrentMinute() != null ? match.getCurrentMinute() : 0);

        // Format time as MM:SS
        int totalSeconds = dto.getElapsedSeconds();
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        dto.setFormattedTime(String.format("%02d:%02d", minutes, seconds));

        dto.setPeriod(match.getPeriod());
        dto.setRunning(match.getRunning());
        dto.setAddedTime(match.getAddedTime());
        dto.setStartTime(match.getStartTime());
        dto.setHalfTimeStart(match.getHalfTimeStart());
        dto.setSecondHalfStart(match.getSecondHalfStart());
        dto.setHomeScore(match.getHomeScore());
        dto.setAwayScore(match.getAwayScore());

        return dto;
    }

    public Match updateMatchState(Long matchId, MatchStateDTO dto) {
        Match match = getMatch(matchId);

        // Only allow manual updates for specific scenarios
        // For pausing/resuming during live matches
        if ("Live".equals(match.getStatus())) {
            if (Boolean.FALSE.equals(dto.getRunning()) && Boolean.TRUE.equals(match.getRunning())) {
                // Manual pause
                match.setLastPauseTime(new Date());
                match.setRunning(false);
            } else if (Boolean.TRUE.equals(dto.getRunning()) && Boolean.FALSE.equals(match.getRunning())) {
                // Manual resume
                if (match.getLastPauseTime() != null) {
                    long pauseDuration = System.currentTimeMillis() - match.getLastPauseTime().getTime();
                    Long totalPaused = match.getTotalPausedMillis() != null ? match.getTotalPausedMillis() : 0L;
                    match.setTotalPausedMillis(totalPaused + pauseDuration);
                    match.setLastPauseTime(null);
                }
                match.setRunning(true);
            }
        }

        // Allow manual added time updates
        if (dto.getAddedTime() != null) {
            match.setAddedTime(dto.getAddedTime());
        }

        return matchRepository.save(match);
    }

    // Remove the old manual start methods - matches start automatically based on scheduled time
    // Keep other existing methods unchanged...

    private TeamLineupResponse resolveLineup(TeamLineup lineup) {
        if (lineup == null) return null;

        TeamLineupResponse response = new TeamLineupResponse();
        response.setFormation(lineup.getFormation());
        response.setCoach(lineup.getCoach());

        List<Player> startingXI = playerRepository.findAllById(lineup.getStartingXIIds());
        List<Player> substitutes = playerRepository.findAllById(lineup.getSubstituteIds());

        response.setStartingXI(startingXI);
        response.setSubstitutes(substitutes);

        return response;
    }

    public Match updateLineups(Long matchId, MatchUpdateRequest request) {
        Match match = getMatch(matchId);

        match.setStatus(request.getStatus());

        TeamLineup homeLineup = new TeamLineup();
        homeLineup.setFormation(request.getHomeLineup().getFormation());
        homeLineup.setCoach(request.getHomeLineup().getCoach());
        homeLineup.setStartingXIIds(request.getHomeLineup().getStartingXIIds());
        homeLineup.setSubstituteIds(request.getHomeLineup().getSubstituteIds());
        match.setHomeLineup(homeLineup);

        TeamLineup awayLineup = new TeamLineup();
        awayLineup.setFormation(request.getAwayLineup().getFormation());
        awayLineup.setCoach(request.getAwayLineup().getCoach());
        awayLineup.setStartingXIIds(request.getAwayLineup().getStartingXIIds());
        awayLineup.setSubstituteIds(request.getAwayLineup().getSubstituteIds());
        match.setAwayLineup(awayLineup);

        return matchRepository.save(match);
    }

    // Keep all other existing methods (goals, events, stats) unchanged...
    public Match addGoal(Long matchId, Goal goal) {
        Match match = matchRepository.findById(matchId).orElseThrow();
        goal.setMatch(match);
        goalRepository.save(goal);

        if (match.getHomeScore() == null) match.setHomeScore(0);
        if (match.getAwayScore() == null) match.setAwayScore(0);

        if ("home".equals(goal.getTeam())) {
            match.setHomeScore(match.getHomeScore() + 1);
        } else {
            match.setAwayScore(match.getAwayScore() + 1);
        }

        return matchRepository.save(match);
    }

    public Match updateGoal(Long matchId, Long goalId, Goal updated) {
        Goal existing = goalRepository.findById(goalId).orElseThrow();
        updated.setId(goalId);
        updated.setMatch(existing.getMatch());
        goalRepository.save(updated);
        return getMatch(matchId);
    }

    public Match deleteGoal(Long matchId, Long goalId) {
        goalRepository.deleteById(goalId);
        return getMatch(matchId);
    }

    public List<Goal> getGoalsByMatchId(Long matchId) {
        return goalRepository.findByMatchId(matchId);
    }

    public MatchEvent addMatchEvent(Long matchId, MatchEvent event) {
        Match match = getMatch(matchId);
        event.setMatch(match);
        return matchEventRepository.save(event);
    }

    public List<MatchEvent> getMatchEvents(Long matchId, String type, String search) {
        if (type != null) return matchEventRepository.findByMatchIdAndType(matchId, type);
        if (search != null) return matchEventRepository.findByMatchIdAndDescriptionContainingIgnoreCase(matchId, search);
        return matchEventRepository.findByMatchId(matchId);
    }

    public MatchEvent updateMatchEvent(Long matchId, Long eventId, MatchEvent updated) {
        MatchEvent existing = matchEventRepository.findById(eventId).orElseThrow();
        updated.setId(eventId);
        updated.setMatch(existing.getMatch());
        return matchEventRepository.save(updated);
    }

    public Match resetMatchState(Long matchId) {
        Match match = getMatch(matchId);

        match.setStatus("Scheduled");
        match.setCurrentMinute(0);
        match.setPeriod("Pre-Match");
        match.setRunning(false);
        match.setAddedTime(new AddedTime());
        match.setStartTime(null);
        match.setHalfTimeStart(null);
        match.setSecondHalfStart(null);
        match.setLastPauseTime(null);
        match.setTotalPausedMillis(0L);
        match.setHomeScore(0);
        match.setAwayScore(0);

        goalRepository.deleteByMatchId(matchId);
        matchEventRepository.deleteByMatchId(matchId);

        return matchRepository.save(match);
    }

    public MatchStats getStats(Long matchId) {
        return matchStatsRepository.findByMatchId(matchId).orElseGet(() -> {
            MatchStats stats = new MatchStats();
            stats.setMatchId(matchId);
            return matchStatsRepository.save(stats);
        });
    }

    public MatchStats updateStats(Long matchId, MatchStats updated) {
        updated.setMatchId(matchId);
        return matchStatsRepository.save(updated);
    }

    public MatchStats incrementStat(Long matchId, StatIncrementRequest request) {
        MatchStats stats = getStats(matchId);
        int value = request.getValue();

        switch (request.getStatType()) {
            case "homeShots": stats.setHomeShots(stats.getHomeShots() + value); break;
            case "awayShots": stats.setAwayShots(stats.getAwayShots() + value); break;
            case "homeFouls": stats.setHomeFouls(stats.getHomeFouls() + value); break;
            case "awayFouls": stats.setAwayFouls(stats.getAwayFouls() + value); break;
            case "homeCorners": stats.setHomeCorners(stats.getHomeCorners() + value); break;
            case "awayCorners": stats.setAwayCorners(stats.getAwayCorners() + value); break;
        }

        return matchStatsRepository.save(stats);
    }

    // Deprecated - matches now start automatically
    @Deprecated
    public Match updateMatchTimer(Long matchId) {
        return getMatch(matchId); // Just return updated state
    }
}