package com.cosek.edms.futsal.futsalmatches;

import com.cosek.edms.futsal.entity.FutsalMatch;
import com.cosek.edms.futsal.entity.Goals;
import com.cosek.edms.futsal.entity.MatchPeriod;
import com.cosek.edms.futsal.entity.MatchfEvent;
import com.cosek.edms.futsal.futsalGoals.GoalfRepository;
import com.cosek.edms.futsal.futsalmatchevents.MatchfEventRepository; // Fixed import
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.cosek.edms.futsal.util.PeriodStateUtils;
import com.cosek.edms.futsal.entity.PeriodState;

@Service
public class FutsalMatchService {
    @Autowired
    private FutsalMatchRepository matchRepo;

    @Autowired
    private GoalfRepository goalsRepo;

    @Autowired
    private MatchfEventRepository eventRepo; // Fixed repository type

    public List<FutsalMatch> getAll() {
        return matchRepo.findAll();
    }

    public FutsalMatch getById(Long id) {
        FutsalMatch match = matchRepo.findById(id).orElseThrow();

        // Compute and set period state for live matches
        if ("Live".equalsIgnoreCase(match.getStatus())) {
            PeriodState currentState = computePeriodState(match);
            match.setPeriodState(currentState);
        }

        return match;
    }

    PeriodState computePeriodState(FutsalMatch match) {
        if (match.getStartTime() == null || !"Live".equalsIgnoreCase(match.getStatus())) {
            return null;
        }

        PeriodState state = new PeriodState();

        // ✅ Convert java.util.Date to milliseconds
        Date startTime = match.getStartTime();
        long matchStartMillis = startTime.getTime();
        long nowMillis = System.currentTimeMillis();

        long elapsedMs = nowMillis - matchStartMillis;

        // ✅ Convert to seconds + minutes explicitly
        int elapsedSeconds = (int) (elapsedMs / 1000L);
        int elapsedMinutes = elapsedSeconds / 60;

        // ✅ Fill PeriodState with both ms and precomputed seconds/minutes
        state.setStartTime(startTime);
        state.setElapsedTimeInCurrentPeriodMs((int) elapsedMs); // Keep ms for high-res use
        state.setElapsedSeconds(elapsedSeconds);               // 🔥 New field in PeriodState
        state.setElapsedMinutes(elapsedMinutes);               // 🔥 New field in PeriodState
        state.setTotalPlayingTimeMs((int) elapsedMs);
        state.setLastUpdatedTimestamp(nowMillis);
        state.setMatchPaused(false);
        state.setBreakPeriod(false);
        state.setCurrentPeriodId(1L);
        state.setCurrentPeriodOrder(0);

        return state;
    }

    public FutsalMatch updateMatchPeriods(Long matchId, List<MatchPeriod> periods) {
        FutsalMatch match = getById(matchId);

        // Clear existing periods
        if (match.getPeriods() == null) {
            match.setPeriods(new ArrayList<>());
        }
        match.getPeriods().clear();

        // Add new periods
        if (periods != null) {
            for (MatchPeriod period : periods) {
                period.setMatch(match); // Set the relationship
                match.getPeriods().add(period);
            }

            // 🔥 NEW: Auto-calculate and update matchDuration
            int totalPlayingDuration = periods.stream()
                    .filter(p -> !p.isBreakPeriod())
                    .mapToInt(MatchPeriod::getDuration)
                    .sum();

            match.setMatchDuration(totalPlayingDuration);
        }

        return matchRepo.save(match);
    }

    public FutsalMatch update(Long id, FutsalMatch updated) {
        FutsalMatch existing = getById(id);

        // Only update fields that are not null in the updated object
        if (updated.getHomeTeam() != null) existing.setHomeTeam(updated.getHomeTeam());
        if (updated.getAwayTeam() != null) existing.setAwayTeam(updated.getAwayTeam());
        if (updated.getVenue() != null) existing.setVenue(updated.getVenue());
        if (updated.getReferee() != null) existing.setReferee(updated.getReferee());
        if (updated.getLeague() != null) existing.setLeague(updated.getLeague());
        if (updated.getDate() != null) existing.setDate(updated.getDate());
        if (updated.getTime() != null) existing.setTime(updated.getTime());
        if (updated.getTeamSize() > 0) existing.setTeamSize(updated.getTeamSize());
        if (updated.getMatchDuration() > 0) existing.setMatchDuration(updated.getMatchDuration());
        if (updated.getMaxSubstitutions() > 0) existing.setMaxSubstitutions(updated.getMaxSubstitutions());
        if (updated.getStatus() != null) existing.setStatus(updated.getStatus());
        if (updated.getHomeScore() != null) existing.setHomeScore(updated.getHomeScore());
        if (updated.getAwayScore() != null) existing.setAwayScore(updated.getAwayScore());
        if (updated.getStartTime() != null) existing.setStartTime(updated.getStartTime());
        if (updated.getCurrentMinute() != null) existing.setCurrentMinute(updated.getCurrentMinute());
        if (updated.getFirstHalfDuration() != null) existing.setFirstHalfDuration(updated.getFirstHalfDuration());
        if (updated.getSecondHalfDuration() != null) existing.setSecondHalfDuration(updated.getSecondHalfDuration());
        if (updated.getHalvesJson() != null) existing.setHalvesJson(updated.getHalvesJson());

        // Handle collections only if they're provided
        if (updated.getLineups() != null) {
            if (existing.getLineups() == null) {
                existing.setLineups(new ArrayList<>());
            }
            existing.getLineups().clear();
            for (var lineup : updated.getLineups()) {
                lineup.setMatch(existing);
                existing.getLineups().add(lineup);
            }
        }

        if (updated.getPeriods() != null) {
            if (existing.getPeriods() == null) {
                existing.setPeriods(new ArrayList<>());
            }
            existing.getPeriods().clear();
            for (var period : updated.getPeriods()) {
                period.setMatch(existing);
                existing.getPeriods().add(period);
            }
        }

        return matchRepo.save(existing);
    }

