package com.cosek.edms.substitution;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/substitution")
public class SubstitutionController {

    @Autowired
    private SubstitutionRepository substitutionRepository;

    @PostMapping("/{matchId}/save")
    public List<Substitution> saveSubstitutions(
            @PathVariable Long matchId,
            @RequestBody SubstitutionRequest request
    ) {
        List<Substitution> substitutions = request.getSubstitutions();
        substitutions.forEach(sub -> sub.setMatchId(matchId));
        return substitutionRepository.saveAll(substitutions);
    }


    @GetMapping("/{matchId}/fetch")
    public List<Substitution> getSubstitutions(@PathVariable Long matchId) {
        return substitutionRepository.findByMatchId(matchId);
    }

    @PutMapping("/{matchId}/update/{id}")
    public Substitution updateSub(@PathVariable Long id, @RequestBody Substitution updated) {
        Substitution sub = substitutionRepository.findById(id).orElseThrow();
        sub.setMinute(updated.getMinute());
        sub.setReason(updated.getReason());
        return substitutionRepository.save(sub);
    }

    @DeleteMapping("/{matchId}/substitutions/{id}")
    public void deleteSub(@PathVariable Long id) {
        substitutionRepository.deleteById(id);
    }
}

