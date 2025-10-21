package com.cosek.edms.futsal.futsalleagues;

import com.cosek.edms.futsal.entity.FutsalLeague;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/futsalleagues")
@CrossOrigin
public class FutsalLeagueController {
    @Autowired
    private FutsalLeagueService leagueService;

    @GetMapping("/")
    public List<FutsalLeague> getAll() {
        return leagueService.getAll();
    }

    @GetMapping("/{id}")
    public FutsalLeague getById(@PathVariable Long id) {
        return leagueService.getById(id);
    }

    @PostMapping("/create")
    public FutsalLeague create(@RequestBody FutsalLeague league) {
        return leagueService.create(league);
    }

    @PutMapping("/{id}")
    public FutsalLeague update(@PathVariable Long id, @RequestBody FutsalLeague league) {
        return leagueService.update(id, league);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        leagueService.delete(id);
    }
}

