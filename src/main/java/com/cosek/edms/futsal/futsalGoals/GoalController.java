package com.cosek.edms.futsal.futsalGoals;

import com.cosek.edms.futsal.entity.Goals;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/goals")
@CrossOrigin
public class GoalController {
    @Autowired
    private GoalfRepository goalRepo;

    @GetMapping
    public List<Goals> getAll() {
        return goalRepo.findAll();
    }

    @PostMapping
    public Goals create(@RequestBody Goals goal) {
        return goalRepo.save(goal);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        goalRepo.deleteById(id);
    }
}

