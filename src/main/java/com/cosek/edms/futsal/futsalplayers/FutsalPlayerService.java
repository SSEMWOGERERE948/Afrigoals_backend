package com.cosek.edms.futsal.futsalplayers;

import com.cosek.edms.futsal.entity.FutsalPlayer;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FutsalPlayerService {
    @Autowired
    private FutsalPlayerRepository playerRepo;

    public List<FutsalPlayer> getAll() {
        return playerRepo.findAll();
    }

    public FutsalPlayer getById(Long id) {
        return playerRepo.findById(id).orElseThrow();
    }

    public FutsalPlayer create(FutsalPlayer player) {
        return playerRepo.save(player);
    }

    public FutsalPlayer update(Long id, FutsalPlayer updated) {
        FutsalPlayer existing = getById(id);
        BeanUtils.copyProperties(updated, existing, "id");
        return playerRepo.save(existing);
    }

    public void delete(Long id) {
        playerRepo.deleteById(id);
    }
}

