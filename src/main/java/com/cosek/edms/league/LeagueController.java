package com.cosek.edms.league;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/leagues")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class LeagueController {

    private final LeagueRepository leagueRepository;

    @GetMapping("/all")
    public List<League> getAllLeagues() {
        return leagueRepository.findAll();
    }

    @PostMapping("/create")
    public League createLeague(@RequestBody League league) {
        return leagueRepository.save(league);
    }

    @PutMapping("/{id}")
    public ResponseEntity<League> updateLeague(@PathVariable UUID id, @RequestBody League updated) {
        return leagueRepository.findById(id)
                .map(existing -> {
                    existing.setName(updated.getName());
                    existing.setCountry(updated.getCountry());
                    existing.setLogo(updated.getLogo());
                    existing.setSeason(updated.getSeason());
                    existing.setTeams(updated.getTeams());
                    existing.setCurrentMatchday(updated.getCurrentMatchday());
                    return ResponseEntity.ok(leagueRepository.save(existing));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteLeague(@PathVariable UUID id) {
        return leagueRepository.findById(id)
                .map(league -> {
                    leagueRepository.delete(league);
                    return ResponseEntity.noContent().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}

