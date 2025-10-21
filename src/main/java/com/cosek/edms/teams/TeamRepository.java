package com.cosek.edms.teams;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// TeamRepository.java
public interface TeamRepository extends JpaRepository<Team, Long> {
    List<Team> findByTeamTypeIgnoreCase(String type);

}
