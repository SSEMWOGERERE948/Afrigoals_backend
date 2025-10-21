package com.cosek.edms.teams;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

// TeamService.java
@Service
public class TeamService {

    @Autowired
    private TeamRepository teamRepository;

    public List<Team> getAllTeams() {
        return teamRepository.findAll();
    }

    public Team getTeam(Long id) {
        return teamRepository.findById(id).orElseThrow(() -> new RuntimeException("Team not found"));
    }

    public Team saveTeam(Team team) {
        return teamRepository.save(team);
    }

    public void deleteTeam(Long id) {
        teamRepository.deleteById(id);
    }

    public List<Team> getTeamsByType(String type) {
        return teamRepository.findByTeamTypeIgnoreCase(type);
    }
}

