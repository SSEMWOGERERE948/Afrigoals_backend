package com.cosek.edms.futsal.futsalleagues;

import com.cosek.edms.futsal.entity.FutsalLeague;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FutsalLeagueService {
    @Autowired
    private FutsalLeagueRepository leagueRepo;

    public List<FutsalLeague> getAll() {
        return leagueRepo.findAll();
    }

    public FutsalLeague getById(Long id) {
        return leagueRepo.findById(id).orElseThrow();
    }

    public FutsalLeague create(FutsalLeague league) {
        return leagueRepo.save(league);
    }

    public FutsalLeague update(Long id, FutsalLeague updated) {
        FutsalLeague existing = getById(id);
        BeanUtils.copyProperties(updated, existing, "id");
        return leagueRepo.save(existing);
    }

    public void delete(Long id) {
        leagueRepo.deleteById(id);
    }
}
