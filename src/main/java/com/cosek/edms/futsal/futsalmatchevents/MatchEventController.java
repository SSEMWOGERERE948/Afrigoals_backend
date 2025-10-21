package com.cosek.edms.futsal.futsalmatchevents;

import com.cosek.edms.futsal.entity.MatchfEvent;
import com.cosek.edms.matches.MatchEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
@CrossOrigin
public class MatchEventController {
    @Autowired
    private MatchfEventRepository eventRepo;

    @GetMapping
    public List<MatchfEvent> getAll() {
        return eventRepo.findAll();
    }

    @PostMapping
    public MatchfEvent create(@RequestBody MatchfEvent event) {
        return eventRepo.save(event);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        eventRepo.deleteById(id);
    }
}

