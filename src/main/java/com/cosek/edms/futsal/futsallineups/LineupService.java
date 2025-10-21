package com.cosek.edms.futsal.futsallineups;

import com.cosek.edms.futsal.dto.PlayerOnFieldDTO;
import com.cosek.edms.futsal.dto.TeamLineupDTO;
import com.cosek.edms.futsal.dto.TeamLineupFullDTO;
import com.cosek.edms.futsal.entity.*;
import com.cosek.edms.futsal.futsalmatches.FutsalMatchRepository;
import com.cosek.edms.futsal.futsalplayers.FutsalPlayerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LineupService {

    private final TeamLineupRepository   lineupRepo;
    private final LineupPlayerRepository lpRepo;
    private final FutsalMatchRepository  matchRepo;
    private final FutsalPlayerRepository playerRepo;

    @Transactional
    public void saveLineup(Long matchId, TeamLineupDTO dto) {
        FutsalMatch match = matchRepo.findById(matchId)
                .orElseThrow(() -> new IllegalArgumentException("Match not found"));

        // Validate formation and player count
        validateFormationAndPlayerCount(dto.formation(), dto.startingXIIds().size());

        // Find existing lineup or create new one
        TeamLineup lineup = lineupRepo.findByMatchIdAndSide(matchId, dto.side())
                .orElse(null);

        if (lineup == null) {
            // Create new lineup
            lineup = new TeamLineup();
            lineup.setMatch(match);
            lineup.setSide(dto.side());
        }

        // Update lineup properties
        lineup.setFormation(dto.formation());
        lineup.setCoach(dto.coach());

        // Save the lineup first to get the ID
        lineup = lineupRepo.save(lineup);

        // Clear existing players for this lineup
        if (lineup.getId() != null) {
            lpRepo.deleteByLineupId(lineup.getId());
        }

        // Add starting XI
        for (int i = 0; i < dto.startingXIIds().size(); i++) {
            addLp(lineup, dto.startingXIIds().get(i), true, i, dto.formation());
        }

        // Add substitutes
        int benchIndex = 0;
        for (Long pid : dto.substituteIds()) {
            addLp(lineup, pid, false, benchIndex++, dto.formation());
        }
    }

    private void validateFormationAndPlayerCount(String formation, int playerCount) {
        if (formation == null || formation.trim().isEmpty()) {
            throw new IllegalArgumentException("Formation cannot be null or empty");
        }

        try {
            int expectedPlayers = calculateTotalPositions(formation);
            if (playerCount != expectedPlayers) {
                throw new IllegalArgumentException(
                        String.format("Formation '%s' expects %d players but received %d starting players",
                                formation, expectedPlayers, playerCount));
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid formation format: " + formation, e);
        }
    }

    private int calculateTotalPositions(String formation) {
        String[] rows = formation.split("-");
        int total = 0;
        for (String row : rows) {
            if (row.trim().isEmpty()) {
                throw new IllegalArgumentException("Formation contains empty segments");
            }
            total += Integer.parseInt(row.trim());
        }
        return total;
    }

    private void addLp(TeamLineup lineup, Long playerId, boolean starting, int posInOrder, String formation) {
        // Validate that lineup has an ID
        if (lineup.getId() == null) {
            throw new IllegalStateException("Lineup must be saved before adding players");
        }

        LineupPlayer lp = new LineupPlayer();
        lp.setLineup(lineup);
        lp.setPlayer(playerRepo.getReferenceById(playerId));
        lp.setStarting(starting);

        if (starting) {
            int[] rc = calcRowCol(posInOrder, formation);
            lp.setRowIndex(rc[0]);
            lp.setColIndex(rc[1]);
        } else {
            lp.setRowIndex(-1);
            lp.setColIndex(-1);
        }

        lpRepo.save(lp);
    }

    private int[] calcRowCol(int slot, String formation) {
        if (formation == null || formation.trim().isEmpty()) {
            throw new IllegalArgumentException("Formation cannot be null or empty");
        }

        String[] rows = formation.split("-");
        int offset = 0;

        for (int row = 0; row < rows.length; row++) {
            try {
                int count = Integer.parseInt(rows[row].trim());
                if (count <= 0) {
                    throw new IllegalArgumentException("Formation contains invalid player count: " + count);
                }

                if (slot < offset + count) {
                    int col = slot - offset;
                    return new int[]{row, col};
                }
                offset += count;
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid formation segment: " + rows[row], e);
            }
        }

        throw new IllegalArgumentException(
                String.format("Slot index %d is out of range for formation '%s' (max slots: %d)",
                        slot, formation, offset));
    }

    @Transactional
    public TeamLineupFullDTO getLineup(Long matchId, TeamLineup.Side side) {
        TeamLineup tl = lineupRepo.findByMatchIdAndSide(matchId, side)
                .orElseThrow(() -> new IllegalArgumentException("No lineup found for match " + matchId + " and side " + side));

        List<PlayerOnFieldDTO> starters = tl.getPlayers().stream()
                .filter(LineupPlayer::isStarting)
                .sorted(Comparator.comparingInt(LineupPlayer::getRowIndex)
                        .thenComparingInt(LineupPlayer::getColIndex))
                .map(this::toPof)
                .collect(Collectors.toList());

        List<PlayerOnFieldDTO> subs = tl.getPlayers().stream()
                .filter(lp -> !lp.isStarting())
                .map(this::toPof)
                .collect(Collectors.toList());

        return new TeamLineupFullDTO(tl.getFormation(), tl.getCoach(), starters, subs);
    }

    private PlayerOnFieldDTO toPof(LineupPlayer lp) {
        FutsalPlayer p = lp.getPlayer();
        return new PlayerOnFieldDTO(
                p.getId(),
                p.getName(),
                p.getNumber(),
                p.getPositionId(),
                p.getTeam() != null ? p.getTeam().getTeamName() : "Unknown",
                lp.isStarting(),
                lp.getRowIndex(),
                lp.getColIndex()
        );
    }
}