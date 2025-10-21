package com.cosek.edms.futsal.futsallineups;

import com.cosek.edms.futsal.dto.TeamLineupDTO;
import com.cosek.edms.futsal.dto.TeamLineupFullDTO;
import com.cosek.edms.futsal.entity.TeamLineup;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/futsalmatches/{matchId}/lineups")
@RequiredArgsConstructor
public class LineupController {

    private final LineupService svc;

    @PutMapping("/{side}")
    public void saveLineup(@PathVariable Long matchId,
                           @PathVariable TeamLineup.Side side,
                           @RequestBody TeamLineupDTO body) {
        svc.saveLineup(matchId, body.withSide(side));
    }

    @GetMapping("/{side}")
    public TeamLineupFullDTO getLineup(@PathVariable Long matchId,
                                       @PathVariable TeamLineup.Side side) {
        return svc.getLineup(matchId, side);
    }
}
