package com.cosek.edms.futsal.futsalplayers;

import com.cosek.edms.futsal.entity.FutsalPlayer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FutsalPlayerRepository extends JpaRepository<FutsalPlayer, Long> {}