    public void delete(Long id) {
        matchRepo.deleteById(id);
    }

    // Add a goal to a match
    public Goals addGoalToMatch(Long matchId, Goals goal) {
        FutsalMatch match = getById(matchId); // fetch the match from DB
        goal.setMatch(match);                 // link goal to match

        // Optional: update score based on which team scored
        if ("home".equalsIgnoreCase(goal.getTeam())) {
            match.setHomeScore((match.getHomeScore() == null ? 0 : match.getHomeScore()) + 1);
        } else if ("away".equalsIgnoreCase(goal.getTeam())) {
            match.setAwayScore((match.getAwayScore() == null ? 0 : match.getAwayScore()) + 1);
        }

        matchRepo.save(match); // persist updated score
        return goalsRepo.save(goal); // save the goal
    }


    // Add an event to a match
    public MatchfEvent addEventToMatch(Long matchId, MatchfEvent event) {
        FutsalMatch match = getById(matchId);
        event.setMatch(match);
        return eventRepo.save(event);
    }

    // Update match score manually
    public FutsalMatch updateMatchScore(Long matchId, Integer homeScore, Integer awayScore) {
        FutsalMatch match = getById(matchId);
        match.setHomeScore(homeScore);
        match.setAwayScore(awayScore);
        return matchRepo.save(match);
    }

    // Get match details with goals and events
    public FutsalMatch getMatchDetails(Long matchId) {
        FutsalMatch match = getById(matchId); // This will now include computed period state
        return match;
    }

    // Get all goals for a match
    public List<Goals> getMatchGoals(Long matchId) {
        return goalsRepo.findByMatchId(matchId);
    }

    // Get all events for a match
    public List<MatchfEvent> getMatchEvents(Long matchId) {
        return eventRepo.findByMatchId(matchId);
    }

    // Update match status
    public FutsalMatch updateMatchStatus(Long matchId, String status) {
        FutsalMatch match = getById(matchId);
        match.setStatus(status);
        return matchRepo.save(match);
    }


    public FutsalMatch startMatch(Long matchId) {
        FutsalMatch match = getById(matchId);
        match.setStatus("Live");

        Date now = new Date(); // ✅ Use java.util.Date instead of LocalDateTime
        match.setStartTime(now);

        // Initialize and persist periodState
        PeriodState state = new PeriodState();
        state.setStartTime(now); // ✅ Date type
        state.setMatchPaused(false);
        state.setLastUpdatedTimestamp(System.currentTimeMillis());
        state.setElapsedTimeInCurrentPeriodMs(0);
        state.setTotalPlayingTimeMs(0);
        match.setPeriodState(state);
        match.setPeriodStateJson(PeriodStateUtils.toJson(state));

        return matchRepo.save(match);
    }

    // End a match
    public FutsalMatch endMatch(Long matchId) {
        FutsalMatch match = getById(matchId);
        match.setStatus("Finished");
        return matchRepo.save(match);
    }

    // Reset a match
    @Transactional
    public FutsalMatch resetMatch(Long matchId) {
        FutsalMatch match = getById(matchId);

        // Delete all goals and events
        goalsRepo.deleteByMatchId(matchId);
        eventRepo.deleteByMatchId(matchId);

        // Reset scores and status
        match.setHomeScore(0);
        match.setAwayScore(0);
        match.setStatus("Scheduled");
        match.setStartTime(null);

        return matchRepo.save(match);
    }

    // Helper method to update match score based on goals
    private void updateMatchScoreFromGoals(Long matchId) {
        List<Goals> goals = getMatchGoals(matchId);

        int homeScore = 0;
        int awayScore = 0;

        for (Goals goal : goals) {
            if ("home".equalsIgnoreCase(goal.getTeam())) {
                if (!"own_goal".equalsIgnoreCase(goal.getType())) {
                    homeScore++;
                } else {
                    awayScore++; // Own goal counts for the other team
                }
            } else if ("away".equalsIgnoreCase(goal.getTeam())) {
                if (!"own_goal".equalsIgnoreCase(goal.getType())) {
                    awayScore++;
                } else {
                    homeScore++; // Own goal counts for the other team
                }
            }
        }

        updateMatchScore(matchId, homeScore, awayScore);
    }
}